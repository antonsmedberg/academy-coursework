// src/components/UI/ErrorBoundary.jsx
import React from "react";
import "../../styles/components/ErrorBoundary.css";

/**
 * Säkerhetsnät för appen 🛡️
 *
 * Fångar fel och visar användarvänligt meddelande
 * istället för att appen kraschar.
 */
class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    // Aktivera felläge
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    // Logga fel för debugging
    console.error("ErrorBoundary fångade ett fel:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="error-boundary">
          <h2>Något gick fel</h2>
          <p>Ett oväntat fel uppstod. Försök ladda om sidan.</p>
          <button onClick={() => window.location.reload()}>
            Ladda om sidan
          </button>
        </div>
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
