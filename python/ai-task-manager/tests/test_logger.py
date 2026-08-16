import logging
import unittest

from ai_task_manager.logger import logger


class TestLogger(unittest.TestCase):
    def test_logger(self):
        logger.info("Testing logger")
        with open('ai_task_manager.log', 'r') as log_file:
            logs = log_file.read()
            self.assertIn("Testing logger", logs)

if __name__ == '__main__':
    unittest.main()
