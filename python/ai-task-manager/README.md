
# AI Task Manager

## Introduktion

AI Task Manager är ett sofistikerat verktyg utformat för att hantera komplex uppgiftshantering och promptbearbetningsscenarier. Detta projekt utnyttjar avancerade AI-modeller, effektiv loggning och robust konfigurationshantering för att säkerställa sömlös exekvering av uppgifter. Implementeringen inkluderar funktioner som checkpoint-hantering, hantering av tokenbegränsningar och asynkron bearbetning för förbättrad prestanda och skalbarhet.

## Innehållsförteckning

- [Katalogstruktur](#katalogstruktur)
- [Installation](#installation)
- [Användning](#användning)
- [Funktioner](#funktioner)
- [Konfiguration](#konfiguration)
- [Exempel](#exempel)
- [Beroenden](#beroenden)
- [Bidragsgivare](#bidragsgivare)
- [Licens](#licens)

## Katalogstruktur

```plaintext
ai_task_manager/
├── __init__.py
├── config.py
├── logger.py
├── models.py
├── prompt_processor.py
├── task.py
├── utils.py
└── main.py
```

## Installation

För att installera AI Task Manager, följ dessa steg:

1. Klona repositoriet:

   ```bash
   git clone https://github.com/antonsmedberg/ai_task_manager.git
   cd ai_task_manager
   ```

2. Skapa och aktivera en virtuell miljö:

   ```bash
   python3 -m venv venv
   source venv/bin/activate  # På Windows använd `venv\Scripts\activate`
   ```

3. Installera de nödvändiga beroendena:

   ```bash
   pip install -r requirements.txt
   ```

4. Skapa en konfigurationsfil som heter `config.yaml` i katalogen `ai_task_manager`:

   ```yaml
   logging:
     level: INFO
     file: ai_task_manager.log

   max_tokens: 512
   ```

## Användning

För att köra huvudskriptet och börja bearbeta uppgifter, kör:

```bash
python ai_task_manager/main.py
```

Skriptet kommer att bearbeta de fördefinierade uppgifterna, hantera checkpoints och logga resultaten.

## Funktioner

- **Konfigurationshantering**: Hantera inställningar enkelt via en YAML-konfigurationsfil.
- **Loggning**: Omfattande loggning för spårbarhet och felsökning.
- **Uppgiftshantering**: Hantera uppgifter med prioriteringar, deluppgifter och checkpoints.
- **AI-modeller**: Integrera olika AI-modeller för olika kapaciteter.
- **Promptbearbetning**: Bearbeta prompts effektivt och hantera tokenbegränsningar.
- **Asynkron bearbetning**: Bearbeta flera prompts samtidigt för bättre prestanda.
- **Checkpointhantering**: Upprätthålla och återuppta uppgifter från senaste checkpoint.

## Konfiguration

Konfigurationsfilen (`config.yaml`) innehåller inställningar för loggning och tokenbegränsningar:

```yaml
logging:
  level: INFO
  file: ai_task_manager.log

max_tokens: 512
```

- `logging.level`: Loggningsnivån (t.ex. `INFO`, `DEBUG`).
- `logging.file`: Filen som loggarna skrivs till.
- `max_tokens`: Det maximala antalet tokens för att dela upp uppgifter.

## Exempel

Här är ett exempel på hur man använder `PromptProcessor`:

```python
from prompt_processor import PromptProcessor, models

processor = PromptProcessor(models, 512)
processor.add_prompt("Analysera användarkraven.", priority=2)
processor.add_prompt("Designa systemarkitekturen.", priority=1)
processor.add_prompt("Implementera kärnfunktionerna.", priority=3)
processor.process_prompts()
```

## Beroenden

- Python 3.8+
- `PyYAML`
- `transformers`
- `spacy`
- `asyncio`

Installera beroendena med:

```bash
pip install pyyaml transformers spacy asyncio
```

## Bidragsgivare

- [Anton Smedberg](https://github.com/antonsmedberg) - Initialt arbete

## Licens

Detta projekt är licensierat under MIT-licensen - se [LICENSE](LICENSE) för detaljer.

---


