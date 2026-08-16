# Uppgift 12: Generera en multiplikationstabell för talen 1-10.

# Loopar genom varje tal och skriver ut tabellen med strängformattering
for i in range(1, 11):
    for j in range(1, 11):
        print(f"{i*j:4}", end=" ")  # :4 för att justera bredden
    print()  # Radbrytning efter varje rad
