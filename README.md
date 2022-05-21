# University With Me

[![Build Status](https://github.com/HappyMary16/uwithme-server/actions/workflows/main.yml/badge.svg)](https://github.com/HappyMary16/uwithme-server/actions/workflows/main.yml)
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL_v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)

This application is created to manage a university.

Link: https://u-with-me.education

#### Main features:
* schedule management
* files management
* university structure management

## To build the application you need
* [Java 11](https://adoptopenjdk.net/index.html?variant=openjdk11&jvmVariant=hotspot)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)

## Running the application

You can run this application by

1. Downloading [Docker files](https://github.com/HappyMary16/uwithme-docker-files)
2. Starting them with 
```
shell docker-compose up
```
5. Starting the project as a Spring Boot application
    - You can run the application with Maven:
    ```
    mvn spring-boot:run
    ```
    - or you can use IDE:
    run the ```main``` method in the ```UwithmeServiceApp``` class
