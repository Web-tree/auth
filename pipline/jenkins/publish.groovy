library identifier: 'webtree-lib@master', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/Web-tree/jenkins.git'])

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
                publishInArtifactory('artifactory-passwd', 'org/webtree/auth-core', 'core/target/core')
            }
        }
    }
}
