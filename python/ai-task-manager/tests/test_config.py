import unittest

from ai_task_manager.config import load_config


class TestConfig(unittest.TestCase):
    def test_load_config(self):
        config = load_config('config.yaml')
        self.assertIn('logging', config)
        self.assertIn('max_tokens', config)
        self.assertEqual(config['logging']['level'], 'INFO')

if __name__ == '__main__':
    unittest.main()
