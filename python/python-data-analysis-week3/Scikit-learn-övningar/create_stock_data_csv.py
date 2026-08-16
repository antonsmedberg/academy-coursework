import pandas as pd
import numpy as np

def create_stock_data_csv(filename='stock_data.csv', num_days=100):
    """
    Skapar en syntetisk CSV-fil med aktiemarknadsdata.

    Args:
        filename (str): Namn på CSV-filen som ska skapas.
        num_days (int): Antal dagar av data att generera.
    """
    # Generera datum
    date_range = pd.date_range(end=pd.Timestamp.today(), periods=num_days, freq='B')  # 'B' för business days
    dates = date_range.strftime('%Y-%m-%d')

    # Generera syntetiska priser
    np.random.seed(42)
    price = 100
    prices = []
    for _ in range(num_days):
        change_percent = np.random.normal(0, 1)
        price *= (1 + change_percent / 100)
        prices.append(price)

    # Skapa DataFrame
    df = pd.DataFrame({
        'Date': dates[::-1],  # Omvänd för att få äldsta datum först
        'Open': prices,
        'High': [p * np.random.uniform(1.00, 1.05) for p in prices],
        'Low': [p * np.random.uniform(0.95, 1.00) for p in prices],
        'Close': [p * np.random.uniform(0.98, 1.02) for p in prices],
        'Volume': np.random.randint(1000, 10000, size=num_days)
    })

    # Säkerställ att kolumnerna 'High', 'Low', 'Open', 'Close' är konsistenta
    df['High'] = df[['Open', 'High', 'Low', 'Close']].max(axis=1)
    df['Low'] = df[['Open', 'High', 'Low', 'Close']].min(axis=1)

    # Spara till CSV
    df.to_csv(filename, index=False)
    print(f"{filename} har skapats med {num_days} dagar av data.")

# Använd funktionen för att skapa CSV-filen
create_stock_data_csv()
