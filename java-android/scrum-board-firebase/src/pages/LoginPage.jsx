// src/pages/LoginPage.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import Button from "../components/UI/Button";
import "../styles/pages/LoginPage.css";

/**
 * Inloggningssida med anonym inloggning 🚪
 *
 * Enkel inloggning utan konton.
 * Perfekt för snabb testning.
 */
const LoginPage = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const { loginAnonymously } = useAuth();
  const navigate = useNavigate();

  /**
   * Anonym inloggning 🎭
   *
   * Firebase skapar tillfällig identitet automatiskt.
   */
  const handleAnonymousLogin = async () => {
    setErrorMessage("");
    setIsLoading(true);

    try {
      const result = await loginAnonymously();

      if (result.success) {
        navigate("/"); // Till startsidan
      } else {
        setErrorMessage("Kunde inte logga in. Försök igen.");
      }
    } catch (error) {
      setErrorMessage("Ett fel inträffade. Försök igen senare.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-header">
          <h1>Scrum Board</h1>
          <p>Enkel projekthantering för ditt team</p>
        </div>

        <div className="login-content">
          {errorMessage && (
            <div className="login-error" role="alert">
              <span className="error-icon">⚠️</span>
              {errorMessage}
            </div>
          )}

          <div className="anonymous-login">
            <Button
              onClick={handleAnonymousLogin}
              variant="primary"
              fullWidth={true}
              disabled={isLoading}
            >
              {isLoading ? "Loggar in..." : "Fortsätt utan konto"}
            </Button>
            <p className="anonymous-info">
              Klicka för att komma igång direkt utan att skapa ett konto.
            </p>
          </div>
        </div>

        <div className="login-footer">
          <p>Scrum Board - Ett projekt av Anton Smedberg</p>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
