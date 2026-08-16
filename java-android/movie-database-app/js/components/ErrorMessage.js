// ErrorMessage.js
// Enhanced error message display with icon and better user guidance
export default class ErrorMessage {
  constructor(message) {
    this.message = message;
  }

  render() {
    // Create container
    const container = document.createElement('div');
    container.className = 'error-message';

    // Create icon element
    const icon = document.createElement('div');
    icon.className = 'error-icon';
    icon.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"><path fill="none" d="M0 0h24v24H0z"/><path d="M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10zm-1-7v2h2v-2h-2zm0-8v6h2V7h-2z" fill="currentColor"/></svg>';

    // Create message element
    const messageEl = document.createElement('div');
    messageEl.className = 'error-text';

    // Check if this is a loading message
    if (this.message.includes('Loading')) {
      messageEl.innerHTML = `<span class="loading-spinner"></span> ${this.message}`;
    } else if (this.message.includes('refresh')) {
      // Add a refresh button for errors that suggest refreshing
      messageEl.innerHTML = `${this.message} <button class="refresh-btn" onclick="window.location.reload()">Refresh Now</button>`;
    } else if (this.message.includes('No results')) {
      // Add a special styling for no results message
      messageEl.innerHTML = `<i class="fa-solid fa-search"></i> ${this.message}`;
    } else {
      messageEl.textContent = this.message;
    }

    // Add dismiss button
    const dismissBtn = document.createElement('button');
    dismissBtn.className = 'error-dismiss';
    dismissBtn.innerHTML = '&times;';
    dismissBtn.setAttribute('aria-label', 'Dismiss error');
    dismissBtn.addEventListener('click', () => {
      container.remove();
    });

    // Assemble the error message
    container.appendChild(icon);
    container.appendChild(messageEl);
    container.appendChild(dismissBtn);

    return container;
  }
}
