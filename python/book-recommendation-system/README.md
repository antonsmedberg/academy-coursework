
# **Bokrekommendationssystem**

Ett Python-baserat system för att rekommendera fantasy- och sci-fi-böcker baserat på användarens preferenser. Projektet innehåller två versioner:

1. **Enkel bokrekommenderare**: En grundläggande implementation som använder k-Nearest Neighbors (k-NN).
2. **Avancerad bokrekommenderare**: En förbättrad version som använder cosinuslikhet och hanterar mer avancerade användarpreferenser.

---

## **Projektstruktur**

### Filer
- **`basic_book_recommender.py`**: Implementerar en grundläggande bokrekommendationsalgoritm med ett litet dataset och k-NN.
- **`advanced_book_recommender.py`**: En utökad version med större dataset, användarpreferenser och cosinuslikhet.

---

## **Funktioner**

### Enkel bokrekommenderare
- Använder ett litet dataset av fördefinierade böcker.
- Rekommenderar liknande böcker baserat på egenskaper som subgenre, teman och betyg.
- Använder k-NN med euklidiskt avstånd.
- Enkel kommandoradsgränssnitt för interaktion.

### Avancerad bokrekommenderare
- Större dataset med fler böcker och egenskaper.
- Möjlighet att ange detaljerade preferenser för subgenrer och teman.
- Använder cosinuslikhet för att hantera högdimensionella data.
- Levererar mer exakta och varierade rekommendationer.

---

## **Kom igång**

### Förkrav
Se till att du har Python 3.7+ installerat samt följande bibliotek:
- `pandas`
- `numpy`
- `scikit-learn`

Installera nödvändiga paket med följande kommando:
```bash
pip install pandas numpy scikit-learn
```

---

## **Hur man kör**

### Enkel bokrekommenderare
1. Navigera till katalogen där `basic_book_recommender.py` finns.
2. Kör skriptet:
   ```bash
   python basic_book_recommender.py
   ```
3. Följ anvisningarna för att ange en boktitel.

### Avancerad bokrekommenderare
1. Navigera till katalogen där `advanced_book_recommender.py` finns.
2. Kör skriptet:
   ```bash
   python advanced_book_recommender.py
   ```
3. Svara på frågorna om dina preferenser för subgenrer och teman.

---

## **Anpassningar**

### Lägga till fler böcker
För att utöka datasetet:
1. Öppna motsvarande Python-fil (`basic_book_recommender.py` eller `advanced_book_recommender.py`).
2. Hitta metoden `load_data()` i klassen `BookData`.
3. Lägg till fler poster i `self.books_df` DataFrame.

Exempel:
```python
self.books_df = pd.DataFrame({
    'title': ['Ny Boktitel', ...],
    'subgenre': ['Ny Subgenre', ...],
    'themes': ['Nya Teman', ...],
    'rating': [4.5, ...]
})
```

### Byta algoritmer
I den avancerade versionen kan du experimentera med andra algoritmer genom att modifiera beräkningen av likhet i metoden `get_recommendations`.

---

## **Tack**
Projektet är möjligt tack vare följande bibliotek och verktyg:
- [Pandas](https://pandas.pydata.org/)
- [NumPy](https://numpy.org/)
- [scikit-learn](https://scikit-learn.org/)
