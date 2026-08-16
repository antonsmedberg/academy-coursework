import unittest

from ai_task_manager.utils import determine_priority, tokenize_text


class TestUtils(unittest.TestCase):
    def test_determine_priority(self):
        priority = determine_priority("Analyze the company data.")
        self.assertEqual(priority, 2)

    def test_tokenize_text(self):
        tokens = tokenize_text("This is a test.")
        self.assertEqual(tokens, ["This", "is", "a", "test", "."])

if __name__ == '__main__':
    unittest.main()