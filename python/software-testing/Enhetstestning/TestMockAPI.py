import unittest
import logging
import sys
from unittest.mock import patch
from mock_api import get_user_data, perform_api_call, timeout_mock, server_error_mock

# Konfigurera loggning
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# Lägg till färgkoder för olika loggningsnivåer
LOG_COLORS = {
    'CRITICAL': '\033[91m',  # Röd färg för kritiska fel
    'ERROR': '\033[91m',      # Röd färg för fel
    'WARNING': '\033[93m',    # Gul färg för varningar
    'INFO': '\033[92m',       # Grön färg för information
    'DEBUG': '\033[94m',      # Blå färg för debug-meddelanden
    'ENDC': '\033[0m'         # Återställ till standardfärg
}

class ColoredFormatter(logging.Formatter):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

    def format(self, record):
        log_level = record.levelname
        log_msg = super().format(record)
        return f"{LOG_COLORS.get(log_level, '')}{log_msg}{LOG_COLORS['ENDC']}"

logger.setLevel(logging.INFO)
console_handler = logging.StreamHandler(sys.stdout)
console_handler.setFormatter(ColoredFormatter('%(asctime)s - %(levelname)s - %(message)s'))
logger.addHandler(console_handler)

class TestMockAPI(unittest.TestCase):
    def setUp(self):
        """Förbereder gemensamma resurser för testerna."""
        self.valid_user_id = 1
        self.invalid_user_id = 3
        self.api_endpoint = "/example"
        self.api_data = {"key": "value"}
        self.expected_response = {"status": "ok", "data": "response for standard request", "code": 200}

    @patch('mock_api.perform_api_call')
    def test_api_call_with_expected_response(self, mock_perform_api_call):
        """Testar ett API-anrop med förväntat svar."""
        mock_perform_api_call.return_value = self.expected_response
        response = perform_api_call(self.api_endpoint, self.api_data)
        self.assertEqual(response, self.expected_response)
        logger.info("Tested API call with expected response. Response: %s", response)

    def test_retrieve_valid_user_data(self):
        """Testar hämtning av användardata med giltigt användar-ID."""
        user_data = get_user_data(self.valid_user_id)
        self.assertIsNotNone(user_data)
        self.assertEqual(user_data["id"], self.valid_user_id)
        self.assertEqual(user_data["username"], "mockuser1")
        self.assertEqual(user_data["email"], "mockuser1@example.com")
        logger.info("Retrieved valid user data: %s", user_data)

    def test_retrieve_invalid_user_data(self):
        """Testar hämtning av användardata med ogiltigt användar-ID."""
        user_data = get_user_data(self.invalid_user_id)
        self.assertIsNone(user_data)
        logger.info("Retrieved invalid user data: %s", user_data)

    def test_perform_api_call_with_invalid_data(self):
        """Testar API-anrop med ogiltiga data."""
        invalid_data = {"invalid_key": "invalid_value"}
        response = perform_api_call(self.api_endpoint, invalid_data)
        self.assertEqual(response, {"status": "error", "message": "Unexpected data", "code": 400})
        logger.info("Performed API call with invalid data. Response: %s", response)

    def test_get_user_data_with_invalid_id(self):
        """Testar hämtning av användardata med icke-existerande användar-ID."""
        invalid_user_id = 999  # Antag att detta ID inte finns
        user_data = get_user_data(invalid_user_id)
        self.assertIsNone(user_data)
        logger.info("Retrieved user data with invalid ID: %s", user_data)

    @patch('mock_api.perform_api_call', side_effect=TimeoutError("Connection timed out"))
    def test_api_call_with_timeout(self, mock_perform_api_call):
        """Testar hantering av timeout-fel under API-anrop."""
        with self.assertRaises(TimeoutError) as context:
            mock_perform_api_call(self.api_endpoint, self.api_data)
        self.assertTrue("Connection timed out" in str(context.exception))
        logger.info("Tested API call with timeout.")

    @patch('mock_api.perform_api_call', side_effect=ConnectionError("Server error"))
    def test_api_call_with_server_error_mock(self, mock_perform_api_call):
        """Testar hantering av serverfel under API-anrop med serverfel mock."""
        with self.assertRaises(ConnectionError) as context:
            mock_perform_api_call(self.api_endpoint, self.api_data)
        self.assertTrue("Server error" in str(context.exception))
        logger.info("Tested API call with server error mock.")


def test_api_call_with_different_data(self):
    """Testar API-anrop med olika datamängder."""
    with patch('mock_api.perform_api_call') as mock_perform_api_call:
        test_cases = [
            ({"key": "value1"}, {"status": "ok", "data": "response for value1", "code": 200}),
            ({"key": "value2"}, {"status": "error", "message": "Unexpected data", "code": 400})
        ]

        for input_data, expected_response in test_cases:
            # Mocka perform_api_call för att returnera expected_response
            mock_perform_api_call.return_value = expected_response
            # Utför API-anropet med input_data
            response = perform_api_call(self.api_endpoint, input_data)
            # Jämför den faktiska responsen med den förväntade responsen
            self.assertEqual(response, expected_response)
            logger.info("Tested API call with different data. Input data: %s, Response: %s", input_data, response)

@patch('mock_api.perform_api_call', side_effect=ConnectionError("Server error"))
def test_api_call_with_server_error_message(self, mock_perform_api_call):
    """Testar hantering av serverfel under API-anrop och kontrollerar felmeddelandet."""
    # Implementera testet för att kontrollera att en ConnectionError kastas
    with self.assertRaises(ConnectionError):
        perform_api_call(self.api_endpoint, self.api_data)
    logger.info("Tested API call with server error message.")


if __name__ == "__main__":
    unittest.main()








