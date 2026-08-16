# main.py

from prompt_generator import PromptGenerator


#/ openai.api_key = 'din-api-nyckel'

#def generate_dynamic_prompt(role, task, specifics, context, examples, notes, deadline, priority):
# prompt_generator = PromptGenerator()
# prompt = prompt_generator.create_developer_prompt(role, task, specifics, context, examples, notes, deadline, priority)
    
#  response = openai.Completion.create(
    #    engine="text-davinci-003",
#     prompt=f"Generate a more detailed and context-aware prompt based on the following:\n{prompt}",
#      max_tokens=500
#   )
    
#   return response.choices[0].text.strip()
##/


def main() -> None:
    """
    Main function to create a sample developer prompt and display formatting rules.
    """
    prompt_generator = PromptGenerator()

    # Display formatting rules
    prompt_generator.display_formatting_rules()

    # Create a sample developer prompt
    role = "Backend Developer"
    task = "Implement API Endpoints"
    specifics = """
1. Use Django REST Framework.
2. Ensure all endpoints are properly documented.
3. Implement JWT authentication.
"""
    context = """
The project is a web application that requires secure communication between the client and server. 
All endpoints must follow RESTful principles.
"""
    examples = """
- Example of a GET endpoint:
```python
@api_view(['GET'])
def example_view(request):
    return Response({"message": "Hello, world!"})
"""
    notes = "Make sure to handle errors properly and return appropriate status codes."

    prompt = prompt_generator.create_developer_prompt(role, task, specifics, context, examples, notes)
    print(prompt)

if __name__ == "__main__":
    main()
