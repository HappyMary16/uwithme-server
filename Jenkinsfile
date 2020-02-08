pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
               bat 'mvn clean install'
            }
        }

        stage('Deploy') {
            steps {
                bat 'java -Dfile.encoding=utf-8 -jar  -Xmx1536m target/server-0.0.1-SNAPSHOT.jar & false'
            }
        }

        stage('End') {
            steps {
                echo 'END'
            }
        }
    }
}