import unittest

from ai_task_manager.task import Task, split_task


class TestTask(unittest.TestCase):
    def test_task_initialization(self):
        task = Task("Test description", priority=1)
        self.assertEqual(task.description, "Test description")
        self.assertEqual(task.priority, 1)
        self.assertEqual(task.subtasks, [])

    def test_split_task(self):
        task = Task("This is a test. Split this task.", priority=1)
        split_task(task, max_tokens=5)
        self.assertGreater(len(task.subtasks), 0)

if __name__ == '__main__':
    unittest.main()