pipeline {
    agent any
     tools {
        maven 'M3'
    }
    environment {
        PATH = "$PATH:/usr/local/bin" // Add the directory containing Docker executable
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
