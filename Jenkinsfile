pipeline {
    agent any
     tools {
        maven 'M3'
        docker 'Docker_Jenkins' 
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
                 sh 'docker build -t transaction-statistics-api .'
            }
        }
    }
}
