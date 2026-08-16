# Uppgift 5: Lägg till "ing" i slutet av strängen om den är längre än 3 tecken.

input_str = input("Skriv in en sträng: ")

if len(input_str) >= 3:
    result = input_str + "ing"  # Lägg till "ing"
else:
    result = input_str  # Behåll strängen oförändrad

print(f"Resultat: {result}")

