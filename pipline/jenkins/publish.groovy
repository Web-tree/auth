pipeline {
    agent {
        docker {
            image 'maven:3'
            args '-v jenkins_m2:/root/.m2'
        }
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Publish in artifactory') {
            steps {
                script {
                    def version = sh(
                            script: 'mvn -q -Dexec.executable=echo -Dexec.args=\'${project.version}\' --non-recursive exec:exec',
                            returnStdout: true
                    ).trim()
                    withCredentials([usernamePassword(credentialsId: 'artifactory-passwd', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh "curl -u ${USERNAME}:${PASSWORD} -X put https://artifactory.webtree.org/artifactory/list/dev/org/webtree/auth-core/${version}/ -T core/target/core-${version}.jar"
                    }
                }
            }
        }
    }
}
