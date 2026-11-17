FROM eclipse-temurin:26-jdk-noble AS build

WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./

COPY src ./src

RUN chmod +x mnvw && ./mnvw -B -DskipTests package

FROM eclipse-temurin:26-jre-noble

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

USER 10001:10001

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]