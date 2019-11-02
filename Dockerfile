FROM openjdk:10
EXPOSE 8080
ADD target/*.jar spideo.jar
ENTRYPOINT [ "sh", "-c", "java -Xmx512m -jar /spideo.jar" ]