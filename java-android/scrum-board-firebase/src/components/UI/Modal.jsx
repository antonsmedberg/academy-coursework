// src/components/UI/Modal.jsx
/**
 * Återanvändbar modalkomponent
 *
 * Flexibel dialog med React Portal.
 * Stöder olika storlekar, Escape-stängning,
 * förhindrar bakgrundsscrollning.
 */
import React, { useEffect } from "react";
import ReactDOM from "react-dom";
import "../../styles/components/Modal.css";
import Button from "./Button.jsx";

const Modal = ({
  isOpen,
  onClose,
  title,
  children,
  footer,
  size = "medium",
}) => {
  /**
   * Hanterar Escape-tangent och scrollning
   */
  useEffect(() => {
    // Stäng med Escape
    const handleEscape = (e) => {
      if (e.key === "Escape" && isOpen) {
        onClose();
      }
    };

    // Registrera event-lyssnare
    window.addEventListener("keydown", handleEscape);

    // Förhindra bakgrundsscrollning
    if (isOpen) {
      document.body.style.overflow = "hidden";
    }

    // Städa upp vid avmontering
    return () => {
      window.removeEventListener("keydown", handleEscape);
      document.body.style.overflow = "auto";
    };
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  /**
   * Förhindrar bubbling av klickhändelser
   */
  const handleModalClick = (e) => {
    e.stopPropagation();
  };

  // React Portal för korrekt rendering
  return ReactDOM.createPortal(
    <div className="modal-overlay" onClick={onClose}>
      {/* Modal-container */}
      <div className={`modal modal-${size}`} onClick={handleModalClick}>
        {/* Header */}
        <div className="modal-header">
          <h2 className="modal-title">{title}</h2>
          <button className="modal-close" onClick={onClose} aria-label="Stäng">
            ×
          </button>
        </div>

        {/* Innehåll */}
        <div className="modal-content">{children}</div>

        {/* Footer */}
        {footer && <div className="modal-footer">{footer}</div>}
      </div>
    </div>,
    document.body // Renderar i body för korrekt z-index
  );
};

export default Modal;
