import random
from enum import Enum
from typing import Optional

class Färgkoder:
    """Klass för ANSI escape-koder för att hantera färg i terminalen."""
    RESET = "\033[0m"
    GREEN = "\033[92m"
    RED = "\033[91m"
    YELLOW = "\033[93m"
    BLUE = "\033[94m"
    INTRO_TEXT = BLUE
    WIN_TEXT = GREEN
    LOSE_TEXT = RED
    DRAW_TEXT = YELLOW

    @staticmethod
    def färga(text: str, färg: str) -> str:
        """
        Färgar text med den angivna färgen och återställer till standardfärg.
        
        :param text: Texten som ska färgas.
        :param färg: ANSI escape-kod för färgen.
        :return: Färgad textsträng.
        """
        return f"{färg}{text}{Färgkoder.RESET}"

    @staticmethod
    def färga_och_skriv(text: str, färg: str):
        """Färgar en text och skriver ut den."""
        print(Färgkoder.färga(text, färg))

class Val(Enum):
    """Enum för att representera möjliga val i spelet."""
    STEN = "sten"
    SAX = "sax"
    PÅSE = "påse"

    @classmethod
    def val_möjligheter(cls):
        """Returnerar en lista med alla möjliga val som strängar."""
        return list(cls)

    @classmethod
    def från_tecken(cls, tecken: str):
        """Returnerar ett Val baserat på inmatningstecken."""
        val_mapping = {'s': cls.STEN, 'x': cls.SAX, 'p': cls.PÅSE}
        return val_mapping.get(tecken)

