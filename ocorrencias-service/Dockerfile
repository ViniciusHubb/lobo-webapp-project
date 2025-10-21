# Imagem base apenas com JRE (Java Runtime)
FROM eclipse-temurin:21-jre AS runtime

# Define diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR já compilado do host para dentro do container
COPY target/ocorrencias-service-*.jar app.jar

# Expõe a porta configurada no application.properties
EXPOSE 8082

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
