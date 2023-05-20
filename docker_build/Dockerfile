# syntax=docker/dockerfile:1

FROM eclipse-temurin:19.0.2_7-jre-alpine

WORKDIR /app

COPY ./SpringGoSpy-0.0.3-SNAPSHOT.jar .
COPY ./gospy_spring_keystore.pkcs12 .
COPY ./gospy_spring_truststore .

CMD ["java", "-jar", "SpringGoSpy-0.0.3-SNAPSHOT.jar"]

EXPOSE 8080 8443

