FROM openjdk:17-alpine
ADD target/transaction-statistics-api.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]