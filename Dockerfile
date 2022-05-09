FROM ubuntu:20.04
RUN apt-get update
RUN apt-get install -y openjdk-11-jdk
COPY uwithme-service/target/UwithmeServiceApp.jar /usr/app/UwithmeServiceApp.jar
CMD java -jar -Dspring.profiles.active=prod /usr/app/UwithmeServiceApp.jar