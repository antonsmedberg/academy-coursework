import numpy as np
import pandas as pd

class SimpleLinearRegression:
    """
    Enkel linjär regressionsmodell som använder minsta kvadratmetoden.
    """

    def __init__(self):
        self.coefficient = 0
        self.intercept = 0

    def fit(self, X, y):
        """
        Tränar modellen för att passa datan med minsta kvadratmetoden.
        """
        n = len(X)
        mean_x = np.mean(X)
        mean_y = np.mean(y)
        
        self.coefficient = np.sum((X - mean_x) * (y - mean_y)) / np.sum((X - mean_x)**2)
        self.intercept = mean_y - self.coefficient * mean_x

    def predict(self, X):
        """
        Returnerar prediktioner för given data X.
        """
        return self.intercept + self.coefficient * X

# Exempeldata
data = {
    'Years_Experience': [1, 3, 5, 7, 10, 12, 15, 20],
    'Salary': [30000, 35000, 40000, 45000, 50000, 55000, 60000, 70000]
}

df = pd.DataFrame(data)

# Definiera mål och egenskaper
X = df['Years_Experience']
y = df['Salary']

# Träna modellen
model = SimpleLinearRegression()
model.fit(X, y)

# Prediktion
y_pred = model.predict(X)

# Visualisera
import matplotlib.pyplot as plt
plt.scatter(X, y, color='blue')
plt.plot(X, y_pred, color='red')
plt.title('Simple Linear Regression')
plt.xlabel('Years of Experience')
plt.ylabel('Salary')
plt.show()
