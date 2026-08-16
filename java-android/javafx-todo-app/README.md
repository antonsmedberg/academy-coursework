# To-Do List Management System

## Introduktion
Denna applikation är ett To-Do List Management System som består av en serverdel som hanterar API-anrop och en frontend-applikation byggd med JavaFX. Syftet är att tillhandahålla en enkel lösning för att hantera uppgifter utan att använda en databas. Applikationen uppfyller kraven för avancerad Java-programmering och inkluderar koncept som gränssnitt, abstrakta klasser och polymorfism.

---

## Projektmål och funktionalitet
**Mål:**
- Implementera ett komplett system för att hantera en att-göra-lista (To-Do List).
- Demonstrera avancerade objektorienterade principer och Java-programmering.

**Funktionalitet:**
- Skapa, läsa, uppdatera och ta bort uppgifter (CRUD).
- Frontend som kommunicerar med backend via API.
- Uppgifter lagras i minnet under serverns livslängd.

---

## Instruktioner för att starta projektet

### Förutsättningar
- Java 18 eller senare installerat.
- IntelliJ IDEA (eller annan IDE som stödjer JavaFX).
- Maven för beroendehantering.

### Steg för att köra projektet

1. **Kloning av projektet:**
   ```bash
   git clone <projektets-repo-URL>
   cd <projektets-mapp>
   ```

2. **Bygg och kör servern:**
    - Navigera till projektets huvudmapp.
    - Kör följande kommando i terminalen:
      ```bash
      mvn compile exec:java -Dexec.mainClass="com.example.todo_antsm.TaskServer"
      ```
    - Servern startar på port **8080**.

3. **Kör frontend-applikationen:**
    - Öppna projektet i din IDE.
    - Navigera till klassen `TaskApp`.
    - Kör applikationen från IDE.

---

## API-specifikation

| Endpoint         | Metod   | Beskrivning               |
|------------------|---------|---------------------------|
| `/tasks`         | GET     | Hämta alla uppgifter.     |
| `/tasks`         | POST    | Skapa en ny uppgift.      |
| `/tasks/{id}`    | PUT     | Uppdatera en uppgift.     |
| `/tasks/{id}`    | DELETE  | Ta bort en specifik uppgift. |

### Exempel på API-anrop

#### GET /tasks
```bash
curl -X GET http://localhost:8080/tasks
```
**Svar:**
```json
[
  {
    "id": 1,
    "title": "Läsa bok",
    "description": "Läsa Java-bok",
    "deadline": "2024-12-25"
  }
]
```

#### POST /tasks
```bash
curl -X POST http://localhost:8080/tasks -d "Läsa bok,Läsa Java-bok,2024-12-25"
```

#### PUT /tasks/{id}
```bash
curl -X PUT http://localhost:8080/tasks -d "1,Gå på promenad,Morgonpromenad kl 7,2024-12-26"
```

#### DELETE /tasks/{id}
```bash
curl -X DELETE http://localhost:8080/tasks?id=1
```

---

## Designval

### UML-diagram
- **Struktur:**
    - `Task` (Abstrakt klass)
    - `ToDoTask` och `DeadlineTask` (konkreta klasser)
    - `TaskService` (gränssnitt)
    - `TaskServiceImpl` (implementering av gränssnittet)
    - `TaskController` (JavaFX-klient för att interagera med API).

### Val av mönster
- **Gränssnitt och abstrakta klasser:** Används för att standardisera CRUD-operationer och strukturera kod.
- **Polymorfism:** Implementerad för hantering av olika typer av uppgifter (ToDoTask och DeadlineTask).

---

## Reflektion

### Designval och tekniker
- **Val av teknik:**
    - Enkel server byggd med `HttpServer` för att minska beroenden.
    - Frontend byggd med JavaFX för att skapa en interaktiv och responsiv användarupplevelse.

- **Varför dessa val?**
    - Lätt att implementera och förstå.
    - Fokuserar på objektorienterad design och avancerade Java-koncept.

### Förbättringsförslag
- **Lägg till uthållighet:** Implementera en databas för att lagra uppgifter mellan serverstartar.
- **Autentisering:** Lägg till användarauthentisering för att hantera flera användare.
- **Utökad API-funktionalitet:** Lägg till filter och sorteringsfunktioner.

---

## Slutsats
Projektet visade hur man kan använda objektorienterad programmering för att utveckla ett funktionellt system. Genom att implementera gränssnitt, abstrakta klasser och polymorfism kunde en flexibel och skalbar lösning byggas.

Under projektets gång har förståelsen för API-utveckling, JavaFX och avancerade Java-koncept förbättrats avsevärt.

---

## Struktur och filer
- **`TaskController.java:`** Hanterar frontend-logik och kommunikation med API.
- **`TaskServer.java:`** Hanterar API-endpoints och CRUD-operationer.
- **`TaskService.java:`** Gränssnitt för CRUD-operationer.
- **`TaskServiceImpl.java:`** Implementation av `TaskService`.
- **`ToDoTask.java` och `DeadlineTask.java:`** Olika typer av uppgifter.
- **`README.md:`** Dokumentation av projektet.
