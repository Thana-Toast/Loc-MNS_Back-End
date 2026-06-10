FROM eclipse-temurin

WORKDIR /app

COPY target/app.jar app.jar

COPY .env .env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]