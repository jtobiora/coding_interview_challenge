FROM bellsoft/liberica-openjdk-alpine-musl:17
ADD target/transaction-statistics-api.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]