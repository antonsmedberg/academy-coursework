// src/components/UI/Notification.jsx
/**
 * Popup-meddelanden som försvinner automatiskt
 *
 * Fyra typer: info, success, warning, error.
 */
import React, { useState, useEffect } from "react";
import "../../styles/components/Notification.css";

const Notification = ({ message, type = "info", duration = 5000, onClose }) => {
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    // Inget meddelande? Gör inget
    if (!message) return;

    // Visa meddelandet
    setIsVisible(true);

    // Försvinner automatiskt
    const timer = setTimeout(() => {
      setIsVisible(false);
      // Vänta på animation innan borttagning
      if (onClose) setTimeout(onClose, 300);
    }, duration);

    // Städa upp
    return () => clearTimeout(timer);
  }, [message, duration, onClose]);

  if (!message) return null;

  // När du klickar på krysset
  const handleClose = () => {
    setIsVisible(false); // Starta försvinn-animationen
    // Vänta på animationen innan vi tar bort helt
    if (onClose) setTimeout(onClose, 300);
  };

  return (
    <div
      className={`notification notification-${type} ${
        isVisible ? "show" : "hide"
      }`}
    >
      <div className="notification-content">
        {/* Ikoner för olika typer */}
        {type === "success" && <span className="notification-icon">✓</span>}
        {type === "error" && <span className="notification-icon">✗</span>}
        {type === "warning" && <span className="notification-icon">⚠</span>}
        {type === "info" && <span className="notification-icon">ℹ</span>}
        {/* Meddelandet */}
        <p className="notification-message">{message}</p>
      </div>
      {/* Stäng-knapp */}
      <button
        className="notification-close"
        onClick={handleClose}
        aria-label="Stäng"
      >
        ×
      </button>
    </div>
  );
};

export default Notification;
