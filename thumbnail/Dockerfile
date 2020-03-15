FROM elderbyte/docker-alpine-jdk8-ffmpeg
ARG BUILD_VERSION
COPY thumbnail-${BUILD_VERSION}.war thumbnail.war
COPY thumbnail.properties /thumbnail.properties
ENTRYPOINT ["java", "-jar", "thumbnail.war", "--spring.config.location=/thumbnail.properties"]