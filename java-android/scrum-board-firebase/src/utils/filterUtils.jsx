// src/utils/filterUtils.jsx

/**
 * Filtreringsverktyg för uppgifter 🔍
 *
 * Hjälper dig hitta rätt uppgifter snabbt.
 * Filtrera på person, kategori eller status.
 */

/**
 * Visar bara en persons uppgifter 👤
 *
 * @param {Array} tasks - Alla uppgifter
 * @param {string} memberId - Vilken person
 * @returns {Array} Personens uppgifter
 */
export const filterByMember = (tasks, memberId) => {
  // Ingen person vald? Visa allt
  if (!memberId || !Array.isArray(tasks) || tasks.length === 0) return tasks;

  // Bara den personens uppgifter
  return tasks.filter((task) => task?.assignedToMemberId === memberId);
};

/**
 * Visar bara en typ av uppgifter 🎨
 *
 * @param {Array} tasks - Alla uppgifter
 * @param {string} category - Vilken kategori (frontend/backend/ux)
 * @returns {Array} Uppgifter av den typen
 */
export const filterByCategory = (tasks, category) => {
  // Ingen kategori vald? Visa allt
  if (!category || !Array.isArray(tasks) || tasks.length === 0) return tasks;

  // Bara den kategorin
  return tasks.filter((task) => task?.category === category);
};

/**
 * Visar bara uppgifter med viss status 📋
 *
 * @param {Array} tasks - Alla uppgifter
 * @param {string} status - Vilken status (Nytt/Pågående/Klar)
 * @returns {Array} Uppgifter med den statusen
 */
export const filterByStatus = (tasks, status) => {
  // Ingen status vald? Visa allt
  if (!status || !Array.isArray(tasks) || tasks.length === 0) return tasks;

  // Bara den statusen
  return tasks.filter((task) => task?.status === status);
};

/**
 * Tar bort arkiverade uppgifter 🗑️
 *
 * Säkerhetsnät för gamla arkiverade uppgifter.
 *
 * @param {Array} tasks - Alla uppgifter
 * @returns {Array} Bara aktiva uppgifter
 */
export const filterOutArchived = (tasks) => {
  // Inget att filtrera? Returnera som det är
  if (!Array.isArray(tasks) || tasks.length === 0) return tasks;

  // Bara aktiva uppgifter
  return tasks.filter((task) => !task?.isArchived);
};
