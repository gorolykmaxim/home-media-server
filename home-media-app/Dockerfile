FROM openjdk:8-jdk-alpine
COPY ./build/libs/home-media-app-2.2.1.war home-media-app.war
COPY ./home-media-app.properties /home-media-app.properties
ENTRYPOINT ["java", "-jar", "home-media-app.war", "--spring.config.location=/home-media-app.properties"]