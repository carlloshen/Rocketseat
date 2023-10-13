FROM ubuntu:latest AS build

RUN apt update
RUN apt install openjdk-17-jdk -y

FROM openjdk:17-alpine

COPY . .

RUN apt install maven -y 

RUN mvn clean install

EXPOSE 8080

COPY --from=build /target/todolist-1.0.0.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]
