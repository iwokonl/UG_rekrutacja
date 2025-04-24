# Użycie obrazu JDK 21 jako bazowego
FROM eclipse-temurin:21-jdk

# Instalacja Mavena
RUN apt-get update && apt-get install -y maven

# Ustawienie katalogu roboczego
WORKDIR /app

# Kopiowanie pliku pom.xml
COPY pom.xml .

# Pobranie zależności Maven (cache)
RUN mvn dependency:go-offline -B

# Kopiowanie reszty projektu
COPY src ./src

# Budowanie aplikacji
RUN mvn clean package -DskipTests

# Uruchomienie aplikacji
CMD ["java", "-jar", "target/UG-0.0.1-SNAPSHOT.jar"]