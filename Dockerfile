FROM openjdk:17.0.2

WORKDIR /app

COPY ./target/GestionProductos-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "GestionProductos-0.0.1-SNAPSHOT.jar"]