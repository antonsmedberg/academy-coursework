# Uppgift 8: Konvertera en sträng till versaler om minst 2 av de första 4 tecknen är versaler.

input_str = input("Skriv in en sträng: ")

# Kontrollera hur många av de första 4 tecknen som är versaler
uppercase_count = sum(1 for char in input_str[:4] if char.isupper())

# Om det finns minst 2 versaler, konvertera hela strängen till versaler
if uppercase_count >= 2:
    result = input_str.upper()
else:
    result = input_str

print(f"Resultat: {result}")
