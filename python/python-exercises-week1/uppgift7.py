# Uppgift 7: Ta en komma-separerad sträng och skriv ut unika ord i alfabetisk ordning.

input_str = input("Skriv in en komma-separerad lista med ord: ")

# Dela upp strängen i en lista och ta bort dubbletter med set
words = sorted(set(input_str.split(", ")))

# Skriv ut orden i alfabetisk ordning
print(f"Unika ord: {', '.join(words)}")

