
# 📘 Studenthanteringssystem
**Avancerad_JAVA24_Anton_Smedberg_Uppgift2**

Menybaserat filhanteringssystem för att hantera studentdata på en skola. Skapat med Java, enkel filhantering och användarvänligt gränssnitt. 🎓

---

## 🚀 Funktioner
- **➕ Lägg till studenter:** Skapa poster med unika ID:n, namn och betyg (A-F).
- **💾 Spara till fil:** Studentdata sparas i `students.txt`.
- **📂 Läs från fil:** Läs in tidigare sparade studentposter.
- **🔍 Sök studenter:** Hitta en student med deras ID.
- **📋 Visa alla:** Lista alla lagrade studenter.
- **❌ Avsluta:** Spara data och stäng programmet säkert.

---

## 🗂 Projektstruktur
```
src/ 
├── Student.java // Representerar studentmodell
├── StudentManagementSystem.java // Hanterar logik och filoperationer
├── Main.java // Konsolgränssnitt och interaktion
```

---

## 🛠 Exempel på meny
```plaintext
==== Studenthanteringssystem ====
1. Lägg till student
2. Spara studenter till fil
3. Läs in studenter från fil
4. Sök student via ID
5. Visa alla studenter
6. Avsluta
Välj ett alternativ:
```

---

## ✅ Testfall
- **Lägg till studenter:** Kontrollera att poster skapas korrekt och med validering.
- **Spara och läsa data:** Verifiera att filen `students.txt` hanterar in- och utdata.
- **Sökfunktion:** Kontrollera sökningar för både befintliga och obefintliga ID:n.
- **Visa alla:** Se till att alla poster visas i ett tydligt format.
- **Avsluta:** Säkerställ att data sparas innan programmet stängs.

---

## ✍️ Utvecklat av
Anton Smedberg  
