import spacy

from utils import determine_priority, tokenize_text

nlp = spacy.load("en_core_web_sm")

class Task:
    def __init__(self, description, priority=1, subtasks=None, checkpoint=None):
        """Initialiserar en uppgift med beskrivning, prioritet och deluppgifter."""
        self.description = description
        self.priority = priority
        self.subtasks = subtasks or []
        self.checkpoint = checkpoint

    def add_subtask(self, subtask):
        """Lägger till en deluppgift till uppgiften."""
        self.subtasks.append(subtask)

    def set_checkpoint(self, checkpoint):
        """Sätter en checkpoint för uppgiften."""
        self.checkpoint = checkpoint

    def get_checkpoint(self):
        """Hämtar checkpointen för uppgiften."""
        return self.checkpoint

def split_task(task, max_tokens, level=0):
    """Delar upp uppgiften i deluppgifter baserat på meningsgränser och antal tokens."""
    doc = nlp(task.description)
    current_subtask = ""
    current_token_count = 0
    
    for sentence in doc.sents:
        sentence_text = sentence.text
        sentence_tokens = tokenize_text(sentence_text)
        sentence_token_count = len(sentence_tokens)
        
        # Om nuvarande tokenantal plus meningen överskrider max_tokens, skapa en deluppgift
        if current_token_count + sentence_token_count > max_tokens:
            subtask_priority = determine_priority(current_subtask)
            subtask = Task(current_subtask.strip(), priority=subtask_priority)
            task.add_subtask(subtask)
            current_subtask = sentence_text
            current_token_count = sentence_token_count
        else:
            current_subtask += " " + sentence_text
            current_token_count += sentence_token_count
    
    if current_subtask:
        subtask_priority = determine_priority(current_subtask)
        subtask = Task(current_subtask.strip(), priority=subtask_priority)
        task.add_subtask(subtask)

    if level < 2:
        for subtask in task.subtasks:
            split_task(subtask, max_tokens, level + 1)
