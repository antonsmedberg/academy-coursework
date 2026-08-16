import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error

# Skapa exempeldata
data = {
    'Age': [25, 30, 35, 40, 45, 50, 55, 60],
    'Years_Experience': [1, 3, 5, 7, 10, 12, 15, 20],
    'Performance_Score': [88, 79, 95, 92, 87, 83, 80, 91],
    'Salary': [30000, 35000, 40000, 45000, 50000, 55000, 60000, 70000]
}

df = pd.DataFrame(data)

# Definiera mål och egenskaper
X = df[['Years_Experience']]
y = df['Salary']

# Dela upp i tränings- och testuppsättningar
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# Träna linjär regressionsmodell
model = LinearRegression()
model.fit(X_train, y_train)

# Prediktion
y_pred = model.predict(X_test)

# Plotta regressionslinjen och spridningsdiagram
plt.scatter(X, y, color='blue')
plt.plot(X, model.predict(X), color='red', linewidth=2)
plt.title('Years of Experience vs Salary')
plt.xlabel('Years of Experience')
plt.ylabel('Salary')
plt.show()

# Utvärdera modellens prestanda
mse = mean_squared_error(y_test, y_pred)
print(f"Mean Squared Error: {mse}")
