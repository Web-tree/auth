pipeline {
    agent any
    stages {
        stage('Env setup') {
            steps {
                script {
                    buildVersion = env.GIT_BRANCH.toLowerCase()
                    isPullRequest = env.CHANGE_ID ? true : false
                    branch = isPullRequest ? env.CHANGE_BRANCH : env.GIT_BRANCH
                    switch (branch) {
                        case 'master':
                            tier = 'prod'
                            def time = new Date().format('yyyyMMddHH.mm.ss')
                            coreTag = "auth-core-${tier}-${buildVersion}-${time}"
                            break
                        default:
                            tier = 'dev'
                            coreTag = "auth-core-${tier}-${buildVersion}"
                    }
                }
            }
        }
        stage('Validate core') {
            agent {
                kubernetes {
                    label 'auth-validate-maven'
                    containerTemplate {
                        name 'maven'
                        image 'maven:3.3.9-jdk-8-alpine'
                        ttyEnabled true
                        command 'cat'
                    }
                }
            }
            stages {
                stage('Checkout') {
                    steps {
                        git branch: "${env.BRANCH_NAME ?: 'master'}", credentialsId: 'github-repo-token', url: 'https://github.com/Web-tree/auth.git'
                    }
                }
                stage('Build') {
                    steps {
                        sh 'mvn verify -Dmaven.test.failure.ignore=true'
                    }
                }
                stage('Publish test results') {
                    steps {
                        junit allowEmptyResults: true, testResults: '**/target/surefire-reports/**/*.xml'
                    }
                }
            }
        }
        stage('Test system provision') {
            stages {
                stage('Build core image') {
                    agent {
                        kubernetes {
                            label 'auth-docker-builder'
                            yamlFile 'pipeline/jenkins/agent/dockerBuilder.yaml'
                        }
                    }
                    steps {
                        container('docker-builder') {
                            script {
                                withDockerRegistry(credentialsId: 'docker-hub') {
                                    def image = docker.build("webtree/auth:${coreTag}")
                                    image.push(coreTag)
                                }
                            }
                        }
                    }
                }
                stage('Provision PR system') {
                    when {
                        expression { tier == 'dev' && isPullRequest }
                    }
                    agent {
                        kubernetes {
                            label 'helm'
                            containerTemplate {
                                name 'helm'
                                image 'lachlanevenson/k8s-helm:v2.12.3'
                                ttyEnabled true
                                command 'cat'
                            }
                        }
                    }
                    steps {
                        dir('.k8s/chart') {
                            deployDevEnv(buildVersion, coreTag, "auth", tier)
                        }
                    }
                }
                stage('Update production') {
                    when {
                        expression { tier == 'prod' }
                    }
                    agent {
                        kubernetes {
                            label 'helm'
                            containerTemplate {
                                name 'helm'
                                image 'lachlanevenson/k8s-helm:v2.12.3'
                                ttyEnabled true
                                command 'cat'
                            }
                        }
                    }
                    steps {
                        dir('.k8s/chart') {
                            updateProduction(coreTag, "auth")
                        }
                    }
                }
            }
        }
    }
    post {
        success {
            slackSend(color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        unstable {
            slackSend(color: '#FFFF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            slackSend(color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
    }
}

private void deployDevEnv(buildVersion, coreTag, projectName, tier) {
    String deployName = "${projectName}-${tier}-${buildVersion}"
    String coreUrl = "${projectName}-${buildVersion}.dev.webtree.org"
    sh 'helm init --client-only'
    sh 'helm repo add incubator https://kubernetes-charts-incubator.storage.googleapis.com/'
    sh "helm delete ${deployName} --purge || true"
    sh 'helm dependency build'
    sh "helm install --wait --name=${deployName} --namespace=webtree-${tier} --set nameOverride=${deployName},ingress.hosts[0].paths[0]=/rest,ingress.hosts[0].host=${coreUrl},image.tag=${coreTag},cassandra.host=${deployName}-cassandra -f values.${tier}.yaml ."
    String message = "Test system provisioned on url https://${coreUrl}"
    sendPrComment('auth', env.CHANGE_ID, message)

}

private void updateProduction(coreTag, projectName) {
    sh "helm upgrade --wait ${projectName} -f values.yaml --set images.web.tag=${coreTag} ."
}

private void sendPrComment(repo, issueId, message) {
    def body = groovy.json.JsonOutput.toJson([body: message])
    httpRequest(consoleLogResponseBody: true,
        contentType: 'APPLICATION_JSON',
        httpMode: 'POST',
        requestBody: body,
        url: "https://api.github.com/repos/Web-tree/${repo}/issues/${issueId}/comments",
        authentication: 'github-repo-token',
        validResponseCodes: '201')
}
