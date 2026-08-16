// src/components/UI/Button.jsx
import React from "react";
import "../../styles/components/Button.css";

/**
 * Universell knappkomponent 🔘
 *
 * Flexibel knapp med olika stilar och storlekar.
 * Stöder ikoner och fullbredd.
 *
 * @param {ReactNode} children - Knapptext
 * @param {Function} onClick - Klick-hanterare
 * @param {string} type - HTML-typ
 * @param {string} variant - Stil (primary/secondary/danger)
 * @param {string} size - Storlek (small/medium/large)
 * @param {boolean} disabled - Inaktiverad
 * @param {boolean} fullWidth - Full bredd
 * @param {ReactNode} iconLeft - Vänster ikon
 * @param {ReactNode} iconRight - Höger ikon
 * @param {string} className - Extra CSS-klasser
 */
const Button = ({
  children,
  onClick,
  type = "button",
  variant = "primary",
  size = "medium",
  disabled = false,
  fullWidth = false,
  iconLeft = null,
  iconRight = null,
  className = "",
}) => {
  // Kontrollerar om ikoner finns
  const hasIcon = iconLeft || iconRight;

  // Bygger CSS-klasser
  const buttonClasses = `
    button
    button-${variant}
    button-${size}
    ${fullWidth ? "button-full-width" : ""}
    ${hasIcon ? "button-with-icon" : ""}
    ${className}
  `
    .trim()
    .replace(/\s+/g, " ");

  return (
    <button
      type={type}
      className={buttonClasses}
      onClick={onClick}
      disabled={disabled}
      aria-disabled={disabled} // För skärmläsare
    >
      {/* Vänster ikon */}
      {iconLeft && <span className="button-icon-left">{iconLeft}</span>}

      {/* Knapptext */}
      {children}

      {/* Höger ikon */}
      {iconRight && <span className="button-icon-right">{iconRight}</span>}
    </button>
  );
};

export default Button;
