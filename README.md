# Spideo Java Challenge - Auction House

A java challenge sent by Spideo's Team which is about creating an REST API that manages an auction house.<br>The main functionnalities of this application are :
  
* Create/List/Delete an(all) auction(s) house.
* Create/List/Delete an(all) auction(s) of a specific auction house.
* Bid in an auction and display the winner of an auction.

Check the [Spideo Java Challenge.pdf](docs/Spideo_Java_Challenge.pdf) for more detailed information.

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

> NOTE: I could not test this project on my laptop because it's Windows Home x64 even I enabled the VT-X/AMD-v with [Docker Toolbox](https://docs.docker.com/toolbox/toolbox_install_windows/)

##### Download Images
This project uses the OpenJDK docker image from [openjdk:10](https://hub.docker.com/_/openjdk)

> NOTE: For Unix based OS, `docker` command need `sudo` access.

##### Other Software

The software used to create this application. Feel free to download them if you do not have them

* Install [git](https://git-scm.com/).
* Install Docker Cloud CLI following the [instructions](https://docs.docker.com/docker-cloud/installing-cli/).
* Download [Maven](https://maven.apache.org/download.cgi) and install it.
* Download your Java IDE (IntelliJ, Eclipse, Visual Studio, NetBeans ...).
* Download the JDK [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
(if you are not using Docker, and for Unix Based OS you should download OpenJDK from the OS repository)
* After installing JDK, you should set the path variable in your OS's environment.
* Download [Lombok](https://projectlombok.org/download) or install it from the plugins in IntelliJ.
> NOTE: We are using Spring Boot, so you do not have to install any server (Tomcat, GlassFich, Weblogic, JBoss...) it is already in embedded in Spring Boot.

### Installing & Running

After installing all software that you need for this project and setting up paths for Java and Maven in your OS's environment

#### With Maven 
You should run the following command to install the libraries needed. For that you should run this in your shell/Powershell
```
mvnw install / mvnw clean install
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
And finally, you can run the image and expose the port that you need instead of **xxxx** 
```
docker run -p xxxx:8080 test
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

## Built With

* [Spring-Boot](https://spring.io/projects/spring-boot) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Lombok](https://projectlombok.org/) - Library that makes the code cleaner with less efforts
* [With Love](https://github.com/Amraneze) - With love from Paris

## License

This project is licensed under the GNU GPLv3 - see the [LICENSE.md](LICENSE.md) file for details