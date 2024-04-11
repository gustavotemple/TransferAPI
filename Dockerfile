FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /usr/src/app

COPY . .

RUN mvn dependency:go-offline && mvn package -DskipTests

FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /usr/src/app/target/api-transferencia-0.0.1-SNAPSHOT.jar /app/api-transferencia.jar

EXPOSE 8080

CMD ["java", "-jar", "api-transferencia.jar"]

# docker rmi --force api-transfer
# docker build -t api-transfer .
# docker run --network="host" api-transfer
