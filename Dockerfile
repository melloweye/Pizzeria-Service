FROM openjdk:17-jdk

ARG JAR_FILE=target/FinalProject-1.0-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT [ "java", "-jar", "/app.jar" ]

EXPOSE 8080