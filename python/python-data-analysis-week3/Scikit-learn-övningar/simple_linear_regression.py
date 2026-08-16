import pandas as pd
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error

# Läs in data från CSV-filen
df = pd.read_csv('salary_data.csv')  # Ladda data från salary_data.csv

# Kontrollera att nödvändiga kolumner finns i CSV-filen
if 'Years_Experience' not in df or 'Salary' not in df:
    raise ValueError("CSV-filen måste innehålla 'Years_Experience' och 'Salary' kolumner.")

# Dela data i tränings- och testuppsättningar
X_simple = df[['Years_Experience']]
y = df['Salary']
X_train_simple, X_test_simple, y_train_simple, y_test_simple = train_test_split(X_simple, y, test_size=0.2, random_state=42)

# Skapa och träna modellen
model = LinearRegression()
model.fit(X_train_simple, y_train_simple)

# Gör prediktioner
y_pred = model.predict(X_test_simple)

# Visualisera resultatet med ett spridningsdiagram och regressionslinje
plt.figure(figsize=(8, 6))
plt.scatter(X_train_simple, y_train_simple, color='blue', label='Träningsdata')
plt.plot(X_test_simple, y_pred, color='red', label='Regressionslinje')
plt.xlabel('Years of Experience')
plt.ylabel('Salary')
plt.title('Enkel linjär regression - Salary vs. Years of Experience')
plt.legend()
plt.show()

# Utvärdera modellen
mse = mean_squared_error(y_test_simple, y_pred)
print(f'Mean Squared Error (MSE): {mse}')



