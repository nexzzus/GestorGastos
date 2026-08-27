# ==========================================
# Etapa 1: Construcción (Build)
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el archivo de configuración de dependencias
COPY pom.xml .
# Descargamos las dependencias para aprovechar la caché de Docker
RUN mvn dependency:go-offline

# Copiamos el código fuente
COPY src ./src
# Compilamos el proyecto omitiendo los tests para agilizar el despliegue
RUN mvn clean package -DskipTests

# ==========================================
# Etapa 2: Ejecución (Run)
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el .jar generado en la primera etapa y lo nombramos app.jar
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto 8080 que usa Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]