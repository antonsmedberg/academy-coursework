const chalk = require('chalk'); // Använd Chalk för färgade loggar

/**
 * Analysera och logga strukturen på API-responsen.
 * @param {Object} data - Data från API-responsen.
 */
function analyzeResponseStructure(data) {
  console.log(chalk.blue("Analyzing API response structure:"));
  console.log(chalk.green(JSON.stringify(data, null, 2)));

  // Kontrollera och logga nycklar på översta nivån
  const keys = Object.keys(data);
  console.log(chalk.blue("Top-level keys:"), chalk.yellow(keys.join(', ')));

  // Rekursiv funktion för att gå igenom hela strukturen
  function traverseObject(obj, path = '') {
    for (let key in obj) {
      if (obj.hasOwnProperty(key)) {
        const newPath = path ? `${path}.${key}` : key;
        if (typeof obj[key] === 'object' && obj[key] !== null) {
          traverseObject(obj[key], newPath);
        } else {
          console.log(chalk.blue(`Key: ${newPath}, Value: ${obj[key]}`));
        }
      }
    }
  }

  // Börja traversal från rotobjektet
  traverseObject(data);
}

/**
 * Logga felmeddelanden på ett mer strukturerat sätt.
 * @param {string} message - Felmeddelandetext.
 * @param {Error} error - Felobjektet.
 */
function logError(message, error) {
  console.error(chalk.red(`${message}:`), error.message);
}

module.exports = { analyzeResponseStructure, logError };