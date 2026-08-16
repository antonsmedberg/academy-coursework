# Min Loggparser i Python

Hej!  
Jag har byggt ett Python-program som läser och förstår loggar – alltså sånt som datorer och program ofta skriver ut för att visa vad som händer i bakgrunden. Tanken med det här projektet är att göra det lätt att förstå loggar, även om man inte är expert på programmering.

Jag skrev det här både för att lära mig själv, och för att andra (kanske du!) ska kunna följa med och förstå hur man jobbar med loggfiler i Python.

---

## Vad gör programmet?

Här är vad min loggparser kan göra:

- Läser **loggrader som text** (sånt som `[2025-03-23 12:35:59] INFO Modul Något händer`)
- Läser **loggar som JSON** (t.ex. `{ "date": "...", "level": "...", ... }`)
- Försöker **förstå datumen** som finns i loggarna
- **Slår ihop rader som hör ihop** (ibland är en logg flera rader lång)
- **Sorterar loggarna i tidsordning**
- **Grupperar loggarna** i små block – till exempel 5 minuter åt gången
- Skriver ut resultatet så man lätt kan läsa det

---

## Hur funkar koden?

Jag har delat upp koden i flera delar för att det ska bli lättare att förstå:

### 1. Regler för hur en loggrad ska se ut

Jag använder något som heter **regex** (ett slags mall) för att känna igen loggar som ser ut så här:

```
[2025-03-23 12:35:59] INFO Modul Något hände
```

Om ditt loggformat ser annorlunda ut kan du ändra på regexen i början av koden.

---

### 2. Tolka loggrader

Jag har skrivit några funktioner som försöker förstå varje rad:

- **parse_textlog_line:** Tolkar vanliga loggrader i text
- **parse_json_line:** Försöker läsa loggar som är skrivna som JSON
- **try_parse_datetime:** Kollar om datumen i loggen ser ut som jag känner igen (olika vanliga format)

Om en rad inte passar in i någon av mallarna, behandlar jag den som "fortsättning" på föregående loggrad (multiline).

---

### 3. Huvudmotorn – loggparsern

Jag har en funktion som heter `parse_log_pipeline`. Den testar varje rad med flera olika tolkningsfunktioner. När något matchar, skapar den ett loggobjekt.

Om inget matchar försöker den lägga raden som en fortsättning på den förra.

---

### 4. Sortering och uppdelning

Jag har en funktion som:

- **sorterar** loggarna efter tid (`sort_log_entries_by_datetime`)
- **grupperar** loggarna i små block, t.ex. alla loggar som hör till samma 5-minutersperiod (`chunk_log_entries`)

---

### 5. Normalisering – förberedelse innan tolkning

För att göra det enkelt att mata in data, finns funktionen `normalize_raw_log_data`. Den ser till att allt som skickas in först blir en lista med strängar, oavsett om det var en textsträng, en lista, eller en blandning.

---

### 6. Exempel i `main()`

I slutet av filen finns en `main()`-funktion. Där visar jag exempel på hur man:

- Skickar in loggdata
- Kör parsern
- Sorterar och grupperar loggar
- Skriver ut resultatet i JSON-format

Jag har tre exempel:

- En logg som är som en lång text
- En logg som är en blandad lista med text och JSON
- En logg med ostrukturerad text

---

## Hur du testar

Du kan testa programmet genom att köra det i terminalen så här:

```bash
python logparser.py
```

Resultatet kommer att skrivas ut i terminalen i form av lättläst JSON.

---

## Vill du bygga vidare?

Du kan enkelt utöka parsern:

- Lägg till fler typer av loggformat
- Lägg till fler datumformat i `try_parse_datetime`
- Skapa ett gränssnitt (kanske i ett fönster eller på webben)
- Lägg till färger eller filter för olika loggnivåer (INFO, ERROR, osv)

---

## Varför jag byggde det här

Jag gjorde det här projektet för att det är kul att förstå hur datorer loggar vad som händer – och för att träna på att skriva kod som är tydlig och lätt att jobba vidare med. Jag har försökt kommentera koden ordentligt, och skriva den så att den ska gå att förstå även om du inte är så van vid Python.

---

Tack för att du kikar på mitt projekt!  
/Anton