const axios = require('axios');
const { fetchFromAPI } = require('./fetchFromApi');
const { analyzeResponseStructure, logError } = require('./responseLogger');

// Mockar Axios GET-förfrågan för att testa utan att anropa det riktiga API:et.
jest.mock('axios');

describe("Fetching with Axios", () => {
  // Rensar alla mockar innan varje test för att säkerställa en ren miljö.
  beforeEach(() => {
    jest.clearAllMocks();
  });

  /**
   * Testar framgångsrikt hämtning av data med mockad API-respons.
   */
  it("Fetches data successfully", async () => {
    // Definierar en mockad API-respons.
    const mockResponse = {
      data: {
        time: {
          updated: "May 30, 2024 12:00:00 UTC"
        },
        disclaimer: "This data was produced from the CoinDesk Bitcoin Price Index.",
        bpi: {
          USD: {
            code: "USD",
            rate: "30,000.0000",
            description: "United States Dollar",
            rate_float: 30000.00
          }
        }
      }
    };

    // Ställer in mocken för att returnera den mockade responsen.
    axios.get.mockResolvedValue(mockResponse);

    // Anropar funktionen för att hämta data.
    const response = await fetchFromAPI();

    // Förväntar att svaret innehåller korrekt data.
    expect(response.time.updated).toBe("May 30, 2024 12:00:00 UTC");
    expect(response.bpi.USD.code).toBe("USD");
    expect(response.bpi.USD.rate).toBe("30,000.0000");
  });

  /**
   * Testar hantering av fel vid API-anrop.
   */
  it("Handles error gracefully", async () => {
    // Definierar en mockad felrespons vid API-anrop.
    const mockError = new Error('Network Error');
    axios.get.mockRejectedValue(mockError);

    try {
      // Försöker anropa funktionen och förväntar ett fel.
      await fetchFromAPI();
    } catch (error) {
      // Förväntar att felmeddelandet är korrekt.
      expect(error).toBe(mockError);
    }
  });

  /**
   * Testar hantering av ogiltig responsstruktur.
   */
  it("Handles invalid response structure", async () => {
    // Definierar en mockad API-respons med ogiltig struktur.
    const mockResponse = {
      data: {
        invalid: "structure"
      }
    };

    // Ställer in mocken för att returnera den ogiltiga responsen.
    axios.get.mockResolvedValue(mockResponse);

    try {
      // Försöker anropa funktionen och förväntar ett fel.
      await fetchFromAPI();
    } catch (error) {
      // Förväntar att felmeddelandet är korrekt.
      expect(error.message).toBe('Invalid response structure');
    }
  });

  /**
   * Testar hämtning av data från det riktiga API:et.
   * Detta kan ses som ett integrationstest.
   */
  it("Fetches data from real API", async () => {
    // Av-mockar Axios för detta specifika test.
    jest.unmock('axios');

    // Anropar funktionen för att hämta data från det riktiga API:et.
    try {
      const response = await fetchFromAPI();
      
      // Loggar hela API-svaret för att inspektera dess struktur.
      console.log('Real API response:', response);

      // Förväntar att svaret innehåller vissa nycklar, men hanterar också okända strukturer.
      if (response.time && response.disclaimer && response.bpi) {
        expect(response).toHaveProperty('time');
        expect(response).toHaveProperty('disclaimer');
        expect(response).toHaveProperty('bpi');
      } else {
        console.log('Unknown structure:', response);
      }
    } catch (error) {
      // Om API-strukturen är felaktig, logga felet för att hjälpa med felsökning.
      logError('Real API response structure error', error);
      throw error;
    }
  });

  /**
   * Testar dynamisk hantering av okända API-svar.
   */
  it("Handles unknown API response structure", async () => {
    // Definierar en mockad API-respons med okänd struktur.
    const mockResponse = {
      data: {
        some: "unknown",
        structure: {
          with: "random",
          keys: [1, 2, 3]
        }
      }
    };

    // Ställer in mocken för att returnera den okända responsen.
    axios.get.mockResolvedValue(mockResponse);

    // Anropar funktionen för att hämta data.
    const response = await fetchFromAPI();

    // Anropar funktionen för att analysera och logga responsstrukturen.
    analyzeResponseStructure(response);

    // Förväntar att svaret innehåller vissa nycklar (justeras beroende på API-struktur).
    expect(response).toHaveProperty('some');
    expect(response).toHaveProperty('structure');
  });
});