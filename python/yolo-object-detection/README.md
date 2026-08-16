
# **YOLOv5 med COCO-dataset för Objektigenkänning**

---

## **Projektbeskrivning**
Det här projektet implementerar en avancerad träningspipeline för objektigenkänning med hjälp av YOLOv5-modellen och COCO-datasetet. Målet är att skapa en robust och effektiv lösning för realtidsobjektigenkänning som kan identifiera och klassificera objekt i bilder med hög precision och snabbhet.

Projektet utvecklades från en ursprunglig plan att använda enklare dataset och modeller som MobileNet och CIFAR-10. Under arbetets gång insåg jag att en mer avancerad metod skulle ge mig större lärandemöjligheter och en djupare förståelse för datorseende och träningspipelines.

**Projektet möter följande mål:**
- Skriva väldokumenterad och lättläst kod.
- Motivera val av modeller och dataset.
- Implementera effektiv Pythonkod med robust felhantering.
- Utforska och dokumentera förbättringsmöjligheter.

---

## **Motivering av val**

### **Val av algoritm: YOLOv5**
YOLOv5 valdes eftersom det erbjuder en optimal balans mellan snabbhet och noggrannhet, vilket gör den idealisk för realtidsapplikationer. Med dess modulära design och omfattande dokumentation var YOLOv5 det bästa valet för att implementera en robust träningspipeline och lära mig hur avancerade modeller fungerar.

### **Val av dataset: COCO**
COCO-datasetet (Common Objects in Context) är en industristandard med över 80 objektklasser. Jag valde COCO eftersom:
- **Högkvalitativ data**: Datasetet innehåller detaljerade annoteringar och mångsidiga objekt i olika miljöer.
- **Relevans**: COCO används ofta för benchmarking av objektigenkänningsmodeller, vilket gör det möjligt att jämföra prestanda med andra lösningar.

---

## **Projektmål**
Projektet syftar till att:
1. Implementera en avancerad träningspipeline med YOLOv5 och COCO-datasetet.
2. Förstå och hantera datasetförberedelser och felhantering i AI-projekt.
3. Optimera träningsparametrar för att förbättra modellens prestanda.
4. Exportera modellen i ONNX-format för bredare kompatibilitet.

---

## **Projektstruktur**

### **1. Datasetförberedelser**
- Automatisk nedladdning och packning av COCO128, en mindre version av COCO.
- Verifiering av datasetstruktur för att säkerställa att alla nödvändiga filer är på plats.
- Generering av en `dataset.yaml`-fil för att definiera tränings- och valideringsvägar samt objektklasser.

### **2. Träningspipeline**
- **Parametrar**:
  - Bildstorlek: 640x640 pixlar.
  - Batchstorlek: 16.
  - Antal epoch: 50.
  - Optimerare: AdamW.
- Dynamisk validering av indata och träningsparametrar för att säkerställa korrekthet.
- Loggning av träningsresultat, inklusive precision (P), recall (R), och mean Average Precision (mAP).

### **3. Testning och visualisering**
- Modellen testas med nya bilder för att generera prediktioner.
- Annoterade bilder sparas och analyseras för utvärdering.

### **4. Export och distribution**
- Export av den tränade modellen till ONNX-format för att möjliggöra vidare användning i olika applikationer.

---

## **Kod och Implementering**

Projektets implementation finns i en Jupyter Notebook-fil som innehåller all kod för datasetförberedelser, träningspipeline, testning och export. Du kan komma åt notebooken direkt via länken nedan:

- 📂 **[ITHS_Projekt_AntonSme.ipynb](https://github.com/antonsmedberg/projekt-del-2-Ai/blob/main/ITHS_Projekt_AntonSme.ipynb)**

### **Notebook-innehåll**
1. **Initialisering och beroenden**: Installation av YOLOv5 och nödvändiga bibliotek.
2. **Datasetförberedelser**: Automatisk nedladdning och verifiering av COCO128.
3. **Modellträning**: Implementering av träningsloop och loggning av resultat.
4. **Testning och visualisering**: Visualisering av prediktioner och sparande av annoterade bilder.
5. **Export till ONNX**: Export av den tränade modellen för vidare användning.

---

## **Kodkvalitet och Felhantering**

Projektet fokuserar på att upprätthålla hög kodkvalitet genom:
- **Modularisering**: Funktioner och moduler är tydligt definierade för bättre läsbarhet och underhåll.
- **Felhantering**: Kritiska delar av pipeline, som datasetvalidering och parameterkontroll, inkluderar robusta felhanteringsmekanismer.
- **Effektivitet**: Pipeline använder GPU-acceleration där det är möjligt för att maximera prestandan.

---

## **Analys och Förbättringsmöjligheter**

### **Vad jag har lärt mig**
- Hur YOLOv5:s arkitektur fungerar och kan anpassas för olika dataset.
- Vikten av datavalidering och robust felhantering för att förbättra stabiliteten i en träningspipeline.
- Hur man optimerar träningsparametrar för att uppnå en bra balans mellan snabbhet och noggrannhet.

### **Förbättringsmöjligheter**
1. **Distribuerad träning**: Implementera stöd för multi-GPU för att förbättra träningshastigheten.
2. **Utökat dataset**: Använd hela COCO-datasetet istället för COCO128 för bättre generalisering.
3. **Avancerad optimering**: Utforska tekniker som mixed precision och kvantisering för att minska latens och resursförbrukning.

---

## **Resultat och Reflektion**

### **Resultat**
- Modellen uppnådde en genomsnittlig precision (mAP50) på 78% på COCO128, vilket är konkurrenskraftigt för datasetets storlek.
- Annoterade resultat sparades och analyserades framgångsrikt.
- Modellen exporterades till ONNX-format utan problem.

### **Reflektion**
Projektet har gett mig en djupare förståelse för avancerade datorseendetekniker och hur man bygger robusta och skalbara AI-lösningar. Jag ser fram emot att vidareutveckla projektet med mer avancerade tekniker och dataset.

---

## **Framtida Arbete**
1. **Generaliseringsförbättring**: Utöka datasetet och träna modellen på fler kategorier och större datamängder.
2. **Produktion**: Implementera stöd för TensorRT för att förbättra prestandan i produktionsmiljöer.
3. **Semi-supervised learning**: Utforska oannoterade data för att förbättra träningsprocessen och minska behovet av omfattande annotering.

---

