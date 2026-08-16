import unittest
import logging


class LogContextManager:
    """Context manager för loggning för att hantera loggfiler effektivt."""
    def __enter__(self):
        configure_logging()
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        # Här kan du lägga till logik för att hantera eventuella undantag eller stänga loggfilen på ett säkert sätt.
        logging.shutdown()


def configure_logging():
    """
    Konfigurerar loggning för att spara testresultat i en fil.
    Detaljerad loggning hjälper till att spåra testernas utförande och resultat.
    """
    logging.basicConfig(filename='test_results.log', level=logging.DEBUG, 
                        format='%(asctime)s - %(levelname)s - %(message)s')


# Testdriven utveckling (TDD) är en mjukvaruutvecklingsmetodik där du skriver tester för ny funktion innan du implementerar funktionen själv.
# Detta hjälper till att säkerställa att din implementering uppfyller de förväntade kraven och uppmuntrar till reflektion och förbättring av koden.

def add(a, b):
    """Adderar två numeriska värden med validering."""
    if not all(isinstance(i, (int, float)) for i in [a, b]):
        logging.error("Ogiltiga operandtyper för addition: {}, {}".format(a, b))
        raise ValueError("Båda operanderna måste vara hela tal eller flyttal.")
    logging.debug("add({a}, {b}) kallad.")
    return a + b

def multiply(a, b):
    """Multiplicerar två numeriska värden."""
    logging.debug(f"multiply({a}, {b}) kallad.")
    return a * b


class TestMathOperations(unittest.TestCase):
    """Tester för matematiska operationer."""
    @classmethod
    def setUpClass(cls):
        """Initialiserar resurser som delas mellan tester."""
        print("Loggning konfigurerad.")
        
        
    def assertAndLog(self, a, b, expected, func, operation):
        """Assert och loggning i en centraliserad funktion."""
        result = func(a, b)
        if result == expected:
            logging.info(f'{operation} with {a}, {b}: Expected = {expected}, Got = {result}')
        else:
            logging.warning(f'{operation} with {a}, {b}: Expected = {expected}, Got = {result}, which is incorrect.')
        self.assertEqual(result, expected, f"Incorrect result for {operation} with inputs {a}, {b}")


    def test_add_valid_cases(self):
        """
        Testar 'add' funktionen med olika indata för att säkerställa korrekt hantering av positiva, negativa och decimaltal.
        """
        # Testfall
        test_cases = [
            (1, 2, 3), # Testar med positiva heltal.
            (-1, 1, 0), # Testar med ett negativt och ett positivt heltal.
            (-1, -1, -2), # Testar med två negativa heltal.
            (0, 0, 0), # Testar med nollor.
            (0.5, 0.5, 1.0), # Testar med decimaltal.
        ]
        for a, b, expected in test_cases:
            with self.subTest(f"Testing add with: {a}, {b}"):
                self.assertAndLog(a, b, expected, add, "addition")

    
    def test_multiply_valid_cases(self):
        """
        Testar 'multiply' funktionen med en variation av indata för att bekräfta rätt hantering av olika taltyper.
        """
        # Testfall
        test_cases = [
            (3, 3, 9), # Testar med positiva heltal.
            (-1, 1, -1), # Testar med ett negativt och ett positivt heltal.
            (-1, -1, 1), # Testar med två negativa heltal.
            (0, 10, 0), # Testar multiplikation med noll.
            (0.5, 2, 1.0), # Testar med decimaltal.
        ]
        for a, b, expected in test_cases:
            with self.subTest(f"Testing multiply with: {a}, {b}"):
                self.assertAndLog(a, b, expected, multiply, "multiplication")


    def test_add_invalid_cases(self):
        """
        Testar 'add' funktionen med ogiltiga indata för att bekräfta att korrekta undantag kastas.
        """
        invalid_cases = [
            ("a", 1), # Testar med en sträng och ett heltal.
            (None, 1), # Testar med None och ett heltal.
            ("a", "b"), # Testar med två strängar.
        ]
        for a, b in invalid_cases:
            with self.subTest(f"Testing add with invalid inputs: {a}, {b}"):
                with self.assertRaises(ValueError, msg=f"Expected ValueError for add with inputs {a}, {b}"):
                    add(a, b)


    def test_multiply_invalid_cases(self):
        """
        Verifierar att 'multiply'-funktionen korrekt hanterar ogiltiga indata.
        Tester inkluderar att försöka multiplicera med strängar och None-värden,
        vilket ska resultera i ett ValueError.
        """
        invalid_cases = [
            ("a", 1), # Testar med en sträng och ett heltal.
            (None, 1), # Testar med None och ett heltal.
            ("a", "b"), # Testar med två strängar.
        ]
        for a, b in invalid_cases:
            with self.subTest(f"Testing multiply with invalid inputs: {a}, {b}"):
                with self.assertRaises(ValueError):
                    multiply(a, b)


    @classmethod
    def tearDownClass(cls):
        """Städar upp resurser efter testerna."""
        print("Testsviten slutförd. Resurser städade.")

if __name__ == '__main__':
    with LogContextManager():
        unittest.main(verbosity=2)


