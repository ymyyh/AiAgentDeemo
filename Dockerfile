FROM eclipse-temurin:21-jre
WORKDIR /app

# 接收传入的 JAR 文件路径
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# 启动 SpringBoot 可执行 JAR
ENTRYPOINT ["java","-jar","app.jar"]