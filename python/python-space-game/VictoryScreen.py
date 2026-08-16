import pygame
from utils import show_text_on_screen, wait_for_key

HEIGHT = 600  # Assuming HEIGHT is defined here


def victory_screen(game_screen, score):
    """
    Visar en 'Victory'-skärm.
    :param game_screen: Pygame-fönstret där texten ska visas.
    :param score: Spelarens poäng som ska visas.
    """
    game_screen.fill((0, 0, 0))  # Fyller skärmen med svart
    show_text_on_screen(game_screen, "Grattis! Du vann!", 50, HEIGHT // 3)
    show_text_on_screen(game_screen, f"Ditt slutresultat: {score}", 30, HEIGHT // 2)
    show_text_on_screen(game_screen, "Tryck på valfri tangent för att fortsätta...", 20, HEIGHT * 2 // 3)
    pygame.display.flip()
    wait_for_key()
