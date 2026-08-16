import pygame
from utils import show_text_on_screen, wait_for_key

HEIGHT = 600


def game_over_screen(game_screen, score):
    """
    Visar en 'Game Over'-skärm.
    :param game_screen: Pygame-fönstret där texten ska visas.
    :param score: Spelarens poäng som ska visas.
    """
    game_screen.fill((0, 0, 0))
    show_text_on_screen(game_screen, "Game Over", 50, HEIGHT // 3)
    show_text_on_screen(game_screen, f"Ditt slutresultat: {score}", 30, HEIGHT // 2)
    show_text_on_screen(game_screen, "Tryck på valfri tangent för att avsluta...", 20, HEIGHT * 2 // 3)
    pygame.display.flip()
    wait_for_key()


if __name__ == "__main__":
    pygame.init()
    main_screen = pygame.display.set_mode((800, HEIGHT))
    game_over_screen(main_screen, 100)
