FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache gettext

COPY ./uwithme-service/target/UwithmeServiceApp.jar /usr/app/UwithmeServiceApp.jar

COPY ./image/application.properties.tmpl /usr/app/application.properties.tmpl
COPY ./image/substitute.sh /opt/uwm/substitute.sh

WORKDIR /usr/app

CMD /opt/uwm/substitute.sh && java -jar /usr/app/UwithmeServiceApp.jar