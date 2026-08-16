# 🚗 Virtuell Biluthyrning

En konsolbaserad Java-applikation där du kan hyra fordon, returnera dem och beräkna hyreskostnader.

---

## 📘 Projektbeskrivning

Detta projekt visar hur man använder viktiga objektorienterade programmeringskoncept:
- **📦 Arv (Inheritance):** Gemensamma attribut och metoder hanteras i en basklass (`Vehicle`).
- **🔐 Inkapsling (Encapsulation):** Privata attribut skyddas och nås via getters.
- **🧩 Abstraktion (Abstraction):** Abstrakta klasser används för att hantera gemensam logik.
- **🔗 Interface:** Gränssnitt används för att definiera uthyrningsfunktioner.

---

## ✨ Funktioner

- **🔍 Visa tillgängliga fordon**: Se en lista över alla tillgängliga fordon och deras status (uthyrd eller ledig).
- **📥 Hyra fordon**: Uppdatera statusen på ett fordon till uthyrd.
- **📤 Returnera fordon**: Återställ statusen för ett fordon till ledig.
- **💵 Beräkna hyreskostnader**: Ange antal hyresdagar och få total kostnad.

---

## 📂 Projektstruktur

```plaintext
src/
├── interfaces/
│   ├── Rentable.java        # Definierar uthyrningsfunktioner
│
├── vehicles/
│   ├── Vehicle.java         # Abstrakt basklass för alla fordon
│   ├── Car.java             # Klass för bilar
│   ├── SUV.java             # Klass för SUV
│   ├── Truck.java           # Klass för lastbilar
│   ├── Convertible.java     # Klass för cabrioleter
│
├── Main.java                # Huvudklass som hanterar användargränssnittet
```

---

## 🎓 Vad jag lärde mig

- **🧱 Arv:** Klasser som `Car` och `SUV` ärver gemensamma egenskaper från `Vehicle`.
- **🔒 Inkapsling:** Privata attribut som `model` och `registrationNumber` skyddas via getters.
- **🎨 Abstraktion:** Basklassen `Vehicle` innehåller gemensamma metoder och egenskaper.
- **🔗 Interface:** `Rentable` används för uthyrningsfunktioner som `rentOut` och `returnVehicle`.
- **🤖 Felhantering:** Effektiv användning av `try-catch` för att hantera ogiltig användarinmatning.

---

## 🛠️ Funktioner i användargränssnittet

När applikationen körs visas följande meny i terminalen:

```plaintext
--- Virtuell Biluthyrning ---
1. Visa alla fordon
2. Hyra ett fordon
3. Returnera ett fordon
4. Beräkna hyreskostnad
5. Avsluta
```

### Menyval:

1. **🔍 Visa alla fordon**: Visar en lista över alla fordon, deras typ och status.
2. **📥 Hyra ett fordon**: Ange fordonets nummer för att hyra det.
3. **📤 Returnera ett fordon**: Ange fordonets nummer för att markera det som återlämnat.
4. **💰 Beräkna hyreskostnad**: Ange fordonets nummer och antal hyresdagar för att se den totala kostnaden.
5. **❌ Avsluta**: Stänger applikationen.

