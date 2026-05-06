FROM eclipse-temurin:21-jre

WORKDIR /app

ARG JAR_FILE
ARG SERVICE_PORT=8080

COPY ${JAR_FILE} app.jar

EXPOSE ${SERVICE_PORT}

ENTRYPOINT ["java","-jar","app.jar"]