pipeline {
    agent any
     tools {
        maven 'M3'
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
        stage('Initialize'){
            steps {
                 def dockerHome = tool 'docker_jenkins'
                 env.PATH = "${dockerHome}/bin:${env.PATH}"
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
