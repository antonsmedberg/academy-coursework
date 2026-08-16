import pandas as pd
import numpy as np
import logging

# Konfigurera logger
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def create_weather_data_csv(filename='weather_data.csv', num_days=100):
    """
    Skapar en syntetisk CSV-fil med väderdata.

    Args:
        filename (str): Namn på CSV-filen som ska skapas.
        num_days (int): Antal dagar av data att generera.
    """
    try:
        # Generera datum
        date_range = pd.date_range(end=pd.Timestamp.today(), periods=num_days, freq='D')
        dates = date_range.strftime('%Y-%m-%d')

        # Generera syntetiska temperaturer
        np.random.seed(42)
        temperatures = np.random.normal(loc=15, scale=10, size=num_days)  # Medeltemperatur på 15°C med standardavvikelse på 10°C

        # Skapa DataFrame
        df = pd.DataFrame({
            'Date': dates[::-1],  # Omvänd för att få äldsta datum först
            'Temperature': temperatures
        })

        # Spara till CSV
        df.to_csv(filename, index=False)
        logging.info(f"{filename} har skapats med {num_days} dagar av data.")
    
    except IOError:
        logging.error(f"Kunde inte spara filen {filename}.")
    except Exception as e:
        logging.error(f"Ett oväntat fel uppstod vid skapandet av CSV-filen: {e}")

# Använd funktionen för att skapa CSV-filen
create_weather_data_csv()


