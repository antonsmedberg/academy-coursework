// src/components/Tasks/TaskCard.jsx
import React, { useState, memo, useMemo, useCallback } from "react";
import Button from "../UI/Button.jsx";
import Modal from "../UI/Modal.jsx";
import { formatDate, formatRelativeTime } from "../../utils/dateUtils.jsx";
import "../../styles/components/TaskCard.css";

/**
 * Uppgiftskort - visuell representation av en uppgift
 *
 * Visar en uppgift som ett interaktivt kort med all relevant information.
 * Fungerar som en digital post-it med möjlighet att:
 * - Visa uppgiftens status, kategori och tilldelning
 * - Öppna detaljvy med fullständig information
 * - Ändra status (flytta mellan kolumner)
 * - Tilldela uppgiften till rätt teammedlem
 * - Ta bort uppgiften från systemet
 *
 * @param {Object} task - Uppgiftsdata att visa
 * @param {Array} members - Tillgängliga teammedlemmar
 * @param {Function} onUpdateStatus - Callback för statusändring
 * @param {Function} onAssignTask - Callback för tilldelning
 * @param {Function} onDeleteTask - Callback för borttagning
 */
const TaskCard = ({
  task,
  members,
  onUpdateStatus,
  onAssignTask,
  onDeleteTask,
}) => {
  // States för modal och tilldelning
  const [showingTaskDetails, setShowingTaskDetails] = useState(false);
  const [askingForDeleteConfirmation, setAskingForDeleteConfirmation] =
    useState(false);
  const [chosenTeammateId, setChosenTeammateId] = useState("");

  /**
   * Hittar personer med rätt kompetens
   *
   * OPTIMERING: Memoizerad för bättre prestanda
   */
  const qualifiedTeammates = useMemo(
    () => members.filter((teammate) => teammate.role === task.category),
    [members, task.category]
  );

  /**
   * Min färgkodning för var uppgiften befinner sig
   *
   * Som trafikljus för arbetsflödet - blått för "vänta",
   * orange för "kör", grönt för "klart". Enkelt att förstå!
   */
  const getWorkflowColor = (status) => {
    switch (status.toLowerCase()) {
      case "nytt":
        return "status-new";
      case "pågående":
        return "status-in-progress";
      case "klar":
        return "status-completed";
      default:
        return "";
    }
  };

  /**
   * Min färgkodning för olika typer av arbete
   *
   * Varje kompetensområde får sin egen färg så man snabbt
   * ser vad som är vad på tavlan. Som att sortera LEGO!
   */
  const getSkillAreaColor = (category) => {
    switch (category.toLowerCase()) {
      case "frontend":
        return "category-frontend";
      case "backend":
        return "category-backend";
      case "ux":
        return "category-ux";
      default:
        return "category-default";
    }
  };

  /**
   * Flyttar uppgiften framåt i arbetsflödet
   *
   * Som att trycka på "nästa"-knappen i en process.
   * Stänger också detaljvyn så man ser resultatet direkt.
   *
   * OPTIMERING: Memoizerad callback för att undvika onödiga re-renders
   */
  const moveTaskForward = useCallback(
    (newStatus) => {
      onUpdateStatus(task.id, newStatus);
      setShowingTaskDetails(false);
    },
    [onUpdateStatus, task.id]
  );

  /**
   * Ger jobbet till rätt person
   *
   * Hittar den valda personen och säger "det här är ditt nu!".
   * Stänger sedan detaljvyn så man ser förändringen.
   *
   * OPTIMERING: Memoizerad callback för bättre prestanda
   */
  const giveTaskToChosen = useCallback(() => {
    if (!chosenTeammateId) return;

    const chosenPerson = members.find((m) => m.id === chosenTeammateId);
    if (chosenPerson) {
      onAssignTask(task.id, chosenPerson.id, chosenPerson.name);
      setShowingTaskDetails(false);
    }
  }, [chosenTeammateId, members, onAssignTask, task.id]);

  /**
   * Raderar uppgiften för alltid
   *
   * Som att kasta något i papperskorgen och tömma den.
   * Stänger alla öppna fönster efteråt.
   *
   * OPTIMERING: Memoizerad callback för bättre prestanda
   */
  const eraseTaskForever = useCallback(() => {
    onDeleteTask(task.id);
    setAskingForDeleteConfirmation(false);
    setShowingTaskDetails(false);
  }, [onDeleteTask, task.id]);

  /**
   * Renderar uppgiftskortet med alla dess komponenter
   *
   * Består av tre huvuddelar:
   * - Kompakt kort för visning på tavlan
   * - Detaljmodal med fullständig information
   * - Bekräftelsemodal för borttagning
   */
  return (
    <>
      {/* Det lilla kortet på tavlan - klicka för att se mer! */}
      <div className="task-card" onClick={() => setShowingTaskDetails(true)}>
        {/* Status och typ visas högst upp med färgkodning */}
        <div className="task-header">
          <span className={`task-status ${getWorkflowColor(task.status)}`}>
            {task.status}
          </span>
          <span className={`task-category ${getSkillAreaColor(task.category)}`}>
            {task.category}
          </span>
        </div>

        {/* Vad uppgiften handlar om - i stora bokstäver */}
        <h3 className="task-title">{task.title}</h3>

        {/* Kort beskrivning - kapar texten om den är för lång */}
        {task.description && (
          <p className="task-description">
            {task.description.length > 100
              ? `${task.description.substring(0, 100)}...`
              : task.description}
          </p>
        )}

        {/* Vem som gör uppgiften och när den skapades */}
        <div className="task-meta">
          {/* Vem jobbar med detta? Eller är det ledigt? */}
          {task.assignedToMemberName ? (
            <div className="task-assigned">
              <span className="meta-label">Tilldelad:</span>
              <span className="meta-value">{task.assignedToMemberName}</span>
            </div>
          ) : (
            <div className="task-assigned">
              <span className="meta-label">Tilldelad:</span>
              <span className="meta-value not-assigned">Ej tilldelad</span>
            </div>
          )}

          {/* När skapades uppgiften? Visar "2 dagar sedan" istället för exakt datum */}
          <div className="task-created">
            <span className="meta-label">Skapad:</span>
            <span
              className="meta-value"
              title={formatDate(task.creationTimestamp)}
            >
              {formatRelativeTime(task.creationTimestamp)}
            </span>
          </div>
        </div>
      </div>

      {/* Stor popup med alla detaljer och knappar */}
      <Modal
        isOpen={showingTaskDetails}
        onClose={() => setShowingTaskDetails(false)}
        title={task.title}
        size="medium"
        footer={
          <>
            {/* Ta bort-knappen (öppnar en "är du säker?"-ruta) */}
            <Button
              onClick={() => setAskingForDeleteConfirmation(true)}
              variant="danger"
            >
              Ta bort
            </Button>

            {/* Tomt utrymme som trycker isär knapparna */}
            <div style={{ flex: 1 }}></div>

            {/* "Klar"-knappen visas bara om uppgiften inte redan är klar */}
            {task.status !== "Klar" && (
              <Button onClick={() => moveTaskForward("Klar")} variant="success">
                Markera som klar
              </Button>
            )}

            {/* "Starta"-knappen visas bara för nya uppgifter */}
            {task.status === "Nytt" && (
              <Button
                onClick={() => moveTaskForward("Pågående")}
                variant="primary"
              >
                Starta uppgift
              </Button>
            )}
          </>
        }
      >
        <div className="task-details">
          {/* Var är uppgiften i flödet? */}
          <div className="task-detail-row">
            <span className="detail-label">Status:</span>
            <span
              className={`detail-value task-status ${getWorkflowColor(
                task.status
              )}`}
            >
              {task.status}
            </span>
          </div>

          {/* Vilken typ av uppgift? */}
          <div className="task-detail-row">
            <span className="detail-label">Kategori:</span>
            <span
              className={`detail-value task-category ${getSkillAreaColor(
                task.category
              )}`}
            >
              {task.category}
            </span>
          </div>

          {/* När skapades uppgiften? */}
          <div className="task-detail-row">
            <span className="detail-label">Skapad:</span>
            <span className="detail-value">
              {formatDate(task.creationTimestamp)}
            </span>
          </div>

          {/* Detaljerad beskrivning av vad som ska göras */}
          {task.description && (
            <div className="task-detail-description">
              <h4>Beskrivning</h4>
              <p>{task.description}</p>
            </div>
          )}

          {/* Vem ska göra jobbet? */}
          <div className="task-assignment">
            <h4>Tilldelning</h4>

            {/* Redan tilldelad? Visa vem och ge möjlighet att ändra */}
            {task.assignedToMemberName ? (
              <div className="current-assignment">
                <p>
                  Denna uppgift är tilldelad till:{" "}
                  <strong>{task.assignedToMemberName}</strong>
                </p>
                <Button
                  onClick={() => onAssignTask(task.id, null, null)}
                  variant="secondary"
                  size="small"
                >
                  Ta bort tilldelning
                </Button>
              </div>
            ) : (
              /* Inte tilldelad? Visa formulär för att välja person */
              <div className="assignment-form">
                {/* Lista med personer som har rätt kompetens för jobbet */}
                {qualifiedTeammates.length > 0 ? (
                  <>
                    <p>
                      Tilldela denna {task.category}-uppgift till en teammedlem
                      med matchande roll:
                    </p>
                    <div className="assignment-controls">
                      <select
                        value={chosenTeammateId}
                        onChange={(e) => setChosenTeammateId(e.target.value)}
                      >
                        <option value="">Välj teammedlem</option>
                        {qualifiedTeammates.map((teammate) => (
                          <option key={teammate.id} value={teammate.id}>
                            {teammate.name}
                          </option>
                        ))}
                      </select>
                      <Button
                        onClick={giveTaskToChosen}
                        variant="primary"
                        disabled={!chosenTeammateId}
                      >
                        Tilldela
                      </Button>
                    </div>
                  </>
                ) : (
                  /* Inga personer med rätt kompetens? Visa ett meddelande */
                  <p className="no-eligible-members">
                    Det finns inga teammedlemmar med rollen {task.category} som
                    kan tilldelas denna uppgift.
                  </p>
                )}
              </div>
            )}
          </div>
        </div>
      </Modal>

      {/* "Är du säker?"-rutan för borttagning */}
      <Modal
        isOpen={askingForDeleteConfirmation}
        onClose={() => setAskingForDeleteConfirmation(false)}
        title="Bekräfta borttagning"
        footer={
          <>
            <Button
              onClick={() => setAskingForDeleteConfirmation(false)}
              variant="secondary"
            >
              Avbryt
            </Button>
            <Button onClick={eraseTaskForever} variant="danger">
              Ta bort
            </Button>
          </>
        }
      >
        <p>
          Är du säker på att du vill ta bort uppgiften "{task.title}"? Uppgiften
          kommer att tas bort permanent från databasen och kan inte återställas.
        </p>
      </Modal>
    </>
  );
};

// OPTIMERING: Memoizerad komponent för att undvika onödiga re-renders
// Komponenten renderas bara om när props faktiskt ändras
export default memo(TaskCard);
