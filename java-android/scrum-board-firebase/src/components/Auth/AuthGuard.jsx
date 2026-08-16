// src/components/Auth/AuthGuard.jsx
import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";
import "../../styles/components/AuthGuard.css";

/**
 * Skyddar sidor från obehöriga 🛡️
 *
 * Kontrollerar inloggning innan åtkomst.
 * Omdirigerar till login om ej inloggad.
 * Kommer ihåg destination för senare.
 */
const AuthGuard = ({ children }) => {
  const { currentUser, loading } = useAuth();
  const location = useLocation();

  // Väntar på autentiseringskontroll
  if (loading) {
    return (
      <div className="auth-loading">
        <div className="loading-spinner"></div>
        <p>Laddar...</p>
      </div>
    );
  }

  // Ej inloggad? Omdirigera till login
  // Sparar destination för senare
  if (!currentUser) {
    return <Navigate to="/login" state={{ from: location.pathname }} replace />;
  }

  // Inloggad - visa skyddat innehåll
  return children;
};

export default AuthGuard;
