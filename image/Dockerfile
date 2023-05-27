FROM openjdk:11.0-jre

COPY ./uwithme-service/target/UwithmeServiceApp.jar /usr/app/UwithmeServiceApp.jar
COPY ./image/application.properties /usr/app/application.properties

WORKDIR /usr/app

CMD java -jar /usr/app/UwithmeServiceApp.jar