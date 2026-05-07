FROM eclipse-temurin:21-jre
WORKDIR /app

# 接收外部传入的 JAR 路径
ARG JAR_PATH
COPY ${JAR_PATH} app.jar

ENTRYPOINT ["java","-jar","app.jar"]