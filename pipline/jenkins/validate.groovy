pipeline {
    agent any

    stages {
        stage('Validate core') {
            agent {
                docker {
                    image 'maven:3'
                    //noinspection GroovyAssignabilityCheck
                    args '-v jenkins_m2:/root/.m2'
                    reuseNode true
                }
            }
            stages {
                stage('Checkout') {
                    steps {
                        git branch: "${env.BRANCH_NAME ?: 'master'}", credentialsId: 'github-app', url: 'https://github.com/Web-tree/auth.git'
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
}