FROM openjdk:8-jdk-alpine
ARG BUILD_VERSION
COPY torrent-ui-${BUILD_VERSION}.war torrent-ui.war
COPY torrent-ui.properties /torrent-ui.properties
ENTRYPOINT ["java", "-jar", "torrent-ui.war", "--spring.config.location=/torrent-ui.properties"]