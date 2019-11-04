# Spideo Java Challenge - Auction House

[![CircleCI](https://circleci.com/gh/Amraneze/spideo-challenge/tree/master.svg?style=svg)](https://circleci.com/gh/Amraneze/spideo-challenge/tree/master)

A java challenge sent by Spideo's Team which is about creating an REST API that manages an auction house.<br>The main functionnalities of this application are :
  
* Create/List/Delete an(all) auction(s) house.
* Create/List/Delete an(all) auction(s) of a specific auction house.
* Bid in an auction and display the winner of an auction.

## Getting Started

### Prerequisites

This section describes the requirements to run the application

##### Setup Environments

* Memory: At least 2GB
* Operating System: Linux kernel version 3.10 or higher, Windows x Pro/Ent x64.
* Disk Space: 2GB of available disk space.

> NOTE: You may find all the details in the following page [Docker System requirements](https://docs.docker.com/datacenter/ucp/1.1/installation/system-requirements/)

##### Install Docker

It's preferred to use Docker to test this solution, otherwise you can just install JRE and run the project with command lines

This workshop is tested with Docker Community Edition `19.03.4, build 9013bf583a` on Ubuntu 18.04.3 LTS (GNU/Linux 4.15.0-1051-aws x86_64) in AWS.

##### Download Images
This project uses the OpenJDK docker image from [openjdk:10](https://hub.docker.com/_/openjdk)

##### Other Software

The software used to create this application. Feel free to download them if you do not have them

* Install [Docker](https://docs.docker.com/install/)
(Optional) Install Docker Cloud CLI following the [instructions](https://docs.docker.com/docker-cloud/installing-cli/).
* Install [Docker Compose](https://docs.docker.com/compose/) if you want to use docker compose.
* Download [Maven](https://maven.apache.org/download.cgi) and install it.
* Download your Java IDE (IntelliJ, Eclipse, Visual Studio, NetBeans ...).
* Download the JDK [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
(if you are not using Docker, and for Unix Based OS you should download OpenJDK from the OS repository)
* After installing JDK, you should set the path variable in your OS's environment.
* Download [Lombok](https://projectlombok.org/download) or install it from the plugins in IntelliJ if you want to work with the project's code.
> NOTE: We are using Spring Boot, so you do not have to install any server (Tomcat, GlassFich, Weblogic, JBoss...) it is already in embedded in Spring Boot.

### Installing & Running

After installing all software that you need for this project and setting up paths for Java and Maven in your OS's environment

#### With Maven 
You should run the following command to install the libraries needed. For that you should run this in your shell/Powershell
```
mvnw clean install
```
After this, you can run the application with this command
```
mvnw spring-boot:run
```
Or you can build the jar with this command
```
mvnw package 
```
And then 
```
java -jar target/test-0.0.1-SNAPSHOT.jar
```
>NOTE: You should be in the root folder of the project where you can see the folders **src, docs and other files**

#### With Docker

You should build the jar before starting the build the docker image, for that you should use this command
```
mvnw package
```
After checking if the jar is built in the target folder, you may use this following command
```
docker build -t test:latest .
```
Then, you should make sure the image is created using
```
docker images
```
##### With Docker
And finally, you can run the image and expose the port that you need instead of **xxxx** 
```
docker run -p xxxx:8080 test
```

##### With Docker Compose
And finally, you can use docker-compose that it will run the image and also check its health
```
docker-compose up
```

>NOTE: The port exposed in Docker image is 8080, which can be changed in Dockerfile, if you need to. Otherwise, you can just change the port that you want in **xxxx** without modifying the image

**You can also test using my Docker image that is running on an [AWS instance](http://13.48.136.181:8080/api/)**

### Running the tests

For running the tests, you should run this maven command
* Using a Unix based OS: 
```
mvn test
```
* Using a Windows OS:
```
mvnw test
```

## Endpoints

>NOTE: Content-Type: ``application/json`` header must be present in each request to use the API.

### Swagger

For visualizing the detailed documentation about the API, please refer to the [Project's Swagger Page](http://13.48.136.181:8080/api/swagger-ui.html#/auction-house-controller)

## Built With

* [Spring-Boot](https://spring.io/projects/spring-boot) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Lombok](https://projectlombok.org/) - Library that makes the code cleaner with less efforts
* [Swagger](https://swagger.io/tools/swagger-ui/) - Library to visual the API's endpoints
* [With Love](https://github.com/Amraneze) - With love from Paris

## License

This project is licensed under the GNU GPLv3 - see the [LICENSE.md](LICENSE.md) file for details