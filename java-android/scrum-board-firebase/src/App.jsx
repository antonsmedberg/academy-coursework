// src/App.jsx
/**
 * Appens huvudkomponent 🎼
 *
 * Styr routing och navigation.
 * Tre sidor: inloggning, startsida och 404.
 */
import React from "react";
import { HashRouter as Router, Routes, Route } from "react-router-dom";
import AuthGuard from "./components/Auth/AuthGuard.jsx";
import { AuthProvider } from "./hooks/useAuth.jsx";
import { ErrorBoundary } from "react-error-boundary"; // Använd paketet istället

// Direct imports instead of lazy loading
import HomePage from "./pages/HomePage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import NotFoundPage from "./pages/NotFoundPage.jsx";

// Fallback UI för ErrorBoundary
const ErrorFallback = ({ error, resetErrorBoundary }) => (
  <div className="error-boundary">
    <h2>Något gick fel</h2>
    <p>Ett oväntat fel uppstod. Försök ladda om sidan.</p>
    <div className="error-details">
      <p>
        <strong>Felmeddelande:</strong> {error.message}
      </p>
    </div>
    <button onClick={resetErrorBoundary}>Ladda om sidan</button>
  </div>
);

function App() {
  return (
    <ErrorBoundary
      FallbackComponent={ErrorFallback}
      onReset={() => window.location.reload()}
      onError={(error, info) => console.error("App error:", error, info)}
    >
      <AuthProvider>
        <Router>
          <Routes>
            {/* Inloggningssida */}
            <Route path="/login" element={<LoginPage />} />

            {/* Skyddad startsida */}
            <Route
              path="/"
              element={
                <AuthGuard>
                  <HomePage />
                </AuthGuard>
              }
            />

            {/* 404-sida */}
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </Router>
      </AuthProvider>
    </ErrorBoundary>
  );
}

export default App;
