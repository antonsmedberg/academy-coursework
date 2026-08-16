import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# Läs in aktiemarknadsdata från CSV-fil
df = pd.read_csv('stock_data.csv')

# Hantera saknade värden
df.fillna(method='ffill', inplace=True)

# Beräkna rullande medelvärden och standardavvikelser
df['Moving_Avg'] = df['Close'].rolling(window=20).mean()
df['Std_Dev'] = df['Close'].rolling(window=20).std()

# Visualisera trender och anomalier
plt.figure(figsize=(10,6))
plt.plot(df['Date'], df['Close'], label='Close')
plt.plot(df['Date'], df['Moving_Avg'], label='20-Day Moving Avg', color='red')
plt.fill_between(df['Date'], df['Moving_Avg'] - 2*df['Std_Dev'], df['Moving_Avg'] + 2*df['Std_Dev'], color='gray', alpha=0.2)
plt.title('Stock Price with Moving Averages and Anomalies')
plt.xlabel('Date')
plt.ylabel('Price')
plt.legend()
plt.tight_layout()
plt.savefig('stock_analysis.png')
plt.show()

# Spara bearbetad data till CSV-fil
df.to_csv('processed_stock_data.csv', index=False)


