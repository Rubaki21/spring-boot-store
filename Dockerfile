FROM bellsoft/liberica-openjdk-alpine:21
LABEL org.opencontainers.image.authors="nikolay medvedev"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java","-jar","/application.jar"]