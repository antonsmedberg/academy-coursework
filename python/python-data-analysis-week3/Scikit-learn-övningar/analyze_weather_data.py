import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import logging

# Konfigurera logger
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def analyze_weather_data(input_file='weather_data.csv', output_file='processed_weather_data.csv', plot_file='weather_analysis.png'):
    """
    Läser in väderdata, beräknar rullande medelvärden och skapar en graf över temperaturtrender.
    
    Args:
        input_file (str): Filvägen till CSV-filen med väderdata.
        output_file (str): Filvägen för att spara den bearbetade datan.
        plot_file (str): Filvägen för att spara grafen som PNG.
    """
    try:
        # Läs in väderdata från CSV-filen och konvertera Date till datetime-format
        df = pd.read_csv(input_file)
        df['Date'] = pd.to_datetime(df['Date'])  # Konvertera 'Date' till datetime-format
        logging.info(f"Läste in data från {input_file}")

        # Kontrollera om det finns saknade värden
        if df.isnull().values.any():
            logging.warning("Saknade värden hittades, de kommer att fyllas med framåt-fyllning.")
            df.fillna(method='ffill', inplace=True)
        
        # Beräkna rullande medelvärden för temperatur (7-dagars medelvärde)
        df['Temp_Moving_Avg'] = df['Temperature'].rolling(window=7).mean()

        # Skapa visualiseringar
        plt.figure(figsize=(10,6))
        plt.plot(df['Date'], df['Temperature'], label='Temperature', marker='o')
        plt.plot(df['Date'], df['Temp_Moving_Avg'], label='7-Day Moving Avg', color='red', linestyle='--')
        plt.title('Weather Temperature Trend')
        plt.xlabel('Date')
        plt.ylabel('Temperature (°C)')
        plt.xticks(rotation=45)
        plt.legend()
        plt.tight_layout()

        # Spara visualiseringen som en PNG-fil
        plt.savefig(plot_file)
        logging.info(f"Grafen har sparats som {plot_file}")
        plt.show()

        # Spara den bearbetade datan med rullande medelvärden
        df.to_csv(output_file, index=False)
        logging.info(f"Bearbetad data har sparats som {output_file}")

    except FileNotFoundError:
        logging.error(f"Filen {input_file} kunde inte hittas.")
    except Exception as e:
        logging.error(f"Ett oväntat fel uppstod: {e}")

# Kör analysen
analyze_weather_data()





