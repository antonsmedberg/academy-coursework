import unittest
import timeit

# Enhetstestning är en metod för att testa de minsta testbara delarna av en applikation, 
# isolerat och oberoende från varandra. unittest-biblioteket i Python ger en ram för att skapa och köra sådana tester.

def multiply(a, b):
    """
    Multiplicerar två tal. Funktionen hanterar både heltal och flyttal men kastar 
    ett TypeError om någon av inparametrarna inte är av numerisk typ.
    
    Args:
        a (int/float): Första faktorn.
        b (int/float): Andra faktorn.
    
    Returns:
        int/float: Produkten av a och b.
        
    Raises:
        TypeError: Om något av argumenten inte är ett heltal eller flyttal.
    """
    if not all(isinstance(i, (int, float)) for i in [a, b]):
        raise TypeError("Båda argumenten måste vara hela tal eller flyttal.")
    return a * b


class TestMultiply(unittest.TestCase):
    """
    Testklass för funktionen multiply. Innehåller tester för att verifiera funktionens
    korrekthet över ett brett spektrum av indata inklusive positiva och negativa tal,
    noll, flyttal samt hantering av ogiltiga indata.
    """

    def test_multiply_with_various_numbers(self):
        """
        Testar multiplikation med olika kombinationer av positiva och negativa tal
        samt multiplikation med noll.
        """
        test_cases = [
            (2, 3, 6, "Multiplicera två positiva tal."),
            (-2, -3, 6, "Multiplicera två negativa tal."),
            (2, -3, -6, "Multiplicera ett positivt med ett negativt tal."),
            (0, 5, 0, "Multiplicera med noll ska alltid ge noll."),
            (5, 0, 0, "Multiplicera med noll ska alltid ge noll."),
        ]
        for a, b, expected, msg in test_cases:
            with self.subTest(a=a, b=b):
                self.assertEqual(multiply(a, b), expected, msg)

    def test_multiply_floats(self):
        """
        Testar multiplikation med flyttal för att verifiera korrekt hantering av decimaltal.
        """
        self.assertAlmostEqual(multiply(0.5, 2), 1.0, msg="Multiplicera flyttal.")

    def test_multiply_extreme_values(self):
        """
        Testar multiplikation med extremt stora och små tal för att utvärdera funktionens
        beteende med värden nära gränserna för numerisk precision.
        """
        self.assertEqual(multiply(1e308, 1e308), float('inf'), "Borde resultera i oändlighet för stora tal.")
        self.assertAlmostEqual(multiply(1e-308, 1e-308), 0, "Borde närma sig noll för små tal.")

    def test_multiply_invalid_input(self):
        """
        Testar multiplikation med icke-numeriska indata för att säkerställa korrekt felhantering.
        """
        with self.assertRaises(TypeError):
            multiply("2", 3)
        with self.assertRaises(TypeError):
            multiply(None, 4)

    def test_multiply_performance(self):
        """
        Prestandatest för multiply-funktionen. Målet är att säkerställa att funktionen kan
        hantera ett högt antal operationer inom en rimlig tidsram för att uppfylla
        prestandakraven för realtidssystem.
        """
        execution_time = timeit.timeit('multiply(2, 3)', globals=globals(), number=1000000)
        acceptable_time = 1  # Sekunder, baserat på prestandakrav.
        self.assertTrue(execution_time < acceptable_time, 
                        f"Funktionens prestanda är otillräcklig; exekveringstid: {execution_time}s, Acceptabel tid: {acceptable_time}s.")


if __name__ == '__main__':
    unittest.main()

