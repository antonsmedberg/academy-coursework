import numpy as np
import logging

# Konfigurera loggning
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# Funktion för att skapa en 3x3 matris av slumpmässiga heltal mellan 1 och 10
def skapa_slumpmatris(min_varde: int = 1, max_varde: int = 10, storlek: tuple = (3, 3)) -> np.ndarray:
    """
    Skapar en matris av slumpmässiga heltal mellan min_varde och max_varde.
    
    Args:
        min_varde (int): Lägsta värde som kan genereras.
        max_varde (int): Högsta värde som kan genereras.
        storlek (tuple): Dimensioner på matrisen.
        
    Returnerar:
        np.ndarray: Matris med slumpmässiga heltal, eller None vid fel.
    """
    try:
        matris = np.random.randint(min_varde, max_varde + 1, size=storlek)
        logging.info(f"Skapade en {storlek[0]}x{storlek[1]} matris med slumpmässiga heltal.")
        return matris
    except ValueError as ve:
        logging.error(f"Felaktigt värde vid skapande av matris: {ve}")
        return None
    except Exception as e:
        logging.error(f"Ett oväntat fel uppstod: {e}")
        return None

# Funktion för att utföra elementvis multiplikation av två matriser
def elementvis_multiplikation(storlek: tuple = (4, 4)) -> np.ndarray:
    """
    Utför elementvis multiplikation av två matriser med samma dimensioner.
    
    Args:
        storlek (tuple): Dimensioner på matriserna.
    
    Returnerar:
        np.ndarray: Resultat av elementvis multiplikation, eller None vid fel.
    """
    try:
        matris1 = np.random.randint(1, 11, size=storlek)
        matris2 = np.random.randint(1, 11, size=storlek)
        resultat = matris1 * matris2
        logging.info(f"Utförde elementvis multiplikation av två {storlek[0]}x{storlek[1]} matriser.")
        return resultat
    except ValueError as ve:
        logging.error(f"Felaktigt värde vid elementvis multiplikation: {ve}")
        return None
    except Exception as e:
        logging.error(f"Ett oväntat fel uppstod: {e}")
        return None

# Funktion för att lösa ett system av linjära ekvationer (Ax = b)
def losa_linjara_ekvationer() -> np.ndarray:
    """
    Löser ett system av linjära ekvationer Ax = b.

    Returnerar:
        np.ndarray: Lösningen x för systemet Ax = b, eller None vid fel.
    """
    try:
        A = np.array([[3, 1], [1, 2]])
        b = np.array([9, 8])
        x = np.linalg.solve(A, b)
        logging.info("Löste systemet av linjära ekvationer Ax = b.")
        return x
    except np.linalg.LinAlgError as le:
        logging.error(f"Ett fel i linjär algebra uppstod: {le}")
        return None
    except Exception as e:
        logging.error(f"Ett oväntat fel uppstod: {e}")
        return None

# Funktion för att generera en array med 1000 prover från en binomialfördelning
def binomialfordelning(n: int = 10, p: float = 0.5, storlek: int = 1000) -> np.ndarray:
    """
    Genererar en array med prover från en binomialfördelning.
    
    Args:
        n (int): Antal försök.
        p (float): Sannolikhet för framgång.
        storlek (int): Antal prover.
    
    Returnerar:
        np.ndarray: En array med binomialfördelade värden, eller None vid fel.
    """
    try:
        prover = np.random.binomial(n, p, size=storlek)
        logging.info(f"Genererade {storlek} prover från en binomialfördelning med n={n} och p={p}.")
        return prover
    except ValueError as ve:
        logging.error(f"Felaktigt värde vid generering av binomialfördelning: {ve}")
        return None
    except Exception as e:
        logging.error(f"Ett oväntat fel uppstod: {e}")
        return None

# Funktion för att skapa en 5x5 identitetsmatris och ersätta dess diagonal
def ersatt_diagonal(ersattning_array: np.ndarray) -> np.ndarray:
    """
    Skapar en 5x5 identitetsmatris och ersätter diagonalen med en anpassad array.
    
    Args:
        ersattning_array (np.ndarray): Array som ska ersätta diagonalen.
    
    Returnerar:
        np.ndarray: Matris med den nya diagonalen, eller None vid fel.
    """
    try:
        if len(ersattning_array) != 5:
            raise ValueError("Den anpassade arrayen måste ha exakt 5 element.")
        
        identitetsmatris = np.eye(5)
        np.fill_diagonal(identitetsmatris, ersattning_array)
        logging.info("Ersatte diagonalen i en 5x5 identitetsmatris.")
        return identitetsmatris
    except ValueError as ve:
        logging.error(f"Ogiltigt värde: {ve}")
        return None
    except Exception as e:
        logging.error(f"Ett oväntat fel uppstod: {e}")
        return None

# Huvudprogram inkapslat i en funktion
def main():
    print("3x3 Matris:\n", skapa_slumpmatris())
    print("\nElementvis Multiplikation:\n", elementvis_multiplikation())
    print("\nLösning på linjära ekvationer:\n", losa_linjara_ekvationer())
    print("\nBinomialfördelning:\n", binomialfordelning())
    print("\nErsatt diagonal:\n", ersatt_diagonal(np.array([10, 20, 30, 40, 50])))

if __name__ == "__main__":
    main()





