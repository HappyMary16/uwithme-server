pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
               sh 'mvn clean install'
            }
        }

        stage('Deploy') {
            steps {
                echo 'java -Dfile.encoding=utf-8 -jar  -Xmx1536m target/server-0.0.1-SNAPSHOT.jar'
            }
        }

        stage('End') {
            sh {
                echo 'END'
            }
        }
    }
}