import pygame
import sys


def show_text_on_screen(game_screen, text, font_size, y_pos):
    """
    Visar text på skärmen.
    :param game_screen: Pygame-fönstret där texten ska visas.
    :param text: Texten som ska visas.
    :param font_size: Storlek på texten.
    :param y_pos: Y-position för texten.
    """
    font = pygame.font.Font(None, font_size)
    text_surface = font.render(text, True, (255, 255, 255))
    text_rect = text_surface.get_rect(center=(game_screen.get_width() // 2, y_pos))
    game_screen.blit(text_surface, text_rect)


def wait_for_key():
    """
    Väntar på att användaren trycker på en tangent.
    Avslutar spelet om användaren stänger fönstret.
    """
    running = True
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            elif event.type == pygame.KEYDOWN:
                running = False
