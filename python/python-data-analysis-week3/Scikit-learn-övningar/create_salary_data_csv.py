import pandas as pd

def create_salary_data_csv(filename='salary_data.csv'):
    """
    Skapar en CSV-fil med data för löner baserat på erfarenhet och andra egenskaper.
    """
    data = {
        'Age': [25, 32, 28, 35, 45, 50, 42, 29],
        'Years_Experience': [2, 10, 5, 12, 20, 25, 18, 7],
        'Performance_Score': [88, 79, 95, 92, 87, 83, 80, 91],
        'Salary': [45000, 60000, 52000, 75000, 90000, 110000, 85000, 57000]
    }
    df = pd.DataFrame(data)
    df.to_csv(filename, index=False)
    print(f"{filename} har skapats.")

# Använd funktionen för att skapa CSV-filen
create_salary_data_csv()
