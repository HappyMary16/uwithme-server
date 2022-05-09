# University With Me

This application created for management of university.

Link: https://u-with-me.education

#### Main functions:
* schedule management
* files management
* university structure management

## For usage the application you need
* [Java 11](https://adoptopenjdk.net/index.html?variant=openjdk11&jvmVariant=hotspot)
* [Maven 3](https://maven.apache.org/index.html)
* [PostgreSql 14.2](https://www.postgresql.org/)
* [Keycloak 18.0.0](https://www.keycloak.org/)

You can use doker compose file located in [uwithme-docker-files](https://github.com/HappyMary16/uwithme-docker-files) to download and start Postgres and Keycloak containers.

## Running the application

##### You can run the application with Maven:
```bash
mvn spring-boot:run
```
##### or you can use IDE:

Run the ```main``` method in the ```UwithmeServiceApp``` class
