FROM openjdk:11-jdk-slim

COPY build/libs/alfabank-0.0.1-SNAPSHOT.jar /alfabank.jar

CMD ["java", "-jar", "/alfabank.jar"]
