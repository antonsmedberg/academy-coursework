'use strict'; // Strikt läge för säkrare kod

// 
// DOM-element (grupperat i objekt)
// 
const DOM = {
  setupSection: document.getElementById('setup-section'),
  nameForm: document.getElementById('name-form'),
  nameInput: document.getElementById('player-name-input'),

  gameSection: document.getElementById('game-section'),
  playerNameDisplay: document.getElementById('display-player-name'),
  totalScoreDisplay: document.getElementById('display-total-score'),
  roundScoreDisplay: document.getElementById('display-round-score'),
  roundsPlayedDisplay: document.getElementById('display-rounds-played'),
  dice: document.getElementById('dice'),
  diceValueDisplay: document.getElementById('dice-value'),
  roundMessage: document.getElementById('round-message'),

  rollBtn: document.getElementById('roll-btn'),
  holdBtn: document.getElementById('hold-btn'),

  winMessageContainer: document.getElementById('win-message'),
  winText: document.getElementById('win-text'),
  newGameBtn: document.getElementById('new-game-btn'),
};

// 
// Speldata
// 
const state = {
  playerName: '',
  totalScore: 0,
  roundScore: 0,
  roundsPlayed: 0,
  isGameActive: false,
  winningScore: 100,
};

// 
// Initiera / återställ spelet
// 
function initializeGame() {
  state.totalScore = 0;
  state.roundScore = 0;
  state.roundsPlayed = 1;
  state.isGameActive = true;

  DOM.playerNameDisplay.textContent = state.playerName;
  DOM.diceValueDisplay.textContent = '-';
  DOM.roundMessage.textContent = '';
  DOM.roundMessage.classList.remove('soft-danger');
  DOM.diceValueDisplay.classList.remove('danger');

  DOM.winMessageContainer.classList.add('hidden');
  DOM.rollBtn.classList.remove('hidden');
  DOM.holdBtn.classList.remove('hidden');

  updateDisplay();
}

// 
// Uppdatera poäng & omgångsvisning
// 
function updateDisplay() {
  DOM.totalScoreDisplay.textContent = state.totalScore;
  DOM.roundScoreDisplay.textContent = state.roundScore;
  DOM.roundsPlayedDisplay.textContent = state.roundsPlayed;
}

// 
// Kasta tärningen
// 
function rollDice() {
  if (!state.isGameActive) return;

  const diceRoll = Math.floor(Math.random() * 6) + 1;

  const diceRotations = {
    1: [0, 0],
    2: [-90, 0],
    3: [0, 90],
    4: [0, -90],
    5: [90, 0],
    6: [180, 0],
  };

  const [xDeg, yDeg] = diceRotations[diceRoll];
  const extraSpins = (Math.floor(Math.random() * 4) + 2) * 360;
  DOM.dice.style.transform = `rotateX(${xDeg + extraSpins}deg) rotateY(${yDeg + extraSpins}deg)`;

  setTimeout(() => {
    DOM.diceValueDisplay.textContent = diceRoll;

    if (diceRoll === 1) {
      state.roundScore = 0;
      DOM.roundMessage.textContent = 'Du kastade en 1:a! Omgången är avslutad.';
      DOM.roundMessage.classList.add('soft-danger');
      DOM.diceValueDisplay.classList.add('danger');
      endRound();
    } else {
      state.roundScore += diceRoll;
      DOM.roundMessage.textContent = '';
      DOM.roundMessage.classList.remove('soft-danger');
      DOM.diceValueDisplay.classList.remove('danger');
      updateDisplay();
    }
  }, 1600);
}

// 
// Håll poäng (frys omgång)
// 
function holdScore() {
  if (!state.isGameActive || state.roundScore === 0) return;

  state.totalScore += state.roundScore;

  if (state.totalScore >= state.winningScore) {
    displayWinner();
  } else {
    endRound();
  }
}

// 
// Avsluta omgång
// 
function endRound() {
  state.roundScore = 0;
  state.roundsPlayed++;
  updateDisplay();
}

// 
// Visa vinstmeddelande
// 
function displayWinner() {
  state.isGameActive = false;

  DOM.winText.textContent = `Grattis ${state.playerName}! Du nådde ${state.totalScore} poäng på ${state.roundsPlayed} omgångar!`;
  DOM.winMessageContainer.classList.remove('hidden');
  DOM.rollBtn.classList.add('hidden');
  DOM.holdBtn.classList.add('hidden');
  DOM.diceValueDisplay.textContent = '🏆';
}

// 
// Händelser
// 
DOM.nameForm.addEventListener('submit', (event) => {
  event.preventDefault();
  state.playerName = DOM.nameInput.value.trim() || 'Spelare';

  DOM.setupSection.classList.add('hidden');
  DOM.setupSection.classList.remove('active-section');
  DOM.gameSection.classList.remove('hidden');
  DOM.gameSection.classList.add('active-section');

  initializeGame();
});

DOM.rollBtn.addEventListener('click', rollDice);
DOM.holdBtn.addEventListener('click', holdScore);

DOM.newGameBtn.addEventListener('click', () => {
  DOM.gameSection.classList.add('hidden');
  DOM.gameSection.classList.remove('active-section');
  DOM.setupSection.classList.remove('hidden');
  DOM.setupSection.classList.add('active-section');
  DOM.nameInput.value = '';
});

// 
// När sidan laddas
// 
window.addEventListener('load', () => {
  DOM.setupSection.classList.add('active-section');
  DOM.setupSection.classList.remove('hidden');
  DOM.gameSection.classList.remove('active-section');
  DOM.gameSection.classList.add('hidden');
  DOM.winMessageContainer.classList.add('hidden');
});