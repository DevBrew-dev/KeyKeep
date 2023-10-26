FROM openjdk:20-jdk-alpine

WORKDIR /app

COPY ./backend.jar /app/backend.jar

EXPOSE 8420

CMD ["java", "-jar", "/app/backend.jar"]
