# Uppgift 3: Skriv ett program som skriver ut de två första och två sista tecknen i strängen.

input_string = input("Skriv in en sträng: ")

# Kontrollera att strängen är minst 2 tecken lång
if len(input_string) >= 2:
    # Hämta de två första och sista tecknen
    result = input_string[:2] + " " + input_string[-2:]
    print(f"Resultat: {result}")
else:
    print("Strängen är för kort!")

