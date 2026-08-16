// src/utils/sortUtils.jsx

/**
 * Sorteringsfunktioner för uppgifter 🔄
 *
 * Olika sätt att ordna uppgifter.
 * Alla funktioner skapar nya listor utan att ändra originalet.
 */

/**
 * Sorterar nyast först 🆕
 *
 * @param {Array} tasks - Uppgifter att sortera
 * @returns {Array} Sorterad lista
 */
export const sortByNewest = (tasks) => {
  // Kopia och sortering
  return [...tasks].sort((a, b) => b.creationTimestamp - a.creationTimestamp);
};

/**
 * Äldst först! 👴
 *
 * "First in, first out" - som en kö på banken. Perfekt när du
 * vill beta av uppgifter i den ordning de kom in. Rättvist!
 *
 * @param {Array} tasks - Uppgifterna du vill sortera
 * @returns {Array} En ny lista med de äldsta först
 */
export const sortByOldest = (tasks) => {
  // Gör en kopia först (säkert är säkert)
  // Sen sorterar så det äldsta kommer först
  return [...tasks].sort((a, b) => a.creationTimestamp - b.creationTimestamp);
};

/**
 * A till Ö 📚
 *
 * Som en telefonbok eller bibliotekskatalog! Perfekt när du
 * letar efter något och vet ungefär vad det heter. Funkar
 * fint med våra svenska å, ä, ö också.
 *
 * @param {Array} tasks - Uppgifterna du vill sortera
 * @returns {Array} En ny lista i alfabetisk ordning
 */
export const sortByTitleAsc = (tasks) => {
  // Gör en kopia först (som alltid)
  // Sen sorterar alfabetiskt (localeCompare fixar å, ä, ö)
  return [...tasks].sort((a, b) => a.title.localeCompare(b.title));
};

/**
 * Ö till A (baklänges!) 🔄
 *
 * Ibland är det kul att vända på steken! Kanske ser man
 * mönster man annars missar? Eller bara för att det är kul.
 *
 * @param {Array} tasks - Uppgifterna du vill sortera
 * @returns {Array} En ny lista i omvänd bokstavsordning
 */
export const sortByTitleDesc = (tasks) => {
  // Gör en kopia först (som en god vana)
  // Sen sorterar baklänges, från Ö till A
  return [...tasks].sort((a, b) => b.title.localeCompare(a.title));
};
