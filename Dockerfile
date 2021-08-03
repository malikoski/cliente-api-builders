FROM maven:3.8.1-openjdk-16-slim AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /build/src
RUN mvn package

FROM openjdk:16.0.2-jdk-slim

VOLUME /tmp

COPY --from=build "/build/target/cliente-api*.jar" app.jar
CMD [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]