# Primeira etapa: compilação
FROM ubuntu:latest AS build

# Atualize as listas de pacotes
RUN apt-get update

# Configure o diretório de trabalho
WORKDIR /app

# Copie o código-fonte para o contêiner
COPY . .

# Instale o Maven
RUN apt-get install maven -y

# Compile o código usando o Maven
RUN mvn clean install

# Segunda etapa: imagem final
FROM openjdk:17-slim

# Copie o arquivo JAR compilado da etapa anterior
COPY --from=build /app/target/todolist-1.0.0.jar app.jar

# Exponha a porta do aplicativo
EXPOSE 8080

# Comando para executar o aplicativo
CMD ["java", "-jar", "app.jar"]
