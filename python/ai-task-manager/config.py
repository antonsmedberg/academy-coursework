import yaml


def load_config(file_path='config.yaml'):
    """Läser konfigurationsfilen och returnerar inställningarna."""
    with open(file_path, 'r') as file:
        config = yaml.safe_load(file)
    return config

# Exempel på config.yaml
"""
logging:
  level: INFO
  file: ai_task_manager.log

max_tokens: 512
"""
