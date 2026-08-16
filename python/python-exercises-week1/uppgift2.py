# Uppgift 2: Skriv ett program som skriver ut frekvensen av tecken i en given sträng.

# Be användaren om en sträng
input_string = input("Skriv in en sträng: ")

# Skapa en tom dictionary för att lagra teckenfrekvens
frekvens = {}

# Loopar igenom varje tecken i strängen
for char in input_string:
    if char in frekvens:
        frekvens[char] += 1  # Öka räknaren om tecknet redan finns
    else:
        frekvens[char] = 1  # Annars, sätt räknaren till 1

# Skriv ut frekvensen som en dictionary
print(f"Teckenfrekvens: {frekvens}")

