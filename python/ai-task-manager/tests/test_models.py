import unittest

from ai_task_manager.models import model_a, model_b, model_c
from ai_task_manager.task import Task


class TestModels(unittest.TestCase):
    def setUp(self):
        self.task_analyze = Task("Analyze the data.")
        self.task_design = Task("Design the system.")
        self.task_implement = Task("Implement the solution.")

    def test_model_a(self):
        result = model_a.process_task(self.task_analyze)
        self.assertIn("Resultat från Model A", result)

    def test_model_b(self):
        result = model_b.process_task(self.task_design)
        self.assertIn("Resultat från Model B", result)

    def test_model_c(self):
        result = model_c.process_task(self.task_implement)
        self.assertIn("Resultat från Model C", result)

if __name__ == '__main__':
    unittest.main()
