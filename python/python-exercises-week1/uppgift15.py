# Uppgift 15: Kontrollera om ett ord är ett palindrom.

input_str = input("Skriv in ett ord: ")

if input_str == input_str[::-1]:
    print(f"{input_str} är ett palindrom!")
else:
    print(f"{input_str} är inte ett palindrom.")
