FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} analyser.jar
ENTRYPOINT ["java","-Ddebug=true","-Djava.security.egd=file:/dev/./urandom","-jar","/analyser.jar"]