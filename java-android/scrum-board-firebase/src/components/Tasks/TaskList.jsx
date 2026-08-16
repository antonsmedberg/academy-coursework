// src/components/Tasks/TaskList.jsx
/**
 * Smart uppgiftslista med filtrering 🔍
 *
 * Filtrera på person, kategori eller status.
 * Sortera som du vill. Lägg till nya uppgifter.
 */
import React, { useState, memo, useMemo, useCallback } from "react";
import Button from "../UI/Button.jsx";
import Modal from "../UI/Modal.jsx";
import TaskCard from "./TaskCard.jsx";
import AddTaskForm from "./AddTaskForm.jsx";
import {
  filterByMember,
  filterByCategory,
  filterByStatus,
  filterOutArchived,
} from "../../utils/filterUtils.jsx";
import {
  sortByNewest,
  sortByOldest,
  sortByTitleAsc,
  sortByTitleDesc,
} from "../../utils/sortUtils.jsx";
import "../../styles/components/TaskList.css";

const TaskList = ({
  tasks,
  members,
  onAddTask,
  onUpdateTaskStatus,
  onAssignTask,
  onDeleteTask,
  categories = ["frontend", "backend", "ux"],
  isLoading = false,
}) => {
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [filterMember, setFilterMember] = useState("");
  const [filterCategory, setFilterCategory] = useState("");
  const [filterStatus, setFilterStatus] = useState("");
  const [sortOption, setSortOption] = useState("newest");

  // SMART SORTERING: Bara räknar om när något faktiskt ändras (sparar prestanda)
  const sortedTasks = useMemo(() => {
    // Först filtrerar vi bort det vi inte vill se
    const filteredTasks = filterOutArchived(tasks) // Städar bort gamla arkiverade
      .filter(
        (task) => !filterMember || task.assignedToMemberId === filterMember
      ) // Bara en persons uppgifter?
      .filter((task) => !filterCategory || task.category === filterCategory) // Bara en typ av uppgifter?
      .filter((task) => !filterStatus || task.status === filterStatus); // Bara en status?

    // Sen sorterar vi som användaren vill ha det
    switch (sortOption) {
      case "newest":
        return sortByNewest(filteredTasks); // Nyast först (som Instagram)
      case "oldest":
        return sortByOldest(filteredTasks); // Äldst först (rättvist)
      case "titleAsc":
        return sortByTitleAsc(filteredTasks); // A-Ö (som telefonbok)
      case "titleDesc":
        return sortByTitleDesc(filteredTasks); // Ö-A (baklänges)
      default:
        return sortByNewest(filteredTasks); // Standard = nyast först
    }
  }, [tasks, filterMember, filterCategory, filterStatus, sortOption]);

  /**
   * Lägger till uppgift och stänger formuläret 📝
   */
  const handleAddTask = useCallback(
    (taskData) => {
      onAddTask(taskData);
      setIsAddModalOpen(false);
    },
    [onAddTask]
  );

  /**
   * Nollställer alla filter 🔄
   */
  const resetFilters = useCallback(() => {
    setFilterMember("");
    setFilterCategory("");
    setFilterStatus("");
    setSortOption("newest");
  }, []);

  return (
    <div className="task-list-container">
      <div className="task-list-header">
        <h2>Uppgifter</h2>
        <Button onClick={() => setIsAddModalOpen(true)} variant="primary">
          Lägg till uppgift
        </Button>
      </div>

      <div className="task-filters">
        <div className="filter-group">
          <label htmlFor="filterMember">Filtrera efter medlem:</label>
          <select
            id="filterMember"
            value={filterMember}
            onChange={(e) => setFilterMember(e.target.value)}
          >
            <option value="">Alla medlemmar</option>
            {members.map((member) => (
              <option key={member.id} value={member.id}>
                {member.name}
              </option>
            ))}
          </select>
        </div>

        <div className="filter-group">
          <label htmlFor="filterCategory">Filtrera efter kategori:</label>
          <select
            id="filterCategory"
            value={filterCategory}
            onChange={(e) => setFilterCategory(e.target.value)}
          >
            <option value="">Alla kategorier</option>
            {categories.map((category) => (
              <option key={category} value={category}>
                {category.charAt(0).toUpperCase() + category.slice(1)}
              </option>
            ))}
          </select>
        </div>

        <div className="filter-group">
          <label htmlFor="filterStatus">Filtrera efter status:</label>
          <select
            id="filterStatus"
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
          >
            <option value="">Alla statusar</option>
            <option value="Nytt">Nytt</option>
            <option value="Pågående">Pågående</option>
            <option value="Klar">Klar</option>
          </select>
        </div>

        <div className="filter-group">
          <label htmlFor="sortOption">Sortera efter:</label>
          <select
            id="sortOption"
            value={sortOption}
            onChange={(e) => setSortOption(e.target.value)}
          >
            <option value="newest">Nyast först</option>
            <option value="oldest">Äldst först</option>
            <option value="titleAsc">Titel (A-Ö)</option>
            <option value="titleDesc">Titel (Ö-A)</option>
          </select>
        </div>

        <Button onClick={resetFilters} variant="secondary" size="small">
          Återställ filter
        </Button>
      </div>

      {isLoading ? (
        <div className="loading-indicator">
          <span>Laddar uppgifter</span>
        </div>
      ) : sortedTasks.length === 0 ? (
        <div className="empty-state">
          <p>
            Inga uppgifter hittades
            {filterMember || filterCategory || filterStatus
              ? " med valda filter"
              : ""}
            .
          </p>
          {filterMember || filterCategory || filterStatus ? (
            <Button onClick={resetFilters} variant="secondary">
              Återställ filter
            </Button>
          ) : (
            <Button onClick={() => setIsAddModalOpen(true)} variant="secondary">
              Lägg till din första uppgift
            </Button>
          )}
        </div>
      ) : (
        <div className="task-grid">
          {sortedTasks.map((task) => (
            <TaskCard
              key={task.id}
              task={task}
              members={members}
              onUpdateStatus={onUpdateTaskStatus}
              onAssignTask={onAssignTask}
              onDeleteTask={onDeleteTask}
            />
          ))}
        </div>
      )}

      {/* Popup-fönster för att skapa nya uppgifter */}
      <Modal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        title="Lägg till uppgift"
      >
        <AddTaskForm onAddTask={handleAddTask} categories={categories} />
      </Modal>
    </div>
  );
};

// SMART KOMPONENT: Renderas bara om när något faktiskt ändrats (sparar prestanda)
export default memo(TaskList);
