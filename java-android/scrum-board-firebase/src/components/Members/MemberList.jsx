// src/components/Members/MemberList.jsx
/**
 * Teamöversikt med medlemshantering
 *
 * Visar medlemmar med profilbild, namn och roll.
 * Lägg till och ta bort medlemmar med bekräftelse.
 */
import React, { useState } from "react";
import Button from "../UI/Button.jsx";
import Modal from "../UI/Modal.jsx";
import AddMemberForm from "./AddMemberForm.jsx";
import "../../styles/components/MemberList.css";

const MemberList = ({
  members,
  onAddMember,
  onDeleteMember,
  roles = ["frontend", "backend", "ux"],
  isLoading = false,
}) => {
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [memberToDelete, setMemberToDelete] = useState(null);

  /**
   * Lägger till ny medlem och stänger modal
   */
  const handleAddMember = (memberData) => {
    onAddMember(memberData);
    setIsAddModalOpen(false);
  };

  /**
   * Tar bort medlem efter bekräftelse
   */
  const handleConfirmDelete = () => {
    if (memberToDelete) {
      onDeleteMember(memberToDelete.id);
      setMemberToDelete(null);
    }
  };

  /**
   * Returnerar CSS-klass baserat på medlemsroll
   *
   * Ger visuell färgkodning för olika roller:
   * - Frontend: blå
   * - Backend: grön
   * - UX: lila
   *
   * @param {string} role - Medlemmens roll
   * @returns {string} CSS-klassnamn för färgkodning
   */
  const getRoleColorClass = (role) => {
    switch (role.toLowerCase()) {
      case "frontend":
        return "role-frontend";
      case "backend":
        return "role-backend";
      case "ux":
        return "role-ux";
      default:
        return "role-default";
    }
  };

  return (
    <div className="member-list-container">
      <div className="member-list-header">
        <h2>Teammedlemmar</h2>
        <Button onClick={() => setIsAddModalOpen(true)} variant="primary">
          Lägg till medlem
        </Button>
      </div>

      {isLoading ? (
        <div className="loading-indicator">
          <span>Laddar teammedlemmar</span>
        </div>
      ) : members.length === 0 ? (
        <div className="empty-state">
          <p>Inga teammedlemmar tillagda ännu.</p>
          <Button onClick={() => setIsAddModalOpen(true)} variant="secondary">
            Lägg till din första teammedlem
          </Button>
        </div>
      ) : (
        <div className="member-grid">
          {members.map((member) => (
            <div key={member.id} className="member-card">
              <div className="member-avatar">
                {member.avatar ? (
                  <img src={member.avatar} alt={member.name} />
                ) : (
                  <div className="member-initials">
                    {member.name
                      .split(" ")
                      .map((n) => n[0])
                      .join("")}
                  </div>
                )}
              </div>
              <div className="member-info">
                <h3 className="member-name">{member.name}</h3>
                <span
                  className={`member-role ${getRoleColorClass(member.role)}`}
                >
                  {member.role.charAt(0).toUpperCase() + member.role.slice(1)}
                </span>
              </div>
              <Button
                onClick={() => setMemberToDelete(member)}
                variant="danger"
                size="small"
              >
                Ta bort
              </Button>
            </div>
          ))}
        </div>
      )}

      {/* Modal för att lägga till nya teammedlemmar */}
      <Modal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        title="Lägg till teammedlem"
      >
        <AddMemberForm onAddMember={handleAddMember} roles={roles} />
      </Modal>

      {/* Bekräftelsemodal för borttagning av teammedlem */}
      <Modal
        isOpen={!!memberToDelete}
        onClose={() => setMemberToDelete(null)}
        title="Bekräfta borttagning"
        footer={
          <>
            <Button onClick={() => setMemberToDelete(null)} variant="secondary">
              Avbryt
            </Button>
            <Button onClick={handleConfirmDelete} variant="danger">
              Ta bort
            </Button>
          </>
        }
      >
        {memberToDelete && (
          <p>
            Vill du verkligen ta bort {memberToDelete.name} från teamet? Det går
            inte att ångra, och alla uppgifter som personen jobbar med just nu
            blir otilldelade.
          </p>
        )}
      </Modal>
    </div>
  );
};

export default MemberList;
