from unittest.mock import MagicMock

def success_mock():
    """
    Skapar ett mockobjekt som returnerar True för ett lyckat anrop.
    """
    return MagicMock(return_value=True)

def failure_mock():
    """
    Skapar ett mockobjekt som returnerar False för ett misslyckat anrop.
    """
    return MagicMock(return_value=False)

def timeout_mock():
    """
    Skapar ett mockobjekt som utlöser ett TimeoutError vid anrop.
    """
    return MagicMock(side_effect=TimeoutError("Anslutningstimmar"))

def server_error_mock():
    """
    Skapar ett mockobjekt som utlöser ett ConnectionError vid anrop.
    """
    return MagicMock(side_effect=ConnectionError("Serverfel"))

def custom_mock(return_value):
    """
    Skapar ett anpassat mockobjekt med angivet returnerat värde.
    """
    return MagicMock(return_value=return_value)

# Lägg till fler funktioner för att skapa olika mockobjekt efter behov


