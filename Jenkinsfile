pipeline {
    agent any
     tools {
        maven 'M3'
        jenkins_docker 'jenkins_docker'
    }
  
    environment {
        DOCKERHUB_CREDENTIALS=credentials('jenkins_docker')
    }
    stages {
        stage('Git clone') {
            steps {
                git branch: 'main', url: 'https://github.com/jtobiora/coding_interview_challenge.git'
            }
        }
        stage('Maven Build') {
            steps {
                 sh 'mvn package'
            }
        }
        stage("Deploy") {
            steps {
                 script {
                     sh 'docker build -t 514801/transaction-statistics-api .'
                 }
            }
        }
    }
}
