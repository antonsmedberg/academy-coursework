import spacy

nlp = spacy.load("en_core_web_sm")

def determine_priority(description):
    """Bestämmer prioriteten för en uppgift baserat på namngivna enheter."""
    doc = nlp(description)
    if any(ent.label_ == "ORG" for ent in doc.ents):
        return 2
    elif any(ent.label_ == "GPE" for ent in doc.ents):
        return 3
    else:
        return 1

def tokenize_text(text):
    """Tokeniserar text till ord."""
    doc = nlp(text)
    return [token.text for token in doc]
