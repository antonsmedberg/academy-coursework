import logging

from config import load_config

# Läs in konfigurationen
config = load_config()

# Konfigurera logging
logging.basicConfig(
    level=config['logging']['level'],
    filename=config['logging']['file'],
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)

logger = logging.getLogger(__name__)
