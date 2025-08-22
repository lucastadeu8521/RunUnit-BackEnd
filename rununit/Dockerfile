FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY /build/libs/*.jar rununit.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "rununit.jar"]
