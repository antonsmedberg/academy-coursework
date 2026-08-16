from unittest.mock import MagicMock

class NotFoundError(Exception):
    """Undantag som indikerar att resursen inte kunde hittas (404 Not Found)."""
    pass

class InternalServerError(Exception):
    """Undantag som indikerar ett internt serverfel (500 Internal Server Error)."""
    pass

def get_user_data(user_id):
    """Simulerar ett API-anrop för att hämta användardata."""
    # Simulera databasåtkomst för att hämta användardata
    if user_id == 1:
        return {"id": 1, "username": "mockuser1", "email": "mockuser1@example.com"}
    elif user_id == 2:
        return {"id": 2, "username": "mockuser2", "email": "mockuser2@example.com"}
    else:
        return None  # Returnerar None för ogiltiga eller okända användar-ID

def perform_api_call(endpoint, data):
    """Simulerar ett API-anrop till en specifik slutpunkt med angiven data."""
    # Definierar svarslogik baserat på endpoint och data
    responses = {
        "/example": lambda d: {"status": "ok", "data": "response for standard request", "code": 200} if d == {"key": "value"} else {"status": "error", "message": "Unexpected data", "code": 400},
        "/special_case": lambda d: {"status": "ok", "data": "special response", "code": 200} if d.get("special") else {"status": "error", "message": "Missing special key", "code": 400},
    }

    # Väljer svarsfunktion baserat på endpoint
    response_function = responses.get(endpoint, lambda d: {"status": "error", "message": "Invalid endpoint", "code": 404})
    return response_function(data)

def timeout_mock():
    """Skapar ett mock-objekt som utlöser ett TimeoutError vid anrop."""
    return MagicMock(side_effect=TimeoutError("Connection timed out"))

def server_error_mock():
    """Skapar ett mock-objekt som utlöser ett ConnectionError vid anrop."""
    return MagicMock(side_effect=ConnectionError("Server error"))

def not_found_mock():
    """Skapar ett mock-objekt som utlöser ett NotFoundError vid anrop."""
    return MagicMock(side_effect=NotFoundError("Not Found"))

def internal_server_error_mock():
    """Skapar ett mock-objekt som utlöser ett InternalServerError vid anrop."""
    return MagicMock(side_effect=InternalServerError("Internal Server Error"))

# Lägg till fler funktioner för att skapa olika mock-objekt vid behov



