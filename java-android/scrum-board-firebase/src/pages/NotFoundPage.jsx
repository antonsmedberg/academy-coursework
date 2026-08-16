// src/pages/NotFoundPage.jsx
/**
 * 404-sida för felaktiga URL:er
 *
 * Tydligt meddelande och knapp tillbaka till startsidan.
 */
import React from "react";
import { Link } from "react-router-dom";
import Button from "../components/UI/Button.jsx";
import "../styles/pages/NotFoundPage.css";

const NotFoundPage = () => {
  return (
    <div className="not-found-page">
      {/* 404-rubrik */}
      <h1>404</h1>

      {/* Underrubrik */}
      <h2>Sidan kunde inte hittas</h2>

      {/* Förklaring */}
      <p>Tyvärr, sidan du letar efter finns inte eller har flyttats.</p>

      {/* Tillbaka-knapp */}
      <Link to="/">
        <Button variant="primary">Tillbaka till startsidan</Button>
      </Link>
    </div>
  );
};

export default NotFoundPage;
