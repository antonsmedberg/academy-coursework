from transformers import pipeline

from logger import logger


class AIModel:
    def __init__(self, name, capability, model_type):
        """Initialiserar en AI-modell med namn, kapacitet och typ."""
        self.name = name
        self.capability = capability
        self.pipeline = pipeline(model_type)

    def process_task(self, task):
        """Bearbetar en uppgift med modellens pipeline."""
        if self.capability in task.description:
            result = self.pipeline(task.description)
            logger.info(f"{self.name} bearbetade uppgiften: {task.description}")
            return f"Resultat från {self.name}: {result}"
        return None

# Definiera specifika modeller
model_a = AIModel("Model A", "Analyze", "sentiment-analysis")
model_b = AIModel("Model B", "Design", "summarization")
model_c = AIModel("Model C", "Implement", "text-generation")

models = [model_a, model_b, model_c]
