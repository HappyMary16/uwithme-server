FROM ubuntu:20.04
RUN apt-get update
RUN apt-get install -y openjdk-11-jdk
COPY university-with-me-service/target/EducationApp.jar /usr/app/EducationApp.jar
CMD java -jar -Dspring.profiles.active=prod /usr/app/EducationApp.jar