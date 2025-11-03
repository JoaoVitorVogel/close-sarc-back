# syntax=docker/dockerfile:1
# Etapa 1: build com Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

# Etapa 2: runtime leve
FROM eclipse-temurin:21-jre-alpine
ENV TZ=UTC \
    JAVA_OPTS=""
# Usuário não-root
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
# Copia o jar gerado (qualquer name) para app.jar
COPY --from=build /workspace/target/*.jar /app/app.jar
USER app
EXPOSE 8080
# Usamos sh -c para expandir variáveis
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-docker} -jar /app/app.jar"]
