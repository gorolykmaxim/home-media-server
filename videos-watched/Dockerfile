FROM openjdk:8-jdk-alpine
COPY ./build/libs/videos-watched-1.0.0.war videos-watched.war
COPY ./videos-watched.properties /videos-watched.properties
ENTRYPOINT ["java", "-jar", "videos-watched.war", "--spring.config.location=/videos-watched.properties"]