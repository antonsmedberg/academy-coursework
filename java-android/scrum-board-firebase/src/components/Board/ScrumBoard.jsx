// src/components/Board/ScrumBoard.jsx
/**
 * Min digitala kanban-tavla - hjärtat i hela appen!
 *
 * Som en fysisk whiteboard fast bättre - här ser du alla uppgifter
 * sorterade i tre kolumner som visar exakt var allt befinner sig.
 * Uppdateras live så alla i teamet ser samma sak samtidigt.
 *
 * Varför jag älskar den här tavlan:
 * - Ser direkt vad som händer i projektet (inga överraskningar!)
 * - Upptäcker flaskhalsar innan de blir problem
 * - Perfekt för morgonmöten - "vad gör vi idag?"
 * - Alla förstår läget utan långa förklaringar
 */
import React, { useState, memo, useMemo, useCallback } from "react";
import TaskCard from "../Tasks/TaskCard.jsx";
import Button from "../UI/Button.jsx";
import Modal from "../UI/Modal.jsx";
import AddTaskForm from "../Tasks/AddTaskForm.jsx";
import { filterByStatus, filterOutArchived } from "../../utils/filterUtils.jsx";
import "../../styles/components/ScrumBoard.css";

const ScrumBoard = ({
  tasks,
  members,
  onAddTask,
  onUpdateTaskStatus,
  onAssignTask,
  onDeleteTask,
  categories = ["frontend", "backend", "ux"],
  isLoading = false,
}) => {
  const [showingAddTaskForm, setShowingAddTaskForm] = useState(false);

  /**
   * Sorterar uppgifter i kolumner
   *
   * OPTIMERING: Memoizerad för bättre prestanda
   */
  const { freshTasks, ongoingWork, finishedStuff } = useMemo(() => {
    // Ta bort arkiverade uppgifter
    const activeWorkItems = filterOutArchived(tasks);

    // Sortera i tre kolumner
    return {
      freshTasks: filterByStatus(activeWorkItems, "Nytt"),
      ongoingWork: filterByStatus(activeWorkItems, "Pågående"),
      finishedStuff: filterByStatus(activeWorkItems, "Klar"),
    };
  }, [tasks]);

  // När någon skapar en ny uppgift
  // OPTIMERING: Memoizerad callback för bättre prestanda
  const createTaskAndCloseForm = useCallback(
    (taskData) => {
      onAddTask(taskData); // Skicka upp till huvudappen
      setShowingAddTaskForm(false); // Stäng formuläret
    },
    [onAddTask]
  );

  return (
    <div className="scrum-board-container">
      <div className="board-header">
        <h2>Scrum Board</h2>
        <Button onClick={() => setShowingAddTaskForm(true)} variant="primary">
          Lägg till uppgift
        </Button>
      </div>

      {isLoading ? (
        <div className="loading-indicator">
          <span>Laddar uppgifter</span>
        </div>
      ) : (
        <div className="board-columns">
          <div className="board-column">
            <div className="column-header">
              <h3>Nya uppgifter</h3>
              <span className="task-count">{freshTasks.length}</span>
            </div>
            <div className="column-content">
              {freshTasks.length === 0 ? (
                <div className="empty-column">
                  <p>Inga nya uppgifter</p>
                  <Button
                    onClick={() => setShowingAddTaskForm(true)}
                    variant="secondary"
                    size="small"
                  >
                    Lägg till uppgift
                  </Button>
                </div>
              ) : (
                freshTasks.map((task) => (
                  <TaskCard
                    key={task.id}
                    task={task}
                    members={members}
                    onUpdateStatus={onUpdateTaskStatus}
                    onAssignTask={onAssignTask}
                    onDeleteTask={onDeleteTask}
                  />
                ))
              )}
            </div>
          </div>

          <div className="board-column">
            <div className="column-header">
              <h3>Pågående</h3>
              <span className="task-count">{ongoingWork.length}</span>
            </div>
            <div className="column-content">
              {ongoingWork.length === 0 ? (
                <div className="empty-column">
                  <p>Inga pågående uppgifter</p>
                </div>
              ) : (
                ongoingWork.map((task) => (
                  <TaskCard
                    key={task.id}
                    task={task}
                    members={members}
                    onUpdateStatus={onUpdateTaskStatus}
                    onAssignTask={onAssignTask}
                    onDeleteTask={onDeleteTask}
                  />
                ))
              )}
            </div>
          </div>

          <div className="board-column">
            <div className="column-header">
              <h3>Klara</h3>
              <span className="task-count">{finishedStuff.length}</span>
            </div>
            <div className="column-content">
              {finishedStuff.length === 0 ? (
                <div className="empty-column">
                  <p>Inga klara uppgifter</p>
                </div>
              ) : (
                finishedStuff.map((task) => (
                  <TaskCard
                    key={task.id}
                    task={task}
                    members={members}
                    onUpdateStatus={onUpdateTaskStatus}
                    onAssignTask={onAssignTask}
                    onDeleteTask={onDeleteTask}
                  />
                ))
              )}
            </div>
          </div>
        </div>
      )}

      {/* Modal för att lägga till nya uppgifter */}
      <Modal
        isOpen={showingAddTaskForm}
        onClose={() => setShowingAddTaskForm(false)}
        title="Lägg till uppgift"
      >
        <AddTaskForm
          onAddTask={createTaskAndCloseForm}
          categories={categories}
        />
      </Modal>
    </div>
  );
};

// OPTIMERING: Memoizerad komponent för att undvika onödiga re-renders
export default memo(ScrumBoard);
