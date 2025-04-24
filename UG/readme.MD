# Zadanie rekrutacyjne na stanowisko Młodszego Programisty
**Uniwersytet Gdański – Iwo Stanisławski**

---

## Informacje

### Opis funkcjonalności aplikacji

#### Endpointy

W aplikacji dostępne są dwa endpointy:

##### 1. Zapisywanie komputerów

Endpoint służy do zapisywania informacji o komputerach. Wymaga podania:

- nazwy komputera (`String`)
- ceny komputera (`BigDecimal`)

Dane są zapisywane w bazie danych.

**Zachowanie:**

- W przypadku poprawnych danych – zwracany jest status `201 Created` wraz z informacją o zapisanym komputerze.
- W przypadku błędnych danych – zwracany jest status `400 Bad Request` wraz z informacją o polach zawierających błędne wartości.
- W przypadku braku wartości opcjonalnych (np. daty zaksięgowania lub przewalutowania) – zostaną przypisane domyślne wartości, tj. aktualna data.

---

##### 2. Wyszukiwanie komputerów

Endpoint umożliwia wyszukiwanie komputerów według nazwy oraz przedziału dat.

**Zachowanie:**

- Jeśli nie zostaną podane żadne parametry – zwrócone zostaną wszystkie komputery.
- Jeśli podana zostanie tylko data "od", data "do" zostanie ustawiona na `LocalDate.of(1, 1, 1)`.
- Jeśli podana zostanie tylko data "do", data "od" zostanie ustawiona na `LocalDate.of(2200,1,1)`.
- Jeśli podana zostanie tylko nazwa komputera – zostaną wyszukane wszystkie komputery o tej nazwie.
- Jeśli podana zostanie tylko data – zostaną wyszukane wszystkie komputery z tego zakresu, niezależnie od nazwy.

---

### Obsługa wyjątków

Aplikacja posiada jeden generyczny mechanizm obsługi wyjątków, bez podziału na domeny.

Każdy wyjątek zawiera:
- komunikat (message)
- kod HTTP

W ciele odpowiedzi (response body) znajduje się szczegółowa informacja zależna od typu błędu.

---

### Informacje o API NBP

- Aplikacja wykorzystuje dane z tabeli C udostępnianej przez NBP.
- Dokładny URL do pobierania danych znajduje się w NbpApiService.

---

### Logi

- Logi zapisywane są w bazie danych.
- Dodatkowo, logi konsolowe z danego uruchomienia aplikacji zapisywane są w katalogu `logs`, w pliku zawierającym datę i godzinę zamknięcia aplikacji.

---

### Plik XML

- Plik XML z danymi jest zapisywany w domyślnym katalogu aplikacji `resources/computers_export.xml`.

---

### Testy

- W projekcie zaimplementowano przykładowe testy jednostkowe.

---

### Uwagi
- Ze względu na brak kursów walut w dniach piątek–niedziela, kurs jest pobierany z ostatniego dostępnego dnia wstecz (maksymalnie 7 dni w przeszłość).
- Ze względu na niespójność w zadaniu rekrutacyjnym, z powodu braku danych na dni piątek- niedziela zostało rozszerzone pole `data zaksięgowania` na `data zaksięgowania` oraz `data przeliczenia kursu`
---

### Wymagania

- **Java**: 21 LTS (dystrybucja Temurin)
- **Docker**: 28.0.4
- **Docker Compose**: 2.34

---

### Uruchamianie aplikacji

#### 1. Budowanie JAR-A

```bash
  ./mvnw clean package -DskipTests
  java -jar ./target/UG-0.0.1-SNAPSHOT.jar
```

Spring Boot automatycznie pobierze obraz PostgreSQL, utworzy i uruchomi kontener.

#### 2. Uruchamianie w Dockerze

```bash
  docker compose -f /home/iwo/IdeaProjects/UG/docker/docker-compose.yaml -f /home/iwo/IdeaProjects/UG/docker/spring-boot-compose.yaml -p docker up -d
```

Docker automatycznie pobierze obraz PostgreSQL oraz stworzy obraz dla aplikacji Spring Boot i utworzy kontener z JAR i PostgreSQL oraz uruchomi kontenery.

Dostępne są dwa profile uruchomieniowe:

    main – podstawowy profil produkcyjny, 

    recruitment – inicjalizuje dane zgodne z treścią zadania rekrutacyjnego, z create-drop na bazie danych.

---
