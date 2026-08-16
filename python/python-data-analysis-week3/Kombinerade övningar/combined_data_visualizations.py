import pandas as pd
import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt
import logging

# Konfigurera logger
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')


class CombinedExercises:
    """
    En klass som hanterar flera uppgifter relaterade till dataanalys och visualiseringar.
    """

    def __init__(self, df: pd.DataFrame):
        """
        Initialiserar CombinedExercises med en DataFrame och validerar att den innehåller
        de nödvändiga kolumnerna för analys.

        Args:
            df (pd.DataFrame): Data som innehåller information om anställda.
        """
        required_columns = {'City', 'Experience_Category', 'Department', 'Performance_Score', 'Salary'}
        
        if not required_columns.issubset(df.columns):
            missing = required_columns - set(df.columns)
            raise ValueError(f"DataFrame saknar följande nödvändiga kolumner: {missing}")
        
        self.df = df
        logging.info("CombinedExercises har initialiserats med korrekt DataFrame.")

    def korrelation_heatmap(self):
        """
        Beräknar korrelationsmatrisen för numeriska kolumner och skapar en heatmap.
        """
        try:
            if 'Salary' not in self.df or 'Performance_Score' not in self.df:
                raise KeyError("Korrelationsmatrisen kräver kolumnerna 'Salary' och 'Performance_Score'.")
            
            # Beräkna korrelationsmatris för numeriska kolumner
            corr_matrix = self.df.corr()
            logging.info("Korrelationsmatris beräknad.")

            # Skapa en heatmap med Seaborn
            plt.figure(figsize=(8, 6))
            sns.heatmap(corr_matrix, annot=True, cmap='coolwarm', linewidths=0.5)
            plt.title('Heatmap över korrelationsmatrisen')
            plt.tight_layout()
            plt.show()
            logging.info("Heatmap har skapats.")
        except Exception as e:
            logging.error(f"Ett fel uppstod vid skapandet av heatmap: {e}")
        finally:
            plt.close('all')

    def skapa_histogram_fran_numpy(self, num_values: int = 1000):
        """
        Skapar en NumPy-array med slumpmässiga tal, konverterar den till en DataFrame
        och plotter ett histogram med Matplotlib.

        Args:
            num_values (int): Antal slumpmässiga tal som ska genereras. Standard är 1000.
        """
        try:
            # Skapa en NumPy-array med slumpmässiga tal
            random_array = np.random.randn(num_values)
            logging.info(f"Skapade en NumPy-array med {num_values} slumpmässiga värden.")

            # Skapa en DataFrame från arrayen
            df_random = pd.DataFrame(random_array, columns=['Random Values'])
            logging.info("DataFrame från slumpmässig array har skapats.")

            # Plotta ett histogram med Matplotlib
            plt.figure(figsize=(8, 6))
            plt.hist(df_random['Random Values'], bins=30, color='g', alpha=0.7)
            plt.title('Histogram av slumpmässiga värden')
            plt.xlabel('Värden')
            plt.ylabel('Frekvens')
            plt.tight_layout()
            plt.show()
            logging.info("Histogram har skapats.")
        except Exception as e:
            logging.error(f"Ett fel uppstod vid skapandet av histogram: {e}")
        finally:
            plt.close('all')

    def grupperat_stapeldiagram(self):
        """
        Skapar ett grupperat stapeldiagram som visar den genomsnittliga lönen
        per stad och avdelning med hjälp av Matplotlib.
        """
        try:
            if 'City' not in self.df or 'Department' not in self.df or 'Salary' not in self.df:
                raise KeyError("DataFrame måste innehålla 'City', 'Department' och 'Salary' kolumner.")
            
            # Gruppera data efter 'City' och 'Department' och beräkna genomsnittlig lön
            grouped_data = self.df.groupby(['City', 'Department'])['Salary'].mean().unstack()
            logging.info("Data har grupperats efter 'City' och 'Department'.")

            # Skapa ett grupperat stapeldiagram
            grouped_data.plot(kind='bar', figsize=(10, 6), color=sns.color_palette("Set2"))
            plt.title('Genomsnittlig Lön per Stad och Avdelning')
            plt.ylabel('Genomsnittlig Lön')
            plt.xlabel('Stad')
            plt.xticks(rotation=45)
            plt.legend(title='Avdelning')
            plt.tight_layout()
            plt.show()
            logging.info("Grupperat stapeldiagram har skapats.")
        except Exception as e:
            logging.error(f"Ett fel uppstod vid skapandet av stapeldiagram: {e}")
        finally:
            plt.close('all')


# Skapa en sample DataFrame (Du kan byta ut detta mot din egen data)
data = {
    'City': ['Stockholm', 'Göteborg', 'Malmö', 'Uppsala', 'Lund', 'Malmö', 'Göteborg', 'Stockholm'],
    'Experience_Category': ['Junior', 'Senior', 'Junior', 'Senior', 'Junior', 'Senior', 'Junior', 'Senior'],
    'Department': ['IT', 'HR', 'Sales', 'Finance', 'IT', 'HR', 'Sales', 'Finance'],
    'Performance_Score': [88, 79, 95, 92, 87, 83, 80, 91],
    'Salary': [45000, 38000, 52000, 61000, 46000, 39000, 50000, 62000]
}

df = pd.DataFrame(data)

# Initiera klassen och kör alla övningar
exercises = CombinedExercises(df)
exercises.korrelation_heatmap()  # Beräknar korrelation och skapar heatmap
exercises.skapa_histogram_fran_numpy()  # Skapar NumPy-array och plotter histogram
exercises.grupperat_stapeldiagram()  # Skapar grupperat stapeldiagram över genomsnittlig lön


