import pygame
import random
import sys
from GameOverScreen import game_over_screen
from VictoryScreen import victory_screen
from utils import wait_for_key  # Keep only the used imports from utils.py

# Skärmkonstanter
WIDTH, HEIGHT = 800, 600
FPS = 60
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)
RED = (255, 0, 0)
YELLOW = (255, 255, 0)
GRAY = (169, 169, 169)
ORANGE = (255, 165, 0)
LIGHT_BLUE = (173, 216, 230)
SHIP_GREEN = (0, 255, 0)
GRASS_GREEN = (0, 100, 0)
STAR_COUNT = int(WIDTH * HEIGHT * 0.001)
FONT_SIZE = 30

# Globala spelvariabler
main_screen, clock = None, None
spacerocket, cow, stars = None, None, None
player_rect, player_speed, font, grass_rect = None, 5, None, None
score, player_lives, level_complete, running, game_started = 0, 3, False, True, False
targets, space_pressed, current_level, countdown_timer, current_score = [], False, 1, 60, 0
target_spawn_counter, TARGET_SPAWN_RATE, level_colors = 0, 120 - (current_level * 10), []


# Laddar bilder
def load_images():
    global spacerocket, cow
    spacerocket = pygame.image.load("spaceship.png")
    cow = pygame.image.load("cow.png")
    spacerocket = pygame.transform.scale(spacerocket, (50, 50))
    cow = pygame.transform.scale(cow, (40, 40))


# Skapar spelvärlden
def create_window():
    global main_screen, clock
    main_screen = pygame.display.set_mode((WIDTH, HEIGHT))
    pygame.display.set_caption("Spelets Startsida")
    clock = pygame.time.Clock()


# Initialiserar stjärnor för bakgrunden
def init_stars():
    global stars
    stars = [{'x': random.randint(0, WIDTH), 'y': random.randint(0, HEIGHT), 'size': random.randint(1, 3),
              'color': LIGHT_BLUE} for _ in range(STAR_COUNT)]


