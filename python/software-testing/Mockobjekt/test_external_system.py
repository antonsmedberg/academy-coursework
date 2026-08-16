from unittest.mock import patch
import unittest
from mock_objects import success_mock, failure_mock, timeout_mock, server_error_mock, custom_mock

def external_system_function():
    # Föreställ dig att detta är en funktion som kräver anslutning till en extern tjänst
    pass

class TestExternalSystem(unittest.TestCase):
    """
    Testklass för att testa externa systemfunktionen med olika mockobjekt.
    """

    def test_external_system_with_mock(self):
        """
        Testar externa systemfunktionen med olika mockobjekt.
        """
        mock_objects = {
            "success": success_mock(),
            "failure": failure_mock(),
            "timeout": timeout_mock(),
            "server_error": server_error_mock(),
            "custom_true": custom_mock(True),
            "custom_false": custom_mock(False),
            # Lägg till fler mockobjekt för olika scenarier efter behov
        }
        
        for mock_name, mock_obj in mock_objects.items():
            with self.subTest(mock_name=mock_name):
                with patch('__main__.external_system_function', mock_obj):
                    try:
                        result = external_system_function()
                    except Exception as e:
                        result = e
                    
                    if mock_obj.return_value is not None:
                        expected_result = mock_obj.return_value
                        self.assertEqual(result, expected_result, f"{mock_name}: Förväntade ett {'lyckat' if expected_result else 'misslyckat'} anrop.")
                    else:
                        self.assertIsInstance(result, Exception, f"{mock_name}: Förväntade ett undantag.")

if __name__ == '__main__':
    unittest.main()




