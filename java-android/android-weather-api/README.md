# 🌦️ WeatherApp – Open-Meteo Integration

En väderapp byggd helt i **Kotlin** med fokus på ren arkitektur, modern Android-teknik och en användarvänlig design.  
Appen använder **Open-Meteo API** för geokodning (stadssök) och prognoser (nuvarande, timvis och daglig data).  

---

## ✨ Funktioner
- Sök städer och visa aktuell temperatur + 5-dagarsprognos  
- Spara favoriter i en lokal databas (**Room**)  
- Växla mellan **°C och °F**  
- Få **regnvarningar som notiser** via WorkManager  
- Anpassade layouter för **portrait och landscape**  
- Modern **Material 3-design**  

---

## 🔌 API:er
**Geokodning (stadssök):**  
```
https://geocoding-api.open-meteo.com/v1/search?name={q}&count=10&language={sv|en}&format=json
```

**Prognos (väderdata):**  
```
https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&current=temperature_2m&hourly=temperature_2m,precipitation_probability&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max,weathercode&timezone=auto
```

- Sökningar görs **parallellt på svenska och engelska**, slås ihop per koordinat och svenska namn prioriteras.  
- Prognosdata mappas till en intern modell (`WeatherSnapshot`) med:  
  - Aktuell temperatur  
  - Nästa timmes regnchans  
  - Femdagarsprognos (Hi/Lo, max regnchans, väderkod)  

---

## 🔄 Dataflöde
1. **Home** – användaren söker → API-anrop → träffar visas i lista.  
2. **Details** – vald stad → hämtar prognos → visar current + 5 dagar.  
3. **Saved** – favoriter sparas i Room, uppdateras reaktivt via Flow.  
4. **Settings** – enheter (DataStore) och regnvarningar (WorkManager + notiser).  

---

## 🛠️ Arkitektur & teknik
- **Språk:** Kotlin (100 %)  
- **Nätverk:** Retrofit + OkHttp + kotlinx.serialization  
- **Lagring:** Room (favoriter + cache), DataStore (enheter/alerts)  
- **Bakgrundsjobb:** WorkManager + NotificationChannel  
- **UI:** Material 3, kortbaserad design, tillgänglighetstexter  
- **DI:** Enkel `ServiceLocator`  
- **Moduler:**  
  - `app` – UI, logik, datalager  
  - `timelib` – återanvändbara hjälpfunktioner (t.ex. temperatur och tid)  

---

## 📱 Skärmar
- **Home** – sökfält + resultatlista  
- **Details** – kort med aktuell temp + 5-dagarsprognos  
- **Saved** – lista med sparade favoriter  
- **Settings** – °C/°F, regnvarningar, testnotiser  

---

## 🚀 Bygga & köra
### Debug build
```bash
./gradlew :app:installDebug
```

### Release build (signerad)
1. I Android Studio: **Build → Generate Signed Bundle / APK**  
2. Skapa eller välj en keystore (med alias + lösenord)  
3. Välj **Release** och generera `.apk` eller `.aab`  

Filer genereras i:  
- APK: `app/build/outputs/apk/release/`  
- AAB: `app/build/outputs/bundle/release/`  

---

## 🖼️ Exempel
- **Home:** sök efter en stad och öppna detaljer  
- **Details:** se nuvarande temp + 5-dagarskort  
- **Saved:** favoriter lagras och listas automatiskt  
- **Settings:** växla enheter, aktivera notiser, skicka testnotis  

---
