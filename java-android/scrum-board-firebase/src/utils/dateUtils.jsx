// src/utils/dateUtils.jsx
// Använder en enklare approach för att undvika Parcel-problem med date-fns

/**
 * Tidsfunktioner för användarvänliga datum
 *
 * Gör tidsstämplar begripliga med snygga format
 * och relativa texter som "2 timmar sedan".
 */

/**
 * Formaterar tidsstämpel till läsbart datum
 *
 * @param {number} timestamp - Tidsstämpel
 * @returns {string} Format: "2025-05-15 14:30"
 */
export const formatDate = (timestamp) => {
  // Ingen tidsstämpel? Returnera tom sträng
  if (!timestamp) return "";

  // Skapa Date-objekt och formatera
  const date = new Date(timestamp);

  // Formatera med svenska format
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}`;
};

/**
 * Visar relativ tid sedan händelse
 *
 * @param {number} timestamp - Tidsstämpel
 * @returns {string} Text som "2 dagar sedan" eller "Just nu"
 */
export const formatRelativeTime = (timestamp) => {
  // Ingen tidsstämpel? Returnera tom sträng
  if (!timestamp) return "";

  // Beräkna tidsskillnad
  const now = Date.now();
  const diff = now - timestamp;

  // Konvertera till läsbara enheter
  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);

  // Välj lämplig text
  if (days > 0) {
    // Singular/plural för dagar
    return `${days} ${days === 1 ? "dag" : "dagar"} sedan`;
  } else if (hours > 0) {
    // Singular/plural för timmar
    return `${hours} ${hours === 1 ? "timme" : "timmar"} sedan`;
  } else if (minutes > 0) {
    // Singular/plural för minuter
    return `${minutes} ${minutes === 1 ? "minut" : "minuter"} sedan`;
  } else {
    // Mycket nyligen
    return "Just nu";
  }
};
