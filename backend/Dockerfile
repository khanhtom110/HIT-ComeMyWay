# Bước 1: Dùng ảnh chứa Maven và Java 21 để build code
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build ra file .jar và bỏ qua test
RUN mvn clean package -DskipTests -Dformatter.skip=true

# Bước 2: Dùng ảnh chỉ chứa Java JRE 21 cho nhẹ server
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy file .jar từ bước 1 sang
COPY --from=build /app/target/comemyway-0.0.1-SNAPSHOT.jar app.jar
# Mở cổng 8080
EXPOSE 8080
# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]