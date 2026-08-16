import unittest
from task_categories import TaskCategory

class TestTaskCategory(unittest.TestCase):

    def test_enum_members(self):
        """
        Test to ensure all enum members exist and have correct values.
        """
        self.assertEqual(TaskCategory.ROLE_PROMPTING.value, "Role Prompting")
        self.assertEqual(TaskCategory.CHAIN_OF_THOUGHT_PROMPTING.value, "Chain of Thought Prompting")
        self.assertEqual(TaskCategory.EMOTIONAL_PROMPTING.value, "Emotional Prompting")
        self.assertEqual(TaskCategory.FEW_SHOT_PROMPTING.value, "Few Shot Prompting")
        self.assertEqual(TaskCategory.MAKE_SURE_AI_REMEMBERS.value, "Make Sure AI Remembers")

    def test_get_categories(self):
        """
        Test to ensure the get_categories method returns the correct dictionary.
        """
        expected_categories = {
            "01 Role": "Role Prompting",
            "02 Tasks": "Chain of Thought Prompting",
            "03 Specifics": "Emotional Prompting",
            "04 Context": "Emotional Prompting",
            "05 Examples": "Few Shot Prompting",
            "06 Notes": "Make Sure AI Remembers"
        }
        categories = TaskCategory.get_categories()
        self.assertEqual(categories, expected_categories)
        self.assertEqual(len(categories), 6)
        self.assertIn("01 Role", categories)
        self.assertIn("02 Tasks", categories)
        self.assertIn("03 Specifics", categories)
        self.assertIn("04 Context", categories)
        self.assertIn("05 Examples", categories)
        self.assertIn("06 Notes", categories)

if __name__ == '__main__':
    unittest.main()
