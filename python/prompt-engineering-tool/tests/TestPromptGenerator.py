import unittest
from prompt_generator import PromptGenerator

class TestPromptGenerator(unittest.TestCase):

    def setUp(self):
        self.prompt_generator = PromptGenerator()

    def validate_prompt(self, role, task, specifics, context, examples, notes, prompt):
        """
        Hjälpmetod för att validera innehållet i genererade uppmaningar.
        """
        self.assertIsNotNone(prompt)
        self.assertIn(f"Role: {role}", prompt)
        self.assertIn(f"Task: {task}", prompt)
        self.assertIn(f"Specifics\n{specifics}", prompt)
        self.assertIn(f"Context\n{context}", prompt)
        self.assertIn(f"Examples\n{examples}", prompt)
        self.assertIn(f"Notes\n{notes}", prompt)

    def test_create_developer_prompt_with_valid_inputs(self):
        test_cases = [
            {
                "role": "Backend Developer",
                "task": "Implement API Endpoints",
                "specifics": "Use Django REST Framework.",
                "context": "The project is a web application that requires secure communication between the client and server.",
                "examples": "- Example of a GET endpoint:\n```python\n@api_view(['GET'])\ndef example_view(request):\n    return Response({'message': 'Hello, world!'})\n```",
                "notes": "Make sure to handle errors properly and return appropriate status codes."
            },
            {
                "role": "Frontend Developer",
                "task": "Create User Interface",
                "specifics": "Use React.js for building the UI.",
                "context": "The project is a web application that requires a visually appealing and user-friendly interface.",
                "examples": "- Example of a button component:\n```jsx\nimport React from 'react';\n\nconst Button = ({ children }) => {\n  return (\n    <button>{children}</button>\n  );\n};\n\nexport default Button;\n```",
                "notes": "Ensure the UI is responsive and adapts to different screen sizes."
            },
            {
                "role": "Data Scientist",
                "task": "Perform Exploratory Data Analysis",
                "specifics": "Analyze the dataset and identify patterns.",
                "context": "The dataset contains information about customer purchases.",
                "examples": "- Example of loading a dataset using pandas:\n```python\nimport pandas as pd\n\ndata = pd.read_csv('customer_data.csv')\n```",
                "notes": "Visualize the data using appropriate plots and summary statistics."
            },
            {
                "role": "DevOps Engineer",
                "task": "Configure CI/CD Pipeline",
                "specifics": "Set up a pipeline to automate the build, test, and deployment process.",
                "context": "The project is a web application that requires continuous integration and deployment.",
                "examples": "- Example of a GitHub Actions workflow:\n```yaml\nname: CI/CD Pipeline\n\non: [push]\n\njobs:\n  build:\n    runs-on: ubuntu-latest\n\n    steps:\n      - uses: actions/checkout@v2\n      - name: Set up Python\n        uses: actions/setup-python@v2\n        with:\n          python-version: '3.x'\n      - name: Install dependencies\n        run: pip install -r requirements.txt\n      - name: Run tests\n        run: python -m unittest discover\n      - name: Build and push Docker image\n        run: |\n          docker build -t my-app:latest .\n          docker tag my-app:latest myregistry/my-app:latest\n          docker push myregistry/my-app:latest\n```",
                "notes": "Ensure the pipeline is secure and follows best practices."
            },
            {
                "role": "QA Engineer",
                "task": "Perform Regression Testing",
                "specifics": "Test the application for any regressions introduced by new features.",
                "context": "The project is a web application that requires thorough testing.",
                "examples": "- Example of a test case using Selenium:\n```python\nfrom selenium import webdriver\n\n# Set up the web driver\ndriver = webdriver.Chrome()\n\n# Open the application\ndriver.get('http://localhost:3000/')\n\n# Verify the title of the page\ntitle = driver.title\nself.assertEqual(title, 'My Web Application')\n\n# Close the web driver\ndriver.quit()\n```",
                "notes": "Run the test cases on different browsers and devices."
            }
        ]

        for case in test_cases:
            with self.subTest(case=case):
                prompt = self.prompt_generator.create_developer_prompt(
                    case["role"], case["task"], case["specifics"], case["context"], case["examples"], case["notes"]
                )
                self.validate_prompt(
                    case["role"], case["task"], case["specifics"], case["context"], case["examples"], case["notes"], prompt
                )

if __name__ == '__main__':
    unittest.main()