# Initialiserar spelobjekten
def init_game_objects():
    global grass_rect, player_rect, player_speed, font

    grass_rect = pygame.Rect(0, HEIGHT - 40, WIDTH, 40)
    player_rect = pygame.Rect(WIDTH // 2 - 25, HEIGHT - 60, 50, 50)
    player_speed = 5

    # Initialize font
    font = pygame.font.Font(None, 36)


# Initialiserar variabler för spelet
def init_game_variables():
    global score, player_lives, level_complete, running, game_started
    global targets, space_pressed, current_level, countdown_timer, current_score
    global target_spawn_counter, TARGET_SPAWN_RATE, level_colors

    score = 0
    player_lives = 3
    level_complete = False
    running = True
    game_started = False
    targets = []
    space_pressed = False
    current_level = 1
    countdown_timer = 60
    current_score = 0
    target_spawn_counter = 0
    TARGET_SPAWN_RATE = max(30, 120 - (current_level * 10))
    level_colors = [LIGHT_BLUE, ORANGE, RED, YELLOW, GRAY, (0, 255, 0), (255, 0, 255), (0, 255, 255), (255, 165, 0),
                    (128, 0, 128)]


def start_screen(screen_to_use):
    """
    Visar startskärmen för spelet.
    Presenterar introduktionstext och väntar på användarens tangenttryckning för att påbörja spelet.
    """
    global font
    font = pygame.font.Font(None, FONT_SIZE)
    screen_to_use.fill(BLACK)
    intro_text = get_intro_text()

    y_position = HEIGHT // 4
    for line in intro_text:
        text = font.render(line, True, WHITE)
        text_rect = text.get_rect(center=(WIDTH // 2, y_position))
        screen_to_use.blit(text, text_rect)
        y_position += FONT_SIZE

    pygame.display.flip()
    wait_for_key()


def get_intro_text():
    """
    Skapar och returnerar introduktionstexten för spelet.
    """
    return [
        "Välkommen, Alien Abductor!",
        # ... Fortsättning på texten ...
        "Tryck på valfri tangent för att starta spelet...",
    ]


def handle_events():
    """
    Hanterar användarinput och händelser, inklusive tangenttryckningar och fönsterhändelser.
    """
    global running, player_rect, space_pressed, game_started
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        elif event.type == pygame.KEYDOWN:
            if not game_started:
                game_started = True  # Startar spelet
                continue
            if event.key == pygame.K_SPACE:
                space_pressed = True
        elif event.type == pygame.KEYUP and event.key == pygame.K_SPACE:
            space_pressed = False


def update_game():
    """
    Uppdaterar spelets tillstånd, inklusive spelarens rörelse, mål, och kollisioner.
    """
    global player_rect, targets, target_spawn_counter, score, player_lives, stars, level_colors, current_level

    # Kontrollerar om viktiga variabler är initialiserade
    if player_rect is None or stars is None or level_colors is None:
        return

    # Hanterar spelarens rörelse
    keys = pygame.key.get_pressed()
    player_rect.x += (keys[pygame.K_RIGHT] - keys[pygame.K_LEFT]) * player_speed
    player_rect.y += (keys[pygame.K_DOWN] - keys[pygame.K_UP]) * player_speed

    # Håller spelaren inom skärmgränserna
    player_rect.x = max(0, min(player_rect.x, WIDTH - player_rect.width))
    player_rect.y = max(0, min(player_rect.y, HEIGHT - player_rect.height))

    # Skapar nya mål baserat på räknaren
    target_spawn_counter += 1
    if target_spawn_counter >= TARGET_SPAWN_RATE:
        target_rect = pygame.Rect(random.randint(0, WIDTH - 20), HEIGHT - 50, 50, 50)
        targets.append(target_rect)
        target_spawn_counter = 0

    # Uppdaterar stjärnornas animation och färg för nuvarande nivå
    for star in stars:
        star['size'] += 0.05
        if star['size'] > 3:
            star['size'] = 1
        star['color'] = level_colors[current_level - 1]


def draw_game():
    global main_screen, spacerocket, cow, targets, font, score, current_score

    if font is None:
        # Skip drawing text or initialize font here
        return

    """
    Ritar ut spelets alla grafiska element på skärmen.
    """
    # Ensure that necessary variables are initialized
    if main_screen is None or spacerocket is None or cow is None or targets is None or font is None:
        return

    main_screen.fill(BLACK)
    main_screen.blit(spacerocket, player_rect)

    for target in targets:
        main_screen.blit(cow, target)

    # Rita traktorstrålen när mellanslag trycks ner
    if space_pressed:
        tractor_beam_rect = pygame.Rect(player_rect.centerx - 2, player_rect.centery, 4, HEIGHT - player_rect.centery)
        pygame.draw.line(main_screen, YELLOW, (player_rect.centerx, player_rect.centery), (player_rect.centerx, HEIGHT),
                         2)

        for target in targets[:]:
            if tractor_beam_rect.colliderect(target):
                pygame.draw.line(main_screen, YELLOW, (player_rect.centerx, player_rect.centery),
                                 (player_rect.centerx, target.bottom), 2)
                pygame.draw.rect(main_screen, RED, target)
                targets.remove(target)
                current_score += 1
                score += 1

    # Rita UI-element
    info_line_y = 10
    info_spacing = 75

    # Poäng
    score_text = font.render(f"Poäng: {score}", True, WHITE)
    score_rect = score_text.get_rect(topleft=(10, info_line_y))
    pygame.draw.rect(main_screen, ORANGE, score_rect.inflate(10, 5))
    main_screen.blit(score_text, score_rect)

    # Nivåindikator
    level_text = font.render(f"Nivå: {current_level}", True, WHITE)
    level_rect = level_text.get_rect(topleft=(score_rect.topright[0] + info_spacing, info_line_y))
    pygame.draw.rect(main_screen, LIGHT_BLUE, level_rect.inflate(10, 5))
    main_screen.blit(level_text, level_rect)

    # Nedräkningstimer
    timer_text = font.render(f"Tid: {int(countdown_timer)}", True, WHITE)
    timer_rect = timer_text.get_rect(topleft=(level_rect.topright[0] + info_spacing, info_line_y))
    pygame.draw.rect(main_screen, RED, timer_rect.inflate(10, 5))
    main_screen.blit(timer_text, timer_rect)

    pygame.display.flip()


# Huvudspelslingan
def main_game_loop():
    global running, score, player_lives, level_complete, main_screen, clock
    if main_screen is None or clock is None:
        print("Error: Game not properly initialized")
        return

    while running:
        clock.tick(FPS)
        handle_events()
        update_game()
        draw_game()
        check_game_status()


# Funktion för att kontrollera spelets status
def check_game_status():
    global running, score, player_lives, level_complete
    if player_lives <= 0:
        game_over_screen(main_screen, score)
        running = False
    elif level_complete:
        victory_screen(main_screen, score)
        running = False


# Startar spelet
if __name__ == "__main__":
    pygame.init()
    load_images()
    create_window()
    init_stars()
    init_game_objects()
    init_game_variables()
    start_screen(main_screen)
    main_game_loop()
    pygame.quit()
