FROM eclipse-temurin:17-jdk-jammy
RUN apt-get update && apt-get install -y iputils-ping
COPY target/sales-client.jar sales-client.jar
ENTRYPOINT ["java", "-jar", "sales-client.jar"]