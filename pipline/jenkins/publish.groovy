library identifier: 'webtree-lib@master', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/Web-tree/jenkins.git'])

pipeline {
    agent {
        docker {
            image 'maven:3-jdk-8-alpine'
            args '-v jenkins_m2:/root/.m2'
        }
    }

    environment {
        MAVEN_HOME = '/usr/share/maven'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
    }
    stages {
        stage('Publish in artifactory') {
            steps {
                publishInArtifactory()
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
