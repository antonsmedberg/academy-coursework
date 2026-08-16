# Uppgift 11: Hitta det längsta ordet i en lista med ord.

words = input("Skriv in en lista med ord (separerade med mellanslag): ").split()

# Hitta det längsta ordet med max() och len()
longest_word = max(words, key=len)

print(f"Det längsta ordet är: {longest_word}, med längden: {len(longest_word)}")
