// src/components/Tasks/AddTaskForm.jsx
/**
 * Formulär för att skapa nya uppgifter
 *
 * Hanterar inmatning och validering av uppgiftsdata med:
 * - Obligatorisk titel för att identifiera uppgiften
 * - Val av kategori (frontend, backend, UX) för korrekt tilldelning
 * - Valfri beskrivning för ytterligare detaljer
 *
 * Validerar att nödvändiga fält är ifyllda innan uppgiften skapas
 * och skickas till Firebase via callback-funktionen.
 */
import React, { useState } from "react";
import Button from "../UI/Button.jsx";
import "../../styles/components/AddTaskForm.css";

const AddTaskForm = ({
  onAddTask,
  categories = ["frontend", "backend", "ux"],
}) => {
  const [title, setTitle] = useState("");
  const [category, setCategory] = useState(categories[0]);
  const [description, setDescription] = useState("");
  const [error, setError] = useState("");

  /**
   * Hanterar formulärinlämning
   *
   * Processen vid inlämning:
   * 1. Validerar obligatoriska fält (titel och kategori)
   * 2. Skapar ett nytt uppgiftsobjekt med inmatad data
   * 3. Skickar uppgiften till föräldrakomponenten och återställer formuläret
   *
   * @param {Event} e - Formulärets submit-event
   */
  const handleSubmit = (e) => {
    // Stoppar webbläsaren från att ladda om sidan
    e.preventDefault();

    // Validera titel (obligatorisk)
    if (!title.trim()) {
      setError("Du måste ge uppgiften en titel!");
      return;
    }

    // Validera kategori (obligatorisk)
    if (!category) {
      setError("Du måste välja en kategori!");
      return;
    }

    // Skapa uppgiftsobjekt
    const newTask = {
      title: title.trim(),
      category,
      description: description.trim() || null,
      assignedToMemberId: null,
      assignedToMemberName: null,
      // Tidsstämpel och status hanteras av hook
    };

    // Skicka till föräldrakomponent
    onAddTask(newTask);

    // Återställ formuläret
    setTitle("");
    setCategory(categories[0]);
    setDescription("");
    setError("");
  };

  return (
    <form className="add-task-form" onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}

      <div className="form-group">
        <label htmlFor="title">Titel *</label>
        <input
          type="text"
          id="title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Ange uppgiftens titel"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="category">Kategori *</label>
        <select
          id="category"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          required
        >
          {categories.map((categoryOption) => (
            <option key={categoryOption} value={categoryOption}>
              {categoryOption.charAt(0).toUpperCase() + categoryOption.slice(1)}
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label htmlFor="description">Beskrivning (valfritt)</label>
        <textarea
          id="description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Beskriv uppgiften mer detaljerat"
          rows={4}
        />
      </div>

      <div className="form-actions">
        <Button type="submit" variant="primary">
          Lägg till
        </Button>
      </div>
    </form>
  );
};

export default AddTaskForm;
