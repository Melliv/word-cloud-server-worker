FROM openjdk:21-jdk
WORKDIR /app
COPY build/libs/*.jar /app/word-cloud-server-worker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "word-cloud-server-worker.jar"]