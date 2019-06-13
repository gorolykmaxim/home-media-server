FROM openjdk:8-jdk-alpine
COPY ./build/libs/thumbnail-search-1.0.0.jar thumbnail-search.jar
COPY ./thumbnail-search.properties /thumbnail-search.properties
ENTRYPOINT ["java", "-jar", "thumbnail-search.jar", "--spring.config.location=/thumbnail-search.properties"]