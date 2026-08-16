// src/firebase/seedDatabase.jsx
// OPTIMERING: Importerar bara använda Firebase Database-funktioner för mindre bundle
import { ref, set, get } from "firebase/database";
import { db } from "./firebaseConfig";

/**
 * Databashjälpare
 *
 * Samling av funktioner för att hantera testdata i databasen.
 * Används för att snabbt kunna demonstrera applikationens funktionalitet
 * utan att behöva skapa data manuellt.
 */

/**
 * Rensar databasen
 *
 * Tar bort all data från databasen för att ge en ren start.
 * Raderar både medlemmar och uppgifter.
 */
export const clearDatabase = async () => {
  try {
    // Rensa alla diagnostik-noder först (nya strukturen)
    await set(ref(db, "_diagnostics"), null);
    console.log("Diagnostik-noder har rensats från databasen!");

    // Rensa gamla test-noder för bakåtkompatibilitet
    await set(ref(db, "_connectionTest"), null);
    console.log("Gamla test-noder har rensats från databasen!");

    // Rensa members
    await set(ref(db, "members"), null);
    console.log("Teammedlemmar har rensats från databasen!");

    // Rensa tasks
    await set(ref(db, "tasks"), null);
    console.log("Uppgifter har rensats från databasen!");

    // Ta bort flaggan från localStorage
    localStorage.removeItem("exampleDataAdded");

    return { success: true, message: "Databasen har rensats!" };
  } catch (error) {
    console.error("Fel vid rensning av databasen:", error);
    return { success: false, error: error.message };
  }
};

/**
 * Fyller databasen med testdata
 *
 * Skapar ett grundläggande team och uppgifter för att
 * demonstrera applikationens funktionalitet.
 */
export const seedDatabase = async () => {
  try {
    // Rensa databasen först
    await clearDatabase();
    // Exempeldata för teammedlemmar - förenklad version med bara 3 medlemmar
    const membersData = {
      member1: {
        name: "Anna Andersson",
        role: "frontend",
        avatar: "https://randomuser.me/api/portraits/women/44.jpg",
      },
      member2: {
        name: "Erik Eriksson",
        role: "backend",
        avatar: "https://randomuser.me/api/portraits/men/32.jpg",
      },
      member3: {
        name: "Maria Svensson",
        role: "ux",
        avatar: "https://randomuser.me/api/portraits/women/68.jpg",
      },
    };

    // Exempeldata för uppgifter
    const tasksData = {
      task1: {
        title: "Skapa login-sida",
        description: "Designa och koda en inloggningssida med formulär",
        status: "Nytt", // Matchar status i useTasks.jsx
        category: "frontend",
        creationTimestamp: Date.now() - 3 * 24 * 60 * 60 * 1000, // 3 dagar sedan
        assignedToMemberId: null, // Viktigt! Måste matcha fältnamnet i useTasks.jsx
        assignedToMemberName: null, // Viktigt! Måste matcha fältnamnet i useTasks.jsx
        isArchived: false, // Viktigt fält som används i useTasks.jsx
      },
      task2: {
        title: "Fixa API-koppling",
        description: "Koppla ihop frontend med backend-API:et",
        status: "Pågående", // Matchar status i useTasks.jsx
        category: "backend",
        creationTimestamp: Date.now() - 1 * 24 * 60 * 60 * 1000, // 1 dag sedan
        assignedToMemberId: "member2", // Viktigt! Måste matcha fältnamnet i useTasks.jsx
        assignedToMemberName: "Erik Eriksson", // Viktigt! Måste matcha fältnamnet i useTasks.jsx
        isArchived: false,
      },
      task3: {
        title: "Förbättra design",
        description:
          "Uppdatera färger och typografi enligt nya designriktlinjer",
        status: "Klar", // Matchar status i useTasks.jsx
        category: "ux",
        creationTimestamp: Date.now() - 5 * 24 * 60 * 60 * 1000, // 5 dagar sedan
        assignedToMemberId: "member3", // Viktigt! Måste matcha fältnamnet i useTasks.jsx
        assignedToMemberName: "Maria Svensson", // Viktigt! Måste matcha fältnamnet i useTasks.jsx
        isArchived: false,
      },
    };

    // Spara teammedlemmar till Firebase - använder samma sökväg som i useMembers.jsx
    console.log(
      "Försöker spara teammedlemmar till sökvägen 'members':",
      membersData
    );

    try {
      const membersRef = ref(db, "members");
      await set(membersRef, membersData);
      console.log("Teammedlemmar har lagts till i databasen!");

      // Verifiera att data har sparats
      const membersSnapshot = await get(membersRef);
      if (membersSnapshot.exists()) {
        console.log(
          "Verifierat att members har sparats:",
          membersSnapshot.val()
        );
      } else {
        console.error("VARNING: members verkar inte ha sparats!");
      }
    } catch (error) {
      console.error("Fel vid sparande av members:", error);
      throw error; // Kasta vidare felet för att avbryta processen
    }

    // Spara uppgifter till Firebase
    console.log("Sparar uppgifter:", tasksData);

    try {
      const tasksRef = ref(db, "tasks");
      await set(tasksRef, tasksData);
      console.log("Uppgifter har lagts till i databasen!");

      // Verifiera sparning
      const tasksSnapshot = await get(tasksRef);
      if (tasksSnapshot.exists()) {
        console.log("Verifierat att tasks har sparats:", tasksSnapshot.val());
      } else {
        console.error("VARNING: tasks verkar inte ha sparats!");
      }
    } catch (error) {
      console.error("Fel vid sparande:", error);
      throw error; // Kasta vidare felet
    }

    // Verifiera att data har sparats
    console.log("Firebase-databas URL:", db.app.options.databaseURL);
    console.log("Verifiering av data slutförd");

    // Spara i localStorage att exempeldata har lagts till
    localStorage.setItem("exampleDataAdded", "true");

    return { success: true, message: "Databasen har fyllts med exempeldata!" };
  } catch (error) {
    console.error("Fel vid initialisering av databasen:", error);
    return { success: false, error: error.message };
  }
};

