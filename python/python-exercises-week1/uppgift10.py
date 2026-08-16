# Uppgift 10: Skapa en sträng av 4 kopior av de sista två tecknen i en sträng.

input_str = input("Skriv in en sträng: ")

if len(input_str) >= 2:
    result = input_str[-2:] * 4  # Ta de sista två tecknen och multiplicera med 4
else:
    result = input_str

print(f"Resultat: {result}")
