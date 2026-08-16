# prompt_generator.py

from typing import Dict
from formatting_rules import FormattingRules
from task_categories import TaskCategory

class PromptGenerator:
    def __init__(self):
        self.formatting_rules = FormattingRules.get_rules()
        self.task_categories = TaskCategory.get_categories()

    def generate_prompt(self, role: str, task: str, specifics: str, context: str, examples: str, notes: str, deadline: str, priority: str) -> str:
        """
        Generates a formatted prompt based on the given parameters.

        Parameters:
            role (str): The role description.
            task (str): The task description.
            specifics (str): Specific instructions.
            context (str): Context for the task.
            examples (str): Example implementations.
            notes (str): Additional notes.
            deadline (str): Deadline for the task.
            priority (str): Priority of the task.

        Returns:
            str: The formatted prompt.
        """
        prompt = f"""
# Role: {role}
{self.task_categories['01 Role']}

## Task: {task}
{self.task_categories['02 Tasks']}

### Specifics
{specifics}

---

### Context
{context}

---

### Examples
{examples}

***

### Notes
{notes}

### Deadline
{deadline}

### Priority
{priority}
"""
        return prompt

    def display_formatting_rules(self) -> None:
        """
        Displays the Markdown formatting rules.
        """
        for rule, description in self.formatting_rules.items():
            print(f"{rule.capitalize()}: {description}")

    def create_developer_prompt(self, role: str, task: str, specifics: str, context: str, examples: str, notes: str, deadline: str, priority: str) -> str:
        """
        Creates a developer-specific prompt.

        Parameters:
            role (str): The role description.
            task (str): The task description.
            specifics (str): Specific instructions.
            context (str): Context for the task.
            examples (str): Example implementations.
            notes (str): Additional notes.
            deadline (str): Deadline for the task.
            priority (str): Priority of the task.

        Returns:
            str: The generated developer prompt.
        """
        return self.generate_prompt(role, task, specifics, context, examples, notes, deadline, priority)



