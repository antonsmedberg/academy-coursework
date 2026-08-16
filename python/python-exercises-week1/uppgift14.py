# Uppgift 14: Skapa ett gissningsspel där användaren gissar ett tal mellan 1 och 100.

import random

number_to_guess = random.randint(1, 100)

while True:
    guess = int(input("Gissa ett tal mellan 1 och 100: "))
    if guess < number_to_guess:
        print("För lågt!")
    elif guess > number_to_guess:
        print("För högt!")
    else:
        print("Rätt gissat!")
        break
