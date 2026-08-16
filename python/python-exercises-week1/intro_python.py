
# Anton Smedberg

'''
multi-
line 
comment
'''

# 1. Basic syntax, kommentarer, printing
print("Hej, klassen!\n") # Printa i Python

# 2. Variabler och datatyper
x = 28                 # int
y = 3.14              # float
name = "Anton"       # string
is_fun = True         # boolean

# 3. Type checking, typekonvertering
print(type(x))
z = str(x)
print(type(z))

a = "10"
b = int(a) + 1
print(b)

# 4. string operations
print(len("Bertil"))
print(name.upper())
print(name.lower())
print("  spaces   ".strip())

# 5. string formatting
print(f"My name is {name} and I'm {x} years old")
print("Pi is approximately {:.2f}".format((y)))

# 6. Lists
fruits = ["apple", "banana", "cherry"]
print(fruits[2]) # index 0-2 ['0', '1', '2]
fruits.append("date")
fruits.insert(0, "kiwi") # lägga till en frukt i listan
print(fruits)

# 7. Dictionaries
person_dict = {"name": "Alice", "age": 30, "city": "New York"}
print(person_dict["name"])
person_dict["job"] = "Developer"
print(person_dict)

# 8. Sets
unique_numbers = {1, 2, 3, 4, 5, 5, 5}
print(unique_numbers)

unique_fruits = set(fruits)
print(unique_fruits)

# 9. Input från användare
username_input = input("Please enter your username: ")
print(f"You entered username: {username_input}")
print("Tack för det! Ses nästa gång...\n")

# 10. Conditionals
age = 20
if age >= 18:
  print("Du får gå på klubb")
elif age >= 13:
  print("Du är tonåring")
else:
  print("Du är ett barn")
  
  if username_input == "antonsme":
    print("Hej det är ju Anton!")
    
# 11 Loops
# For loops
for fruit in fruits:
    print(fruit)
    
    count = 0
    while count < 5:
      print(count)
      count = count + 1
      
      print("Range loop")
      for i in range(5): # range(5) = [0,1,2,3,4]
        print(i)
        
        # Functions
        def greet(name):
          #print(f"Hello, {name}")
          return f"Hello, {name}!"
          
      greeting = greet("Anton")
      print(greeting)