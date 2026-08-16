# Uppgift 6: Ta bort whitespace och alla tecken på ojämna index.

input_str = input("Skriv in en sträng: ")

# Ta bort whitespace med replace
clean_str = input_str.replace(" ", "").replace("\t", "").replace("\n", "")

# Filtrera ut tecken på ojämna index
result = "".join([clean_str[i] for i in range(len(clean_str)) if i % 2 == 0])

print(f"Resultat: {result}")
