import asyncio
from queue import PriorityQueue

from logger import logger
from models import models
from task import Task, split_task
from utils import load_config

config = load_config()

class PromptProcessor:
    def __init__(self, models, max_tokens):
        """Initialiserar en promptprocessor med modeller och max antal tokens."""
        self.queue = PriorityQueue()
        self.models = models
        self.max_tokens = max_tokens
        self.checkpoints = []

    def add_prompt(self, prompt, priority=1):
        """Lägger till en prompt i kön med en viss prioritet."""
        self.queue.put((priority, prompt))
        logger.info(f"Lade till prompt med prioritet {priority}: {prompt}")

    def add_checkpoint(self, checkpoint):
        """Lägger till en checkpoint till listan."""
        self.checkpoints.append(checkpoint)
        logger.info(f"Lade till checkpoint: {checkpoint}")

    def get_last_checkpoint(self):
        """Hämtar den senaste checkpointen."""
        if self.checkpoints:
            return self.checkpoints[-1]
        return None

    async def process_task(self, task, aggregated_result):
        """Bearbetar en uppgift och dess deluppgifter."""
        results = []
        for model in self.models:
            result = model.process_task(task)
            if result:
                results.append(result)
                aggregated_result.add_result(result)
                logger.info(f"Bearbetade uppgiften: {task.description} av {model.name}")
        for subtask in task.subtasks:
            results.extend(await self.process_task(subtask, aggregated_result))
        return results

    async def process_prompts(self):
        """Bearbetar alla prompts i kön."""
        while not self.queue.empty():
            priority, task_description = self.queue.get()
            last_checkpoint = self.get_last_checkpoint()
            if last_checkpoint:
                task_description = last_checkpoint  # Fortsätt från senaste checkpoint
            task = Task(task_description, priority)
            split_task(task, self.max_tokens)
            aggregated_result = AggregatedResult()
            results = await self.process_task(task, aggregated_result)
            checkpoint = task.get_checkpoint()
            if checkpoint:
                self.add_checkpoint(checkpoint)
            logger.info(f"Bearbetade resultat: {aggregated_result.get_aggregated_result()}")

class AggregatedResult:
    def __init__(self):
        self.results = []

    def add_result(self, result):
        """Lägger till ett resultat i den aggregerade resultatlistan."""
        self.results.append(result)

    def get_aggregated_result(self):
        """Hämtar de aggregerade resultaten som en sträng."""
        return "\n".join(self.results)

# Exempel på asynkron huvudloop
async def main():
    processor = PromptProcessor(models, config['max_tokens'])
    processor.add_prompt("Analysera användarkraven.", priority=2)
    processor.add_prompt("Designa systemarkitekturen.", priority=1)
    processor.add_prompt("Implementera kärnfunktionerna.", priority=3)
    await processor.process_prompts()

if __name__ == "__main__":
    asyncio.run(main())
