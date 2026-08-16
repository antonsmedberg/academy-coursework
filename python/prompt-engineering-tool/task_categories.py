# task_categories.py

from enum import Enum
from typing import Dict

class TaskCategory(Enum):
    ROLE_PROMPTING = "Role Prompting"
    CHAIN_OF_THOUGHT_PROMPTING = "Chain of Thought Prompting"
    EMOTIONAL_PROMPTING = "Emotional Prompting"
    FEW_SHOT_PROMPTING = "Few Shot Prompting"
    MAKE_SURE_AI_REMEMBERS = "Make Sure AI Remembers"

    @classmethod
    def get_categories(cls) -> Dict[str, str]:
        """
        Returns all task categories as a dictionary with keys as codes and values as task category names.

        Returns:
            Dict[str, str]: A dictionary mapping codes to task category names.
        """
        return {
            "01 Role": cls.ROLE_PROMPTING.value,
            "02 Tasks": cls.CHAIN_OF_THOUGHT_PROMPTING.value,
            "03 Specifics": cls.EMOTIONAL_PROMPTING.value,
            "04 Context": cls.EMOTIONAL_PROMPTING.value,
            "05 Examples": cls.FEW_SHOT_PROMPTING.value,
            "06 Notes": cls.MAKE_SURE_AI_REMEMBERS.value
        }




