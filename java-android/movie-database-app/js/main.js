// main.js
// App entry point
import App from './App.js';

// Add global error handler
window.addEventListener('error', (event) => {
  console.error('Global error caught:', event.error);

  // Display error message to user
  const errorRoot = document.getElementById('error-message-root');
  if (errorRoot) {
    errorRoot.innerHTML = `
      <div style="padding: 1rem; background-color: #ffebee; border: 1px solid #e57373; border-radius: 4px; margin: 1rem 0;">
        <h3 style="color: #c62828; margin-top: 0;">Application Error</h3>
        <p>Something went wrong while loading the application. Please check the console for details.</p>
        <button onclick="window.location.reload()" style="background: #e50914; color: white; border: none; padding: 0.5rem 1rem; border-radius: 4px; cursor: pointer;">
          Refresh Page
        </button>
      </div>
    `;
  }
});

// Initialize app when DOM is loaded
window.addEventListener('DOMContentLoaded', () => {
  console.log('[main] DOM content loaded, initializing app');
  try {
    const app = new App();
    console.log('[main] App instance created');
    app.init().catch(error => {
      console.error('[main] Error during app initialization:', error);
    });
  } catch (error) {
    console.error('[main] Error creating App instance:', error);
  }
});
