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
                sh 'java -Dfile.encoding=utf-8 -jar ..\EducationAppServer\target\server-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}