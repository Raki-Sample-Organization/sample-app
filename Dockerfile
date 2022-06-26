# syntax=docker/dockerfile:1
FROM gradle:7.4-jdk17-alpine AS build-env
COPY ./build/generated-sources /app/build/generated-sources
COPY ./src /app/src
COPY build.gradle settings.gradle /app/
WORKDIR /app
RUN gradle build -x generateJooq -x test

FROM gcr.io/distroless/java17-debian11
COPY --from=build-env /app/build/libs/sample-app-0.1.0.jar /app/app.jar
WORKDIR /app
CMD ["app.jar"]
