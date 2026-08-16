# Uppgift 9: Vänd strängen om dess längd är en multipel av 4.

input_str = input("Skriv in en sträng: ")

if len(input_str) % 4 == 0:
    result = input_str[::-1]  # Vänd på strängen
else:
    result = input_str

print(f"Resultat: {result}")
