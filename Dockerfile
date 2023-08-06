FROM openjdk:17-oracle

ARG JAR_FILE=./build/libs/backend-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]
