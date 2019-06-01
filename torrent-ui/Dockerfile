FROM openjdk:8-jdk-alpine
COPY ./build/libs/torrent-ui-1.0.0.war torrent-ui.war
COPY ./torrent-ui.properties /torrent-ui.properties
ENTRYPOINT ["java", "-jar", "torrent-ui.war", "--spring.config.location=/torrent-ui.properties"]