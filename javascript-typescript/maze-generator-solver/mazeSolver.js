document.addEventListener('DOMContentLoaded', () => {
    const mazeSize = 10;
    let maze = generateMaze(mazeSize, mazeSize); // Genererar en 10x10 labyrint
    const start = [0, 0];
    const end = [mazeSize - 1, mazeSize - 1];

    drawMaze(maze);
    setupControls(maze, start, end);
    enableCellEditing(maze);
});

// Genererar en labyrint med givna dimensioner
function generateMaze(rows, cols) {
    const maze = Array.from({ length: rows }, () => Array(cols).fill(0));

    // Exempel: Lägger till väggar dynamiskt
    for (let i = 0; i < rows; i++) {
        for (let j = 0; j < cols; j++) {
            if (Math.random() > 0.7) { // 30% chans att en cell blir en vägg
                maze[i][j] = 1;
            }
        }
    }

    // Gör start och slutceller fria
    maze[0][0] = 0;
    maze[rows - 1][cols - 1] = 0;

    return maze;
}

// Uppdaterar statusmeddelande
function setStatus(message) {
    const statusElement = document.getElementById('status');
    if (statusElement) {
        statusElement.textContent = message;
    }
}

// Sätter upp kontroller för lös och återställ knappar
function setupControls(maze, start, end) {
    const solveButton = document.getElementById('solveButton');
    const resetButton = document.getElementById('resetButton');

    solveButton?.addEventListener('click', () => {
        findPathInMaze(maze, start, end).catch(error => {
            console.error("Error finding path:", error);
        });
    });

    resetButton?.addEventListener('click', () => {
        maze = generateMaze(maze.length, maze[0].length); // Genererar en ny labyrint vid återställning
        drawMaze(maze);
        setStatus('Klicka på "Lös Labyrint" för att starta.');
        enableCellEditing(maze);
    });
}

// Ritar labyrinten
function drawMaze(maze) {
    const mazeContainer = document.getElementById('maze');
    if (!mazeContainer) return;

    mazeContainer.className = 'maze-container';
    mazeContainer.innerHTML = '';
    mazeContainer.style.gridTemplateColumns = `repeat(${maze[0].length}, 50px)`;

    maze.forEach((row, x) => {
        row.forEach((cell, y) => {
            const cellDiv = document.createElement('div');
            cellDiv.id = `${x}-${y}`;
            cellDiv.className = `cell ${cell === 1 ? 'wall' : 'unvisited'}`;
            mazeContainer.appendChild(cellDiv);
        });
    });
}

// Möjliggör redigering av celler genom klick
function enableCellEditing(maze) {
    document.querySelectorAll('.cell').forEach(cell => {
        cell.addEventListener('click', () => {
            const [x, y] = cell.id.split('-').map(Number);
            if (maze[x][y] === 0) {
                maze[x][y] = 1;
                cell.className = 'cell wall';
            } else {
                maze[x][y] = 0;
                cell.className = 'cell unvisited';
            }
        });
    });
}

// Söker efter en väg genom labyrinten
async function findPathInMaze(maze, start, end) {
    try {
        const paths = new Map();
        let queue = [start];
        paths.set(start.toString(), [start]);
        markCell(start, 'start');
        setStatus('Söker efter väg...');

        while (queue.length) {
            const [x, y] = queue.shift();
            await new Promise(resolve => setTimeout(resolve, 50)); // Sätter timeout för visualisering

            for (const [dx, dy] of [[1, 0], [-1, 0], [0, 1], [0, -1]]) {
                const nx = x + dx, ny = y + dy;
                if (isValidCell(nx, ny, maze) && maze[nx][ny] === 0 && !paths.has(`${nx},${ny}`)) {
                    paths.set(`${nx},${ny}`, [...paths.get(`${x},${y}`), [nx, ny]]);
                    queue.push([nx, ny]);
                    markCell([nx, ny], 'visited');

                    if (nx === end[0] && ny === end[1]) {
                        drawPath(paths.get(`${nx},${ny}`));
                        setStatus('Labyrinten löst!');
                        return;
                    }
                }
            }
        }
        setStatus('Ingen väg hittades.');
    } catch (error) {
        console.error('Fel vid bearbetning av labyrinten:', error);
        setStatus('Ett fel uppstod.');
    }
}

// Validerar om en cell är inom gränserna för labyrinten
function isValidCell(x, y, maze) {
    return x >= 0 && y >= 0 && x < maze.length && y < maze[0].length;
}

// Markerar en cell med en viss klass
function markCell([x, y], className) {
    const cell = document.getElementById(`${x}-${y}`);
    if (cell) {
        cell.className = `cell ${className}`;
    }
}

// Ritar den hittade vägen genom labyrinten
function drawPath(path) {
    path.forEach(pos => markCell(pos, 'path'));
}

