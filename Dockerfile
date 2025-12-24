# Etapa 1: Build
FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Descargar dependencias (se cachea si no cambia pom.xml)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN ./mvnw clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar el JAR compilado desde la etapa de build
COPY --from=build /app/target/GestionProductos-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto
EXPOSE 8080

# Configurar Java options para contenedor
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]