class StenSaxPåse:
    """Huvudklassen för spelet Sten-Sax-Påse."""
    
    def __init__(self, vinnarpoäng: int = 5):
        """
        Initialiserar spelets attribut.
        
        :param vinnarpoäng: Antalet poäng som krävs för att vinna spelet.
        """
        self.VINNARPOÄNG = vinnarpoäng
        self.spelarens_poäng = 0
        self.datorns_poäng = 0
        self.high_score = 0

    def visa_introduktion(self):
        """Visar en introduktion till spelet med regler och målsättning."""
        Färgkoder.färga_och_skriv("Välkommen till Sten-Sax-Påse!", Färgkoder.INTRO_TEXT)
        print("Slå datorn i ett klassiskt spel av strategi.")
        Färgkoder.färga_och_skriv(f"Första till {self.VINNARPOÄNG} poäng vinner spelet.", Färgkoder.YELLOW)
        Färgkoder.färga_och_skriv("Lycka till!\n", Färgkoder.GREEN)

    def hämta_spelarens_val(self) -> Optional[Val]:
        """
        Hämtar och validerar spelarens val.
        
        :return: Spelarens val som ett Val Enum eller None om spelaren vill avsluta.
        """
        while True:
            val = input(Färgkoder.färga("Välj Sten (s), Sax (x), eller Påse (p) (eller skriv 'sluta' för att avsluta): ", Färgkoder.YELLOW)).lower()
            val_objekt = Val.från_tecken(val)
            if val_objekt:
                return val_objekt
            elif val == 'sluta':
                return None
            else:
                Färgkoder.färga_och_skriv("Ogiltigt val. Ange 's', 'x', 'p' eller 'sluta'.", Färgkoder.RED)

    def datorns_val(self) -> Val:
        """
        Genererar datorns val slumpmässigt från de möjliga valen.
        
        :return: Datorns val som ett Val Enum.
        """
        return random.choice(Val.val_möjligheter())

    def bestäm_vinnare(self, spelare: Val, datorn: Val) -> str:
        """
        Avgör vinnaren baserat på spelarens och datorns val.
        
        :param spelare: Spelarens val.
        :param datorn: Datorns val.
        :return: 'Spelare', 'Datorn' eller 'Oavgjort' beroende på resultatet.
        """
        vinstregler = {
            (Val.STEN, Val.SAX): "Spelare",
            (Val.SAX, Val.PÅSE): "Spelare",
            (Val.PÅSE, Val.STEN): "Spelare"
        }
        if spelare == datorn:
            return "Oavgjort"
        return vinstregler.get((spelare, datorn), "Datorn")

    def visa_poäng(self):
        """Visar aktuell poängställning och spelarens bästa resultat (high score)."""
        Färgkoder.färga_och_skriv(f"Ställning: Spelare {self.spelarens_poäng} - {self.datorns_poäng} Datorn", Färgkoder.BLUE)
        Färgkoder.färga_och_skriv(f"Ditt bästa resultat hittills: {self.high_score} poäng", Färgkoder.GREEN)

    def kontrollera_vinst(self) -> bool:
        """
        Kontrollerar om någon har nått vinstpoängen och avslutar spelet om så är fallet.
        
        :return: True om spelet är över, annars False.
        """
        if self.spelarens_poäng >= self.VINNARPOÄNG:
            Färgkoder.färga_och_skriv(f"Grattis! Du har nått {self.VINNARPOÄNG} poäng och vunnit spelet! 🏆", Färgkoder.GREEN)
            return True
        elif self.datorns_poäng >= self.VINNARPOÄNG:
            Färgkoder.färga_och_skriv(f"Tyvärr, datorn nådde {self.VINNARPOÄNG} poäng först. Försök igen!", Färgkoder.RED)
            return True
        return False

    def spela_omgång(self) -> Optional[str]:
        """
        Spelar en enskild omgång där spelarens och datorns val jämförs.
        
        :return: Vinnaren av omgången eller None om spelaren valt att sluta.
        """
        spelare_val = self.hämta_spelarens_val()
        if spelare_val is None:
            return None

        datorn_val = self.datorns_val()
        Färgkoder.färga_och_skriv(f"Datorn valde: {datorn_val.value.capitalize()}", Färgkoder.BLUE)

        vinnare = self.bestäm_vinnare(spelare_val, datorn_val)
        return vinnare

    def hantera_omgång(self) -> bool:
        """
        Hanterar resultatet av en spelomgång, uppdaterar poängen och kontrollerar om spelet är över.
        
        :return: False om spelet är slut, annars True.
        """
        vinnare = self.spela_omgång()
        if vinnare is None:
            return False
        
        if vinnare == "Oavgjort":
            Färgkoder.färga_och_skriv("Det blev oavgjort! 😐", Färgkoder.DRAW_TEXT)
        elif vinnare == "Spelare":
            Färgkoder.färga_och_skriv("Du vann! 🎉", Färgkoder.WIN_TEXT)
            self.spelarens_poäng += 1
            self.high_score = max(self.high_score, self.spelarens_poäng)
        else:
            Färgkoder.färga_och_skriv("Datorn vann! 😢", Färgkoder.LOSE_TEXT)
            self.datorns_poäng += 1
        
        self.visa_poäng()
        return not self.kontrollera_vinst()

    def spela(self):
        """Huvudfunktion som kör spelets huvudflöde."""
        self.visa_introduktion()
        self.spela_tills_vinnare_utses()
        self.visa_slutresultat()
        self.spela_igen()

    def spela_tills_vinnare_utses(self):
        """Kör omgångar tills någon vinner spelet."""
        while self.hantera_omgång():
            pass

    def visa_slutresultat(self):
        """Visar spelets slutresultat när någon vinner."""
        Färgkoder.färga_och_skriv(f"Slutresultat: Spelare {self.spelarens_poäng} - {self.datorns_poäng} Datorn", Färgkoder.BLUE)

    def spela_igen(self):
        """
        Frågar spelaren om de vill spela igen och startar om spelet om svaret är ja.
        Avslutar spelet om svaret är nej.
        """
        while True:
            svar = input(Färgkoder.färga("Vill du spela igen? (ja/nej): ", Färgkoder.YELLOW)).lower()
            if svar in ['ja', 'j']:
                self.nollställ_poäng()  # Nollställ poängen om spelaren vill spela igen
                self.spela()
                break
            elif svar in ['nej', 'n']:
                Färgkoder.färga_och_skriv("Tack för att du spelade! Ha en bra dag!", Färgkoder.BLUE)
                break
            else:
                Färgkoder.färga_och_skriv("Ogiltigt val, skriv 'ja' eller 'nej'.", Färgkoder.RED)

    def nollställ_poäng(self):
        """Nollställer poängen inför ett nytt spel."""
        self.spelarens_poäng = 0
        self.datorns_poäng = 0

if __name__ == "__main__":
    spel = StenSaxPåse()
    spel.spela()