/**
 * Kontrollerar om databasen är tom
 *
 * Verifierar om det finns data i members- och tasks-noderna.
 * Används för att undvika oavsiktlig överskrivning av data.
 */
export const checkIfDatabaseEmpty = async () => {
  try {
    // Kontrollera om det finns data i members
    const membersRef = ref(db, "members");
    const membersSnapshot = await get(membersRef);

    // Kontrollera om det finns data i tasks
    const tasksRef = ref(db, "tasks");
    const tasksSnapshot = await get(tasksRef);

    // Returnera true om båda är tomma
    return !membersSnapshot.exists() && !tasksSnapshot.exists();
  } catch (error) {
    console.error("Fel vid kontroll av databas:", error);
    return true; // Anta att databasen är tom vid fel
  }
};

/**
 * Kontrollerar om exempeldata finns
 *
 * Verifierar om specifika exempeldata-ID:n finns i databasen.
 * Används för att visa korrekt status i användargränssnittet.
 */
export const checkIfExampleDataExists = async () => {
  try {
    // Kontrollera om specifika exempeldata finns i members
    const member1Ref = ref(db, "members/member1");
    const member1Snapshot = await get(member1Ref);

    // Kontrollera om specifika exempeldata finns i tasks
    const task1Ref = ref(db, "tasks/task1");
    const task1Snapshot = await get(task1Ref);

    // Om både member1 och task1 finns, antar vi att exempeldata har lagts till
    const hasExampleData = member1Snapshot.exists() && task1Snapshot.exists();

    // Spara resultatet i localStorage för att komma ihåg mellan sessioner
    if (hasExampleData) {
      localStorage.setItem("exampleDataAdded", "true");
    }

    return hasExampleData;
  } catch (error) {
    // Om det är ett behörighetsfel (användaren inte inloggad), logga inte som fel
    if (error.code === "permission-denied") {
      console.log(
        "Användaren är inte inloggad, hoppar över kontroll av exempeldata"
      );
      return false;
    }
    console.error("Fel vid kontroll av exempeldata:", error);
    return false; // Anta att exempeldata inte finns vid fel
  }
};

/**
 * Testar databasanslutningen
 *
 * Skriver och läser testdata för diagnostik.
 * Använder unik timestamp för att undvika konflikter.
 */
export const testDatabaseConnection = async () => {
  try {
    // Skapa unik referens för att undvika konflikter
    const uniqueTestId = `connectionTest_${Date.now()}_${Math.random()
      .toString(36)
      .substr(2, 9)}`;
    const testRef = ref(db, `_diagnostics/${uniqueTestId}`);

    // Skriv data
    const testData = {
      timestamp: Date.now(),
      message: "Test connection",
      testId: uniqueTestId,
    };
    await set(testRef, testData);
    // console.log("Testdata skriven till databasen:", testData);

    // Läs data
    const snapshot = await get(testRef);
    if (snapshot.exists()) {
      // console.log("Testdata läst från databasen:", snapshot.val());

      // Rensa testdata direkt efter lyckad test för att undvika ackumulering
      await set(testRef, null);

      return { success: true, message: "Databasanslutning fungerar!" };
    } else {
      console.error("Kunde inte läsa testdata från databasen");
      return { success: false, error: "Kunde inte läsa testdata" };
    }
  } catch (error) {
    console.error("Fel vid test av databasanslutning:", error);
    return { success: false, error: error.message };
  }
};
