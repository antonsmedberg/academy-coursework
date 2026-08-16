# Uppgift 1: Skriv ett program som tar emot en sträng som input och skriver ut längden på strängen.

# Vi använder input() för att ta in en sträng från användaren
input_string = input("Skriv in en sträng: ")

# Använd len() för att räkna tecknen i strängen och spara resultatet
length = len(input_string)

# Skriv ut längden på strängen
print(f"Längden på strängen är: {length}")
