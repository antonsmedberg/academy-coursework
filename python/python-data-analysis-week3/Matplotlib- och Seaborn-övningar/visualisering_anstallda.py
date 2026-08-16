import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import logging

# Konfigurera logger
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')


class DataVisualizer:
    """
    En klass som hanterar olika typer av visualiseringar av data relaterad till anställda.
    """

    def __init__(self, df: pd.DataFrame):
        """
        Initialiserar DataVisualizer med en DataFrame och validerar dess kolumner.
        
        Args:
            df (pd.DataFrame): Data som innehåller information om anställda.
        """
        required_columns = {'City', 'Experience_Category', 'Department', 'Performance_Score', 'Salary'}
        
        if not required_columns.issubset(df.columns):
            missing = required_columns - set(df.columns)
            raise ValueError(f"DataFrame saknar följande nödvändiga kolumner: {missing}")
        
        self.df = df
        logging.info("DataVisualizer har initialiserats med korrekt DataFrame.")

    def _setup_plot(self, title: str, x_label: str, y_label: str, rotation: int = 0) -> None:
        """
        En hjälpmetod för att ställa in grundläggande plot-konfiguration.
        
        Args:
            title (str): Titel på diagrammet.
            x_label (str): Etikett för X-axeln.
            y_label (str): Etikett för Y-axeln.
            rotation (int): Vinkel för rotation av x-axelns etiketter.
        """
        plt.title(title)
        plt.xlabel(x_label)
        plt.ylabel(y_label)
        plt.xticks(rotation=rotation)
        plt.tight_layout()

    def staplat_stapeldiagram(self) -> None:
        """
        Skapar ett staplat stapeldiagram som visar antalet anställda i varje Experience_Category för varje Stad.
        """
        try:
            plt.figure(figsize=(10, 6))
            city_exp_pivot = self.df.pivot_table(index='City', columns='Experience_Category', aggfunc='size', fill_value=0)
            city_exp_pivot.plot(kind='bar', stacked=True, color=sns.color_palette("Set2"))
            self._setup_plot('Antal anställda i varje Experience_Category för varje Stad', 'Stad', 'Antal anställda', 45)
            plt.legend(title='Experience Category')
            plt.show()
            logging.info("Staplat stapeldiagram har skapats.")
        except Exception as e:
            logging.error(f"Ett fel uppstod vid skapandet av staplat stapeldiagram: {e}")
        finally:
            plt.close()

    def par_plot(self) -> None:
        """
        Skapar ett par-plot med Seaborn för de numeriska kolumnerna i DataFrame:n.
        """
        try:
            sns.pairplot(self.df[['Salary', 'Performance_Score']])
            plt.suptitle('Par-plot för Lön och Prestationspoäng', y=1.02)
            plt.tight_layout()
            plt.show()
            logging.info("Par-plot har skapats.")
        except Exception as e:
            logging.error(f"Ett fel uppstod vid skapandet av par-plot: {e}")
        finally:
            plt.close()

    def violindiagram(self) -> None:
        """
        Skapar ett violindiagram som jämför fördelningen av Performance_Score över olika Avdelningar.
        """
        try:
            plt.figure(figsize=(8, 6))
            sns.violinplot(x='Department', y='Performance_Score', data=self.df, palette="muted")
            self._setup_plot('Fördelning av Performance_Scores över olika Avdelningar', 'Avdelning', 'Performance Score', 45)
            plt.show()
            logging.info("Violindiagram har skapats.")
        except Exception as e:
            logging.error(f"Ett fel uppstod vid skapandet av violindiagram: {e}")
        finally:
            plt.close()

    def cirkeldiagram(self) -> None:
        """
        Skapar ett cirkeldiagram som visar andelen anställda i varje Avdelning.
        """
        try:
            plt.figure(figsize=(8, 6))
            department_count = self.df['Department'].value_counts()
            plt.pie(department_count, labels=department_count.index, autopct='%1.1f%%', startangle=90, colors=sns.color_palette("Set3"))
            plt.title('Andelen anställda i varje Avdelning')
            plt.axis('equal')  # För att göra cirkeln rund
            plt.tight_layout()
            plt.show()
            logging.info("Cirkeldiagram har skapats.")
        except Exception as e:
            logging.error(f"Ett fel uppstod vid skapandet av cirkeldiagram: {e}")
        finally:
            plt.close()

    def skapa_subplots(self) -> None:
        """
        Skapar en 2x2 subplot med olika typer av diagram (linje, spridning, stapel och histogram).
        """
        try:
            fig, axs = plt.subplots(2, 2, figsize=(12, 10))

            # Linjediagram för Lön
            axs[0, 0].plot(self.df['Salary'], color='b')
            axs[0, 0].set_title('Linjediagram för Lön')
            axs[0, 0].set_ylabel('Lön')

            # Spridningsdiagram för Lön vs Prestationspoäng
            axs[0, 1].scatter(self.df['Salary'], self.df['Performance_Score'], color='r')
            axs[0, 1].set_title('Spridningsdiagram för Lön vs Prestationspoäng')
            axs[0, 1].set_xlabel('Lön')
            axs[0, 1].set_ylabel('Performance Score')

            # Stapeldiagram för Antalet anställda per Avdelning
            self.df['Department'].value_counts().plot(kind='bar', ax=axs[1, 0], color='g')
            axs[1, 0].set_title('Stapeldiagram för Antalet anställda per Avdelning')
            axs[1, 0].set_ylabel('Antal anställda')

            # Histogram för Prestationspoäng
            axs[1, 1].hist(self.df['Performance_Score'], bins=5, color='g', alpha=0.7)
            axs[1, 1].set_title('Histogram för Prestationspoäng')
            axs[1, 1].set_xlabel('Performance Score')

            plt.tight_layout()
            plt.show()
            logging.info("2x2 Subplots har skapats.")
        except Exception as e:
            logging.error(f"Ett fel uppstod vid skapandet av subplots: {e}")
        finally:
            plt.close()


# Skapa en sample DataFrame (Du kan byta ut detta mot din egen data)
data = {
    'City': ['Stockholm', 'Göteborg', 'Malmö', 'Uppsala', 'Lund', 'Malmö', 'Göteborg', 'Stockholm'],
    'Experience_Category': ['Junior', 'Senior', 'Junior', 'Senior', 'Junior', 'Senior', 'Junior', 'Senior'],
    'Department': ['IT', 'HR', 'Sales', 'Finance', 'IT', 'HR', 'Sales', 'Finance'],
    'Performance_Score': [88, 79, 95, 92, 87, 83, 80, 91],
    'Salary': [45000, 38000, 52000, 61000, 46000, 39000, 50000, 62000]
}

df = pd.DataFrame(data)

# Initiera visualizer och kör alla visualiseringar
visualizer = DataVisualizer(df)
visualizer.staplat_stapeldiagram()
visualizer.par_plot()
visualizer.violindiagram()
visualizer.cirkeldiagram()
visualizer.skapa_subplots()




