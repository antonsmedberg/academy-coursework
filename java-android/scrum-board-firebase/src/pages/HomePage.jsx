// src/pages/HomePage.jsx
/**
 * Huvudsidan för Scrum Board 🏠
 *
 * Styr hela appen:
 * - Firebase-kommunikation via hooks
 * - Uppgifts- och medlemshantering
 * - Navigation mellan Board, Uppgifter och Team
 * - Notifikationer för användarfeedback
 */
import React, { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";

// UI-komponenter
import ScrumBoard from "../components/Board/ScrumBoard.jsx";
import TaskList from "../components/Tasks/TaskList.jsx";
import MemberList from "../components/Members/MemberList.jsx";
import Notification from "../components/UI/Notification.jsx";
import DatabaseSeeder from "../components/Admin/DatabaseSeeder.jsx";
import Button from "../components/UI/Button.jsx";
import { LogoutIcon, UserIcon } from "../components/UI/Icons.jsx";

// Firebase-hooks
import useMembers from "../hooks/useMembers.jsx"; // Teamhantering
import useOptimizedTasks from "../hooks/useOptimizedTasks.jsx"; // Uppgiftshantering
import { useAuth } from "../hooks/useAuth.jsx"; // Autentisering

// Styling för att göra allt snyggt
import "../styles/pages/HomePage.css";
import "../styles/components/DatabaseSeeder.css";

const HomePage = () => {
  // Håller koll på vilken flik som är aktiv och vad som händer
  const [activeTab, setActiveTab] = useState("board"); // Vilken vy ser vi just nu?
  const [notification, setNotification] = useState(null); // Meddelanden till användaren

  // Mina autentiseringsverktyg - vem är inloggad och hur loggar vi ut?
  const { currentUser, logout, deleteCurrentUser } = useAuth();
  const navigate = useNavigate();

  /**
   * Mina tre favoritområden inom webbutveckling 💻
   *
   * Som en utvecklare ser jag webbutveckling i tre delar:
   * - Frontend: Det vackra som användarna ser och klickar på
   * - Backend: Den smarta logiken som gör att allt fungerar
   * - UX: Konsten att göra allt intuitivt och användarvänligt
   */
  const myTeamSkillAreas = ["frontend", "backend", "ux"];

  /**
   * Hook för teamhantering
   *
   * Hanterar all data och funktionalitet relaterad till teammedlemmar:
   * - Medlemslista
   * - Laddningsstatus
   * - Felhantering
   * - CRUD-operationer för medlemmar
   */
  const {
    members,
    loading: membersLoading,
    error: membersError,
    addMember,
    deleteMember,
  } = useMembers();

  /**
   * Optimerad uppgiftshantering
   *
   * Förbättringar: memoization, query-optimering, caching.
   */
  const {
    tasks,
    loading: tasksLoading,
    error: tasksError,
    addTask,
    deleteTask,
    updateTaskStatus,
    assignTask,
  } = useOptimizedTasks({
    realtime: true, // Realtidsuppdateringar
    limit: 100, // Begränsa för prestanda
  });

  /**
   * Min personliga notifikationsassistent
   *
   * Som en vänlig kollega som tipsar om vad som händer i appen.
   * Dyker upp, säger sitt, och försvinner diskret efter 5 sekunder.
   *
   * @param {string} message - Vad som ska sägas till användaren
   * @param {string} type - Känslan i meddelandet (info, success, error)
   */
  const tellUserWhatHappened = (message, type = "info") => {
    // Visa meddelandet för användaren
    setNotification({ message, type });

    // Försvinn automatiskt så användaren slipper stänga manuellt
    setTimeout(() => {
      setNotification(null);
    }, 5000);
  };

  /**
   * Hanterar tillägg av teammedlem
   *
   * Lägger till en ny medlem och visar feedback om resultatet.
   * OPTIMERING: Memoizerad callback för bättre prestanda
   *
   * @param {Object} memberData - Medlemsdata från formuläret
   */
  const welcomeNewTeammate = useCallback(
    async (memberData) => {
      // Lägg till den nya personen i teamet
      const result = await addMember(memberData);

      // Berätta för användaren hur det gick
      if (result.success) {
        tellUserWhatHappened(
          `Välkommen ${memberData.name} till teamet! 🎉`,
          "success"
        );
      } else {
        tellUserWhatHappened(
          `Kunde inte lägga till ${memberData.name}: ${result.error}`,
          "error"
        );
      }
    },
    [addMember]
  );

  /**
   * Hanterar borttagning av teammedlem
   *
   * Tar bort en medlem och visar feedback om resultatet.
   * OPTIMERING: Memoizerad callback för bättre prestanda
   *
   * @param {string} memberId - ID för medlemmen som ska tas bort
   */
  const sayGoodbyeToTeammate = useCallback(
    async (memberId) => {
      // Ta bort personen från teamet
      const result = await deleteMember(memberId);

      // Meddela användaren vad som hände
      if (result.success) {
        tellUserWhatHappened("Teammedlem har lämnat projektet.", "success");
      } else {
        tellUserWhatHappened(
          `Kunde inte ta bort teammedlem: ${result.error}`,
          "error"
        );
      }
    },
    [deleteMember]
  );

  /**
   * Hanterar tillägg av uppgift
   *
   * Lägger till en ny uppgift och visar feedback om resultatet.
   * OPTIMERING: Memoizerad callback för bättre prestanda
   *
   * @param {Object} taskData - Uppgiftsdata från formuläret
   */
  const createNewWorkItem = useCallback(
    async (taskData) => {
      // Skapa en ny uppgift i systemet
      const result = await addTask(taskData);

      // Berätta för användaren vad som hände
      if (result.success) {
        tellUserWhatHappened(
          `Ny uppgift "${taskData.title}" är redo att tacklas! 💪`,
          "success"
        );
      } else {
        tellUserWhatHappened(
          `Kunde inte skapa uppgift: ${result.error}`,
          "error"
        );
      }
    },
    [addTask]
  );

  /**
   * Hanterar statusändring för uppgift
   *
   * Uppdaterar en uppgifts status och visar feedback om resultatet.
   * OPTIMERING: Memoizerad callback för bättre prestanda
   *
   * @param {string} taskId - Uppgiftens ID
   * @param {string} newStatus - Ny status (Nytt/Pågående/Klar)
   */
  const moveTaskToNewColumn = useCallback(
    async (taskId, newStatus) => {
      // Flytta uppgiften till rätt kolumn på tavlan
      const result = await updateTaskStatus(taskId, newStatus);

      // Låt användaren veta vad som hände
      if (result.success) {
        tellUserWhatHappened(
          `Uppgift flyttad till "${newStatus}" - bra jobbat! 🎯`,
          "success"
        );
      } else {
        tellUserWhatHappened(
          `Kunde inte flytta uppgift: ${result.error}`,
          "error"
        );
      }
    },
    [updateTaskStatus]
  );

  /**
   * Hanterar tilldelning av uppgift
   *
   * Tilldelar en uppgift till en teammedlem eller tar bort tilldelning.
   * Visar feedback om resultatet.
   * OPTIMERING: Memoizerad callback för bättre prestanda
   *
   * @param {string} taskId - Uppgiftens ID
   * @param {string|null} memberId - Medlemmens ID eller null
   * @param {string|null} memberName - Medlemmens namn för visning
   */
  const assignWorkToTeammate = useCallback(
    async (taskId, memberId, memberName) => {
      // Ge uppgiften till rätt person i teamet
      const result = await assignTask(taskId, memberId, memberName);

      // Berätta vad som hände
      if (result.success) {
        if (memberId) {
          tellUserWhatHappened(
            `${memberName} tar nu hand om uppgiften! 👍`,
            "success"
          );
        } else {
          tellUserWhatHappened(
            "Uppgiften är nu ledig för vem som helst.",
            "info"
          );
        }
      } else {
        tellUserWhatHappened(
          `Kunde inte tilldela uppgift: ${result.error}`,
          "error"
        );
      }
    },
    [assignTask]
  );

  /**
   * Hanterar borttagning av uppgift
   *
   * Tar bort en uppgift permanent från databasen och
   * visar feedback om resultatet.
   * OPTIMERING: Memoizerad callback för bättre prestanda
   *
   * @param {string} taskId - Uppgiftens ID
   */
  const removeTaskCompletely = useCallback(
    async (taskId) => {
      // Ta bort uppgiften helt från systemet
      const result = await deleteTask(taskId);

      // Meddela användaren resultatet
      if (result.success) {
        tellUserWhatHappened(
          "Uppgift borttagen - som om den aldrig funnits! ✨",
          "success"
        );
      } else {
        tellUserWhatHappened(
          `Kunde inte ta bort uppgift: ${result.error}`,
          "error"
        );
      }
    },
    [deleteTask]
  );

  /**
   * Hanterar utloggning
   *
   * Städar upp efter anonyma användare och skickar alla tillbaka till login.
   */
  const handleLogout = async () => {
    try {
      // För anonyma användare: ta bort dem från Firebase först
      if (currentUser?.isAnonymous) {
        console.log("Städar upp anonym användare:", currentUser.uid);

        try {
          const deleteResult = await deleteCurrentUser();
          if (deleteResult.success) {
            console.log("Användare borttagen! 🧹");
            navigate("/login");
            return;
          } else {
            // Om borttagning misslyckades, fortsätt med vanlig utloggning
            console.warn("Kunde inte ta bort användaren:", deleteResult.error);
          }
        } catch (error) {
          console.error("Fel vid städning:", error);
        }
      }

      // Vanlig utloggning
      const result = await logout();
      if (result.success) {
        navigate("/login");
      } else {
        tellUserWhatHappened("Kunde inte logga ut. Försök igen.", "error");
      }
    } catch (error) {
      tellUserWhatHappened("Ett fel uppstod vid utloggning.", "error");
    }
  };

  /**
   * Visar felmeddelanden vid datahämtningsfel
   *
   * Övervakar felstatus och visar notifikationer när
   * fel uppstår vid hämtning av data.
   */
  useEffect(() => {
    // Berätta för användaren om något gick fel med teamdata
    if (membersError) {
      tellUserWhatHappened(
        `Problem med att hämta teammedlemmar: ${membersError}`,
        "error"
      );
    }

    // Berätta för användaren om något gick fel med uppgiftsdata
    if (tasksError) {
      tellUserWhatHappened(
        `Problem med att hämta uppgifter: ${tasksError}`,
        "error"
      );
    }
  }, [membersError, tasksError]); // Körs när fel uppstår

  /**
   * Renderar applikationens huvudgränssnitt
   *
   * Bygger upp UI med header, navigation, innehåll och notifikationer
   */
  return (
    <div className="home-page">
      {/* Applikationens header med titel och användarinfo */}
      <header className="app-header">
        <h1>Scrum Board</h1>

        {/* Användarinfo och utloggningsknapp */}
        <div className="user-controls">
          {currentUser?.isAnonymous ? (
            <div className="user-profile">
              <div className="user-avatar">
                <UserIcon size={18} color="#3b82f6" />
              </div>
              <div className="user-info">
                <span className="user-name">Anonym användare</span>
                <span className="user-status">Temporär session</span>
              </div>
              <Button
                onClick={handleLogout}
                variant="outline"
                size="small"
                className="logout-button"
                iconRight={<LogoutIcon size={16} />}
                aria-label="Logga ut"
              >
                Avsluta
              </Button>
            </div>
          ) : (
            <div className="user-profile">
              <div className="user-avatar">
                <UserIcon size={18} color="#3b82f6" />
              </div>
              <div className="user-info">
                <span className="user-name">{currentUser?.email}</span>
                <span className="user-status">Inloggad</span>
              </div>
              <Button
                onClick={handleLogout}
                variant="outline"
                size="small"
                className="logout-button"
                iconRight={<LogoutIcon size={16} />}
                aria-label="Logga ut"
              >
                Logga ut
              </Button>
            </div>
          )}
        </div>
      </header>

      {/* Fliknavigation för att växla mellan olika vyer */}
      <nav className="app-tabs" aria-label="Application Navigation">
        {/* Board-fliken (kanban-vy) */}
        <button
          className={`tab-button ${activeTab === "board" ? "active" : ""}`}
          onClick={() => setActiveTab("board")}
          aria-selected={activeTab === "board"}
          role="tab"
        >
          Board
        </button>

        {/* Uppgifter-fliken (listvy med filter och sortering) */}
        <button
          className={`tab-button ${activeTab === "tasks" ? "active" : ""}`}
          onClick={() => setActiveTab("tasks")}
          aria-selected={activeTab === "tasks"}
          role="tab"
        >
          Uppgifter
        </button>

        {/* Team-fliken (hantering av teammedlemmar) */}
        <button
          className={`tab-button ${activeTab === "members" ? "active" : ""}`}
          onClick={() => setActiveTab("members")}
          aria-selected={activeTab === "members"}
          role="tab"
        >
          Team
        </button>
      </nav>

      {/* Huvudinnehållsområde som visar olika komponenter baserat på aktiv flik */}
      <main className="app-content" role="tabpanel" aria-live="polite">
        {/* Scrum Board-vyn (kanban-vy med kolumner) */}
        {activeTab === "board" && (
          <ScrumBoard
            tasks={tasks}
            members={members}
            onAddTask={createNewWorkItem}
            onUpdateTaskStatus={moveTaskToNewColumn}
            onAssignTask={assignWorkToTeammate}
            onDeleteTask={removeTaskCompletely}
            categories={myTeamSkillAreas}
            isLoading={tasksLoading || membersLoading}
          />
        )}

        {/* Uppgiftslista med filter och sortering */}
        {activeTab === "tasks" && (
          <TaskList
            tasks={tasks}
            members={members}
            onAddTask={createNewWorkItem}
            onUpdateTaskStatus={moveTaskToNewColumn}
            onAssignTask={assignWorkToTeammate}
            onDeleteTask={removeTaskCompletely}
            categories={myTeamSkillAreas}
            isLoading={tasksLoading || membersLoading}
          />
        )}

        {/* Teammedlemslista */}
        {activeTab === "members" && (
          <MemberList
            members={members}
            onAddMember={welcomeNewTeammate}
            onDeleteMember={sayGoodbyeToTeammate}
            roles={myTeamSkillAreas}
            isLoading={membersLoading}
          />
        )}
      </main>

      {/* Notifikationssystem som visar feedback till användaren */}
      {notification && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={() => setNotification(null)}
        />
      )}

      {/* Verktyg för att fylla databasen med exempeldata (alltid synligt för felsökning) */}
      <DatabaseSeeder onSuccess={tellUserWhatHappened} />
    </div>
  );
};

export default HomePage;
