/**
 * Startpunkten för hela appen 🚀
 *
 * Väntar på att sidan laddats klart innan React tar över.
 * Som att vänta på att kaffet bryggts innan man häller upp.
 */
import React from "react";
import { createRoot } from "react-dom/client";
import "./styles/base/index.css"; // Globala stilar för hela appen
import App from "./App.jsx"; // Min huvudkomponent
import { initPerformanceMonitoring } from "./utils/performanceUtils.jsx"; // Prestandaövervakning

// Hittar React:s hem i DOM:en
const rootElement = document.getElementById("root");

if (rootElement) {
  // Skapar React-root med moderna API:et
  const root = createRoot(rootElement);

  // Startar prestandamätning
  initPerformanceMonitoring();

  // StrictMode i utveckling för att hitta problem tidigt
  if (process.env.NODE_ENV === "development") {
    root.render(
      <React.StrictMode>
        <App />
      </React.StrictMode>
    );
  } else {
    // Produktion utan extra kontroller
    root.render(<App />);
  }
} else {
  // Root-element saknas
  console.error("Root element not found");
}
