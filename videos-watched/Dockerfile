FROM openjdk:8-jdk-alpine
COPY ./build/libs/videos-watched-1.0.0.jar videos-watched.jar
COPY ./videos-watched.properties /videos-watched.properties
ENTRYPOINT ["java", "-jar", "videos-watched.jar", "--spring.config.location=/videos-watched.properties"]