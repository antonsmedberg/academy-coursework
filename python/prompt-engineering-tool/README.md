
# Prompt Generator Projekt

Detta projekt innehåller en uppsättning Python-klasser och skript för att generera och formatera uppmaningar baserat på specifika parametrar. Projektet består av fyra huvudfiler:

1. `formatting_rules.py`: Definierar olika Markdown-formatteringsregler.
2. `task_categories.py`: Representerar olika kategorier av uppgifter.
3. `prompt_generator.py`: Genererar formaterade uppmaningar baserat på givna parametrar.
4. `main.py`: Körbar fil som demonstrerar användningen av `PromptGenerator`.

## Projektstruktur

### `formatting_rules.py`

Denna fil innehåller klassen `FormattingRules` som definierar olika Markdown-formatteringsregler som strängkonstanter. Den innehåller även en klassmetod `get_rules` som returnerar alla regler som en ordbok.

### `task_categories.py`

Denna fil innehåller klassen `TaskCategory` som är en enum som representerar olika kategorier av uppgifter. Den innehåller en klassmetod `get_categories` som returnerar alla kategorier som en ordbok.

### `prompt_generator.py`

Denna fil innehåller klassen `PromptGenerator` som använder `FormattingRules` och `TaskCategory` för att generera formaterade uppmaningar. Den har följande metoder:
- `__init__`: Initierar formateringsregler och uppgiftskategorier.
- `generate_prompt`: Genererar en formaterad uppmaning baserad på de givna parametrarna.
- `display_formatting_rules`: Visar Markdown-formatteringsreglerna.
- `create_developer_prompt`: En omslagsmetod för `generate_prompt` som skapar en utvecklarspecifik uppmaning.

### `main.py`

Denna fil demonstrerar användningen av `PromptGenerator` genom att skapa en exempeluppmaning och visa formateringsreglerna.

## Användning

För att använda detta projekt, följ dessa steg:

1. Klona repot:
   ```bash
   git clone https://github.com/ditt-användarnamn/prompt-generator-projekt.git
   cd prompt-generator-projekt
   ```

2. Kör `main.py` för att se exempelutmatning:
   ```bash
   python main.py
   ```

### Exempel på användning

Så här ser ett exempel på utmatning från `main.py` ut:

```markdown
# Role: Backend Developer
Role Prompting

## Task: Implement API Endpoints
Chain of Thought Prompting

### Specifics
1. Use Django REST Framework.
2. Ensure all endpoints are properly documented.
3. Implement JWT authentication.

---

### Context
The project is a web application that requires secure communication between the client and server. 
All endpoints must follow RESTful principles.

---

### Examples
- Example of a GET endpoint:
```python
@api_view(['GET'])
def example_view(request):
    return Response({"message": "Hello, world!"})
```

***

### Notes
Make sure to handle errors properly and return appropriate status codes.
```

## Framtida Planer

För att förbättra projektet planerar jag följande:

1. **Enhetstester**: Implementera omfattande enhetstester för alla klasser och metoder för att säkerställa korrekt funktionalitet och underlätta framtida underhåll.
2. **Logger**: Lägg till loggning för att spåra och felsöka körningen av programmet.
3. **Konfigurationsfil**: Använd en konfigurationsfil för att hantera olika inställningar och parametrar.
4. **Felhantering**: Förbättra felhanteringen genom att fånga och hantera undantag på ett bättre sätt.
5. **Dokumentation**: Skapa detaljerad dokumentation för att underlätta för andra utvecklare att förstå och använda din kod.
6. **Refaktorisering**: Refaktorisera koden för att förbättra läsbarhet och underhållbarhet.

## Bidra

Om du vill bidra till projektet, vänligen gör en fork av repot och skapa en pull request med dina ändringar. Vi uppskattar alla bidrag!

## Licens

Detta projekt är licensierat under MIT-licensen.
