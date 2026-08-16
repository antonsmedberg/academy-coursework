# AI-Projekt: Enkel Objektigenkänning i Bilder

## Projektbeskrivning
Syftet med detta projekt är att utveckla en grundläggande maskininlärningsmodell som kan identifiera och lokalisera objekt i bilder, såsom människor och djur. Projektet är anpassat för nybörjare och använder enklare tekniker och dataset för att göra inlärningsprocessen mer hanterbar. Modellen kommer också att generera enkla bildbeskrivningar för att ge en förståelse av vad som händer i bilden.

## Tydligt Mål och Lärandemål
Målet med projektet är att lära mig grunderna inom maskininlärning, specifikt inom datorseende och objektigenkänning. Jag vill utveckla en modell som:
- Korrekt identifierar och lokaliserar objekt i bilder.
- Genererar en kort bildbeskrivning baserat på de upptäckta objekten.

Genom att uppnå dessa mål kommer jag att få praktisk erfarenhet av att arbeta med dataset, träna maskininlärningsmodeller och optimera deras prestanda. Det är också ett tillfälle att förstå hur man använder förtränade modeller för att snabba upp utvecklingen.

## Val av Dataset
Jag har valt att använda ett mindre dataset, exempelvis **Pascal VOC** eller **CIFAR-10**, eftersom dessa är mer hanterbara i storlek och innehåller tillräckligt med variation för att träna en grundläggande modell. Om projektet behöver expanderas, finns möjligheten att arbeta med en delmängd av **COCO-datasetet** som fokuserar på specifika kategorier, som människor och djur.

Valet av dataset är viktigt då mindre dataset minskar komplexiteten och träningsbehovet, vilket är lämpligt för en nybörjare. Detta ger mig möjlighet att experimentera med olika modeller och tekniker utan att kräva omfattande datorkraft.

## Val av Modeller
Jag har valt att använda en enkel **Convolutional Neural Network (CNN)**, exempelvis **MobileNet**, som är en lättviktig och effektiv modell för objektigenkänning. Jag kommer att utnyttja förtränade viktningar från MobileNet för att minska utvecklingstiden och förenkla träningsprocessen.

För bildtextgenerering kommer jag att implementera en enkel **encoder-decoder-arkitektur** med en LSTM-baserad decoder. Genom att kombinera dessa tekniker kan jag uppnå både objektigenkänning och grundläggande bildtextgenerering, vilket ger en helhetslösning.

### Alternativa Metoder
Om jag stöter på prestandaproblem eller svårigheter med MobileNet kan jag utforska andra förtränade modeller, såsom **VGG16** eller **ResNet**, för att se om de ger bättre resultat för mitt mindre dataset. Alternativt kan jag använda enklare textgenereringsmetoder om LSTM visar sig vara för avancerat för detta stadium.

## Träningsplattform och Verktyg
Jag kommer att använda **Google Colab** för att träna modellen, eftersom det erbjuder gratis tillgång till GPU, vilket påskyndar träningen utan att kräva lokal maskinvara. **Jupyter Notebook** kommer att användas för att visualisera och analysera datasetet, samt för att testa och utvärdera modellerna.

## Förenklad Projektplan
1. **Datasetanalys och Förberedelse (1 vecka)**:
   - Utforska datasetet och säkerställ att det är rent och korrekt formaterat.
   - Enkel kvalitetskontroll för att undvika problem vid modellträning.

2. **Utveckling av Enkel CNN-modell (2 veckor)**:
   - Implementera en enkel CNN för objektigenkänning med MobileNet eller en annan förtränad modell.

3. **Träning och Optimering (2 veckor)**:
   - Träna modellen i Google Colab och utvärdera dess prestanda.
   - Justera parametrar för att optimera modellens noggrannhet.

4. **Utveckling av Enkel Bildtextgenereringsmodell (2–3 veckor)**:
   - Bygg en enkel encoder-decoder-arkitektur med LSTM och använd förtränade viktningar för snabbare resultat.

## Nästa Steg
1. Utforska datasetet i Jupyter Notebook för att förstå dess struktur och innehåll.
2. Förbered och standardisera data för att säkerställa kompatibilitet med modellen.
3. Bygg och testa träningspipelines i Google Colab för att genomföra den första omgången av modellträning och utvärdering.

## Argumentation för Metoder och Val
Valet av enklare dataset och förtränade modeller är en medveten strategi för att göra projektet genomförbart för en nybörjare. Genom att använda MobileNet och förtränade viktningar kan jag fokusera på att förstå träningsprocessen och objektigenkänningens grunder utan att överbelasta datorresurserna. Alternativa metoder finns tillgängliga om dessa inte skulle ge önskade resultat, vilket ger flexibilitet i projektet.

## Dokumentation
All dokumentation och projektplan finns i detta GitHub-repository. Det inkluderar detaljerade beskrivningar av varje steg, insikter och resultat för att säkerställa att projektet är spårbart och reproducerbart.
