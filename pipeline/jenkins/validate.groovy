pipeline {
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
        stage('Validate core') {
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
    }
    post {
        success {
            slackSend(color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            slackSend(color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
    }
}