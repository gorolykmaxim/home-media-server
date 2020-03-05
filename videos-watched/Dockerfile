FROM openjdk:8-jdk-alpine
ARG BUILD_VERSION
COPY videos-watched-${BUILD_VERSION}.war videos-watched.war
COPY videos-watched.properties /videos-watched.properties
ENTRYPOINT ["java", "-jar", "videos-watched.war", "--spring.config.location=/videos-watched.properties"]