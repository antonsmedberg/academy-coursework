# Uppgift 4: Ta två strängar som input och byt ut de två första tecknen i varje.

str1 = input("Skriv första strängen: ")
str2 = input("Skriv andra strängen: ")

if len(str1) >= 2 and len(str2) >= 2:
    # Byt de två första tecknen
    new_str1 = str2[:2] + str1[2:]
    new_str2 = str1[:2] + str2[2:]
    
    print(f"Nya strängar: {new_str1}, {new_str2}")
else:
    print("Båda strängarna måste vara minst 2 tecken långa.")

