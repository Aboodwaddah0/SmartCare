FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/chatbot-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8094
ENTRYPOINT ["java", "-jar", "app.jar"]
