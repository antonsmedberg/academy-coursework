import asyncio
import unittest

from ai_task_manager.config import load_config
from ai_task_manager.models import models
from ai_task_manager.prompt_processor import AggregatedResult, PromptProcessor

config = load_config()

class TestPromptProcessor(unittest.TestCase):
    def setUp(self):
        self.processor = PromptProcessor(models, config['max_tokens'])

    def test_add_prompt(self):
        self.processor.add_prompt("Test prompt", priority=1)
        self.assertEqual(self.processor.queue.qsize(), 1)

    def test_add_checkpoint(self):
        self.processor.add_checkpoint("Test checkpoint")
        self.assertEqual(self.processor.get_last_checkpoint(), "Test checkpoint")

    def test_process_prompts(self):
        async def run_test():
            self.processor.add_prompt("Analysera användarkraven.", priority=2)
            self.processor.add_prompt("Designa systemarkitekturen.", priority=1)
            self.processor.add_prompt("Implementera kärnfunktionerna.", priority=3)
            await self.processor.process_prompts()

        asyncio.run(run_test())
        self.assertGreater(len(self.processor.checkpoints), 0)

if __name__ == '__main__':
    unittest.main()
