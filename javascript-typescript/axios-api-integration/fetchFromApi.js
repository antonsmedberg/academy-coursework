const axios = require('axios');
const { analyzeResponseStructure, logError } = require('./responseLogger');

/**
 * Asynkron funktion för att hämta data från ett API.
 * @param {string} url - URL till API:et som ska anropas.
 * @returns {Promise<Object>} - Returnerar ett löfte som löser sig till API-svaret.
 * @throws {Error} - Kastar ett fel om något går fel vid anropet.
 */
async function fetchFromAPI(url = "https://api.coindesk.com/v1/bpi/currentprice.json") {
  try {
    // Konfigurerar Axios med en timeout på 5 sekunder.
    const config = {
      timeout: 5000
    };

    // Skickar en GET-förfrågan till det angivna API:et.
    const response = await axios.get(url, config);

    // Loggar och analyserar API-responsstrukturen.
    analyzeResponseStructure(response.data);

    // Returnerar API-svaret.
    return response.data;
  } catch (err) {
    // Loggar ett felmeddelande om något går fel vid anropet.
    logError('Error fetching data from API', err);
    // Kastar vidare felet för att hanteras av anroparen.
    throw err;
  }
}

module.exports = { fetchFromAPI };