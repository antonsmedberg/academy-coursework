// src/components/Members/AddMemberForm.jsx
/**
 * Formulär för nya teammedlemmar
 *
 * Obligatoriskt: namn och roll
 * Valfritt: profilbild-URL
 * Validerar innan sparning.
 */
import React, { useState } from "react";
import Button from "../UI/Button.jsx";
import "../../styles/components/AddMemberForm.css";

const AddMemberForm = ({
  onAddMember,
  roles = ["frontend", "backend", "ux"],
}) => {
  const [name, setName] = useState("");
  const [role, setRole] = useState(roles[0]);
  const [avatar, setAvatar] = useState("");
  const [error, setError] = useState("");

  /**
   * Hanterar formulärinlämning
   *
   * Validerar, skapar medlem och återställer formulär.
   */
  const handleSubmit = (e) => {
    e.preventDefault();

    // Validera namn
    if (!name.trim()) {
      setError("Du måste ange ett namn!");
      return;
    }

    // Validera roll
    if (!role) {
      setError("Du måste välja en roll!");
      return;
    }

    // Skapa medlemsobjekt
    const newMember = {
      name: name.trim(),
      role,
      avatar: avatar.trim() || null,
    };

    // Skicka medlemmen till föräldrakomponenten
    onAddMember(newMember);

    // Återställ formuläret
    setName("");
    setRole(roles[0]);
    setAvatar("");
    setError("");
  };

  return (
    <form className="add-member-form" onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}

      <div className="form-group">
        <label htmlFor="name">Namn *</label>
        <input
          type="text"
          id="name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Ange namn"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="role">Roll *</label>
        <select
          id="role"
          value={role}
          onChange={(e) => setRole(e.target.value)}
          required
        >
          {roles.map((roleOption) => (
            <option key={roleOption} value={roleOption}>
              {roleOption.charAt(0).toUpperCase() + roleOption.slice(1)}
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label htmlFor="avatar">Profilbild URL (valfritt)</label>
        <input
          type="url"
          id="avatar"
          value={avatar}
          onChange={(e) => setAvatar(e.target.value)}
          placeholder="https://example.com/image.jpg"
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

export default AddMemberForm;
