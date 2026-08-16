// src/components/Admin/DatabaseSeeder.jsx
import React, { useState, useEffect } from "react";
import {
  seedDatabase,
  testDatabaseConnection,
  clearDatabase,
  checkIfExampleDataExists,
} from "../../firebase/seedDatabase";
import { useAuth } from "../../hooks/useAuth.jsx";
import {
  DatabaseIcon,
  ChevronDownIcon,
  ChevronRightIcon,
  LoadingIcon,
  CheckIcon,
  AlertIcon,
  SearchIcon,
  DatabasePlusIcon,
  TrashIcon,
} from "../UI/Icons";
import "../../styles/components/Button.css";

/**
 * Databasverktyg - hantering av testdata
 *
 * Kompakt verktyg för att hantera testdata i utvecklingsmiljön.
 * Visar databasanslutningsstatus och låter användaren fylla eller
 * rensa databasen med ett klick.
 *
 * @param {Function} onSuccess - Callback för statusmeddelanden
 */
const DatabaseSeeder = ({ onSuccess }) => {
  const [loading, setLoading] = useState(false);
  const [connectionStatus, setConnectionStatus] = useState("checking");
  const [isExpanded, setIsExpanded] = useState(false);
  const [hasExampleData, setHasExampleData] = useState(false);

  // Hämta autentiseringsstatus för att kontrollera om användaren är inloggad
  const { currentUser } = useAuth();

  // Kollar om databasen fungerar och om exempeldata finns direkt när sidan laddas
  useEffect(() => {
    // Bara kör databaskontrollen om användaren är inloggad
    if (!currentUser) {
      setConnectionStatus("error");
      return;
    }

    // Förhindra flera samtidiga anrop genom att använda en flagga
    let isCheckingConnection = false;

    const checkConnection = async () => {
      // Undvik flera samtidiga anrop
      if (isCheckingConnection) {
        console.log("Databasanslutning kontrolleras redan, hoppar över...");
        return;
      }

      isCheckingConnection = true;

      try {
        const result = await testDatabaseConnection();
        setConnectionStatus(result.success ? "connected" : "error");

        if (!result.success) {
          onSuccess(
            `Fel vid anslutning till databasen: ${result.error}`,
            "error"
          );
        } else {
          // Kontrollera om exempeldata redan finns - bara om användaren är inloggad
          if (currentUser) {
            try {
              const storedFlag = localStorage.getItem("exampleDataAdded");
              if (storedFlag === "true") {
                // Dubbelkolla mot databasen för att vara säker
                const dataExists = await checkIfExampleDataExists();
                setHasExampleData(dataExists);
              } else {
                // Om ingen flagga finns, kolla ändå databasen
                const dataExists = await checkIfExampleDataExists();
                setHasExampleData(dataExists);
              }
            } catch (dataError) {
              // Om det blir fel vid kontroll av exempeldata, sätt bara till false
              console.log(
                "Kunde inte kontrollera exempeldata, sätter till false"
              );
              setHasExampleData(false);
            }
          } else {
            // Om användaren inte är inloggad, sätt bara hasExampleData till false
            setHasExampleData(false);
          }
        }
      } catch (error) {
        setConnectionStatus("error");
        onSuccess(
          `Fel vid test av databasanslutning: ${error.message}`,
          "error"
        );
      } finally {
        isCheckingConnection = false;
      }
    };

    // Lägg till en liten fördröjning för att undvika race conditions
    const timeoutId = setTimeout(checkConnection, 100);

    return () => {
      clearTimeout(timeoutId);
      isCheckingConnection = false;
    };
  }, [currentUser]); // BORTTAGET: onSuccess från dependencies för att undvika loopar

  /**
   * Fyller databasen med testdata
   *
   * Skapar exempeldata med teammedlemmar och uppgifter
   * för att demonstrera applikationens funktionalitet.
   */
  const handleSeedDatabase = async () => {
    // Bekräfta åtgärden
    const confirmSeed = window.confirm(
      "Lägg till testdata i databasen?\n\n" +
        "Detta kommer att fylla databasen med exempeldata för team och uppgifter.\n\n" +
        "OBS! Befintlig data kommer att ersättas. Vill du fortsätta?"
    );

    if (!confirmSeed) return;

    setLoading(true);
    try {
      // Visa statusmeddelande
      onSuccess("Skapar testdata...", "info");

      // Kör funktionen som rensar och fyller databasen
      const result = await seedDatabase();

      if (result.success) {
        // Uppdatera status
        setHasExampleData(true);

        // Visa bekräftelse
        onSuccess(
          "Klart! Databasen har fyllts med testdata för team och uppgifter.",
          "success"
        );
      } else {
        onSuccess(`Ett fel uppstod: ${result.error}`, "error");
      }
    } catch (error) {
      console.error("Fel vid seedDatabase:", error);
      onSuccess(`Ett oväntat fel uppstod: ${error.message}`, "error");
    } finally {
      setLoading(false);
    }
  };

  /**
   * Rensar databasen
   *
   * Tar bort all data från databasen för att starta om
   * med en tom databas.
   */
  const handleClearDatabase = async () => {
    // Bekräfta åtgärden
    const confirmClear = window.confirm(
      "Rensa all data från databasen?\n\n" +
        "Detta kommer att ta bort alla team och uppgifter från databasen.\n\n" +
        "Det går inte att ångra denna åtgärd. Vill du fortsätta?"
    );

    if (!confirmClear) return;

    setLoading(true);
    try {
      // Visa statusmeddelande
      onSuccess("Rensar databasen...", "info");

      // Kör funktionen som rensar databasen
      const result = await clearDatabase();

      if (result.success) {
        // Uppdatera status
        setHasExampleData(false);

        // Visa bekräftelse
        onSuccess("Klart! Databasen har rensats och är nu tom.", "success");
      } else {
        onSuccess(`Ett fel uppstod: ${result.error}`, "error");
      }
    } catch (error) {
      console.error("Fel vid clearDatabase:", error);
      onSuccess(`Ett oväntat fel uppstod: ${error.message}`, "error");
    } finally {
      setLoading(false);
    }
  };

  // Växla mellan expanderat och kompakt läge
  const toggleExpand = () => {
    setIsExpanded(!isExpanded);
  };

  return (
    <div className={`database-seeder ${isExpanded ? "expanded" : "collapsed"}`}>
      <div className="seeder-header" onClick={toggleExpand}>
        <div className="seeder-header-content">
          <span className="seeder-header-icon">
            <DatabaseIcon size={16} />
          </span>
          <h3>Databasverktyg</h3>
        </div>
        <span className="expand-icon">
          {isExpanded ? (
            <ChevronDownIcon size={16} />
          ) : (
            <ChevronRightIcon size={16} />
          )}
        </span>
      </div>

      <div className="seeder-content">
        <div className="seeder-content-inner">
          <div className="status-indicators">
            {/* Databasanslutningsstatus */}
            <div className={`connection-status ${connectionStatus}`}>
              {connectionStatus === "checking" && (
                <>
                  <span className="status-icon">
                    <LoadingIcon size={16} />
                  </span>
                  Ansluter till Firebase...
                </>
              )}
              {connectionStatus === "connected" && (
                <>
                  <span className="status-icon">
                    <CheckIcon size={16} color="#10b981" />
                  </span>
                  Ansluten till Firebase-databasen
                </>
              )}
              {connectionStatus === "error" && (
                <>
                  <span className="status-icon">
                    <AlertIcon size={16} color="#ef4444" />
                  </span>
                  Kunde inte ansluta till Firebase
                </>
              )}
            </div>

            {/* Status för exempeldata */}
            {connectionStatus === "connected" && (
              <div
                className={`example-data-status ${
                  hasExampleData ? "exists" : "none"
                }`}
              >
                <span className="status-icon">
                  <SearchIcon size={16} />
                </span>
                {hasExampleData
                  ? "Testdata finns i databasen"
                  : "Databasen är tom"}
              </div>
            )}
          </div>

          <div className="seeder-description">
            Hantera testdata för din Scrum Board. Fyll databasen med färdiga
            exempel på team och uppgifter för att snabbt komma igång, eller
            rensa allt för att börja om från början.
          </div>

          <div className="seeder-buttons">
            {!hasExampleData && (
              <button
                className="button seed-button"
                onClick={handleSeedDatabase}
                disabled={loading || connectionStatus !== "connected"}
              >
                {loading ? (
                  <>
                    <LoadingIcon size={16} className="loading-spinner" />
                    Skapar testdata...
                  </>
                ) : (
                  <>
                    <DatabasePlusIcon size={16} />
                    Lägg till testdata
                  </>
                )}
              </button>
            )}

            {hasExampleData && (
              <button
                className="button clear-button"
                onClick={handleClearDatabase}
                disabled={loading || connectionStatus !== "connected"}
              >
                {loading ? (
                  <>
                    <LoadingIcon size={16} className="loading-spinner" />
                    Rensar databasen...
                  </>
                ) : (
                  <>
                    <TrashIcon size={16} />
                    Rensa databasen
                  </>
                )}
              </button>
            )}
          </div>

          {connectionStatus === "error" && (
            <div className="connection-error">
              <p>
                <strong>Anslutningsfel</strong> Kunde inte ansluta till
                Firebase-databasen.
              </p>
              <p>Kontrollera följande:</p>
              <ul>
                <li>Är Firebase-projektet korrekt konfigurerat?</li>
                <li>Är databasens URL korrekt i firebaseConfig.jsx?</li>
                <li>Har du rätt behörigheter för databasen?</li>
                <li>
                  Tillåter databasens säkerhetsregler läsning och skrivning?
                </li>
              </ul>
            </div>
          )}

          {connectionStatus === "connected" && (
            <div className="seeder-info">
              <p className="seeder-warning">
                <strong>Observera:</strong> Detta verktyg är endast för
                testning. Befintlig data kommer att ersättas om du lägger till
                testdata eller rensar databasen. Använd med försiktighet.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DatabaseSeeder;
