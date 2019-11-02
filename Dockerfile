FROM openjdk:10
EXPOSE 8080
ADD target/*.jar spideo.jar
ENV JAVA_OPTS = ""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /spideo.jar" ]