// domHelpers.js
// Helper functions for DOM manipulation
export function clearElement(element) {
  while (element.firstChild) {
    element.removeChild(element.firstChild);
  }
}
