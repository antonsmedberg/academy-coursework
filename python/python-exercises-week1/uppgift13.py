# Uppgift 13: Beräkna fakulteten av ett tal.

def factorial(n):
    if n == 0 or n == 1:
        return 1
    else:
        return n * factorial(n - 1)

number = int(input("Skriv in ett tal: "))
print(f"Fakulteten av {number} är: {factorial(number)}")
