import pandas as pd
import logging

# Konfigurera loggning
logging.basicConfig(level=logging.INFO)

# Funktion för att skapa en DataFrame från en dictionary
def skapa_dataframe() -> pd.DataFrame:
    """
    Skapar en pandas DataFrame med information om länder.
    
    Returnerar:
        df (pd.DataFrame): DataFrame med information om 5 länder.
    """
    data = {
        'Land': ['Sverige', 'Norge', 'Finland', 'Danmark', 'Island'],
        'Befolkning': [10000000, 5400000, 5500000, 5800000, 366000],
        'Yta': [450295, 385207, 338424, 43094, 103000],
        'Kontinent': ['Europa', 'Europa', 'Europa', 'Europa', 'Europa']
    }
    logging.info("Skapat DataFrame för länder.")
    return pd.DataFrame(data)

# Funktion för att läsa in CSV-fil och visa de sista 10 raderna
def las_csv_fil(filnamn: str) -> pd.DataFrame:
    """
    Läser in en CSV-fil och returnerar de sista 10 raderna.
    
    Args:
        filnamn (str): Sökväg till CSV-filen.
    
    Returnerar:
        pd.DataFrame: De sista 10 raderna från DataFrame:n eller None vid fel.
    """
    try:
        df = pd.read_csv(filnamn)
        logging.info(f"Lyckades ladda filen: {filnamn}")
        return df.tail(10)
    except FileNotFoundError:
        logging.error(f"Filen {filnamn} hittades inte.")
    except pd.errors.EmptyDataError:
        logging.error(f"Filen {filnamn} är tom.")
    except Exception as e:
        logging.error(f"Ett oväntat fel uppstod: {e}")
    return None

# Funktion för att beräkna medianlön per avdelning
def berakna_median_loner(df: pd.DataFrame) -> pd.Series:
    """
    Beräknar medianlön för varje avdelning.
    
    Args:
        df (pd.DataFrame): DataFrame med lönedata.
    
    Returnerar:
        pd.Series: Medianlön per avdelning.
    """
    try:
        median_loner = df.groupby('Department')['Salary'].median()
        logging.info("Beräknade medianlön per avdelning.")
        return median_loner
    except KeyError:
        logging.error("Kolumnen 'Department' eller 'Salary' saknas.")
        return None

# Funktion för att hitta anställda med högst prestationspoäng i varje stad
def hogsta_prestation_per_stad(df: pd.DataFrame) -> pd.DataFrame:
    """
    Hittar den anställda med högst prestationspoäng i varje stad.
    
    Args:
        df (pd.DataFrame): DataFrame med prestationspoäng och stad.
    
    Returnerar:
        pd.DataFrame: Data för de anställda med högst prestationspoäng per stad.
    """
    try:
        hogsta_prestation = df.loc[df.groupby('City')['Performance_Score'].idxmax()]
        logging.info("Hittade högsta prestationspoäng per stad.")
        return hogsta_prestation
    except KeyError:
        logging.error("Kolumnen 'City' eller 'Performance_Score' saknas.")
        return None

# Funktion för att skapa ny kolumn "Lön per År Erfarenhet"
def skapa_lon_per_ar_erfarenhet(df: pd.DataFrame) -> pd.DataFrame:
    """
    Skapar en ny kolumn som räknar ut lön per år av erfarenhet och hanterar division med 0.
    
    Args:
        df (pd.DataFrame): DataFrame med lön och erfarenhetsår.
    
    Returnerar:
        pd.DataFrame: Uppdaterad DataFrame med ny kolumn.
    """
    try:
        df['Lön_per_År_Erfarenhet'] = df['Salary'] / df['Years_Experience'].replace(0, 1)
        logging.info("Skapat kolumn 'Lön_per_År_Erfarenhet'.")
        return df[['Salary', 'Years_Experience', 'Lön_per_År_Erfarenhet']]
    except KeyError:
        logging.error("Kolumnen 'Salary' eller 'Years_Experience' saknas.")
        return None

# Funktion för att använda pd.melt() på DataFrame:n
def omforma_dataframe(df: pd.DataFrame) -> pd.DataFrame:
    """
    Omformar DataFrame:n med pd.melt för att göra 'Salary' och 'Performance_Score' till variabler.
    
    Args:
        df (pd.DataFrame): DataFrame med kolumnerna 'Salary' och 'Performance_Score'.
    
    Returnerar:
        pd.DataFrame: Omformad DataFrame.
    """
    try:
        df_melted = pd.melt(df, id_vars=['City', 'Department'], value_vars=['Salary', 'Performance_Score'])
        logging.info("Omformade DataFrame med pd.melt().")
        return df_melted
    except KeyError:
        logging.error("En eller flera nödvändiga kolumner saknas för melt.")
        return None

# Funktion för att pivotera DataFrame:n och visa genomsnittlig lön
def pivotera_dataframe(df: pd.DataFrame) -> pd.DataFrame:
    """
    Pivotera DataFrame:n för att visa genomsnittlig lön för varje kombination av Stad och Avdelning.
    
    Args:
        df (pd.DataFrame): DataFrame med lön, stad och avdelning.
    
    Returnerar:
        pd.DataFrame: Pivot-tabel med genomsnittlig lön.
    """
    try:
        pivot_df = df.pivot_table(values='Salary', index='City', columns='Department', aggfunc='mean')
        logging.info("Pivotering av DataFrame genomförd.")
        return pivot_df
    except KeyError:
        logging.error("En eller flera nödvändiga kolumner saknas för pivotering.")
        return None

# Huvudprogram
if __name__ == "__main__":
    # Skapa och visa DataFrame för länder
    land_df = skapa_dataframe()
    print("DataFrame för länder:\n", land_df)

    # Ladda och visa sista 10 raderna av en CSV-fil
    sample_data_df = las_csv_fil('sample_data0.csv')
    if sample_data_df is not None:
        print("\nSista 10 raderna av sample_data0.csv:\n", sample_data_df)

        # Visa medianlön per avdelning
        median_loner = berakna_median_loner(sample_data_df)
        if median_loner is not None:
            print("\nMedianlön per avdelning:\n", median_loner)

        # Hitta den anställda med högst prestationspoäng i varje stad
        hogsta_prestation = hogsta_prestation_per_stad(sample_data_df)
        if hogsta_prestation is not None:
            print("\nAnställda med högst prestationspoäng per stad:\n", hogsta_prestation)

        # Skapa kolumn för lön per år av erfarenhet
        uppdaterad_df = skapa_lon_per_ar_erfarenhet(sample_data_df)
        if uppdaterad_df is not None:
            print("\nDataFrame med kolumn 'Lön per År Erfarenhet':\n", uppdaterad_df)

        # Omforma DataFrame:n
        omformad_df = omforma_dataframe(sample_data_df)
        if omformad_df is not None:
            print("\nOmformad DataFrame:\n", omformad_df)

        # Pivotera DataFrame:n
        pivot_df = pivotera_dataframe(sample_data_df)
        if pivot_df is not None:
            print("\nPivot-tabel för genomsnittlig lön per stad och avdelning:\n", pivot_df)


