import pandas as pd
from sklearn.model_selection import train_test_split

# Skapa exempeldata
data = {
    'Age': [25, 32, 28, 35, 45, 50, 42, 29],
    'Years_Experience': [2, 10, 5, 12, 20, 25, 18, 7],
    'Performance_Score': [88, 79, 95, 92, 87, 83, 80, 91],
    'Salary': [45000, 60000, 52000, 75000, 90000, 110000, 85000, 57000]
}

df = pd.DataFrame(data)

# Dela data i egenskaper (X) och målvariabel (y)
X = df[['Age', 'Years_Experience', 'Performance_Score']]
y = df['Salary']

# Dela data i tränings- och testuppsättningar
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

print("Träningsdata (X_train):\n", X_train.head())
print("\nTestdata (X_test):\n", X_test.head())
