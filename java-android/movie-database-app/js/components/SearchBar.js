// SearchBar.js
// Search input component
export default class SearchBar {
  constructor(onSearch) {
    this.onSearch = onSearch;
  }

  render() {
    const container = document.createElement('div');
    container.className = 'search-container';

    // Create form
    const form = document.createElement('form');
    form.id = 'search-form';
    form.autocomplete = 'off';

    // Create search input
    const input = document.createElement('input');
    input.type = 'search';
    input.id = 'search-input';
    input.placeholder = 'Search for movies or people...';
    input.setAttribute('aria-label', 'Search movies or people');

    // Create search button
    const button = document.createElement('button');
    button.type = 'submit';
    button.className = 'search-button';
    button.innerHTML = `<i class='fa-solid fa-search'></i> <span class='sr-only'>Search</span>`;

    // Add elements to form
    form.appendChild(input);
    form.appendChild(button);

    // Create error message element (hidden by default)
    const errorMsg = document.createElement('div');
    errorMsg.className = 'search-error';
    errorMsg.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i> Please enter a search term';
    errorMsg.style.display = 'none';

    // Add form submit handler
    form.addEventListener('submit', (e) => {
      e.preventDefault();
      const query = input.value.trim();

      if (query) {
        // Hide error message if it was shown
        errorMsg.style.display = 'none';
        // Call the search callback
        this.onSearch(query);
      } else {
        // Show error message for empty search
        errorMsg.style.display = 'block';
        // Focus the input field
        input.focus();

        // Auto-hide error message after 3 seconds
        setTimeout(() => {
          errorMsg.style.opacity = '0';
          setTimeout(() => {
            errorMsg.style.display = 'none';
            errorMsg.style.opacity = '1';
          }, 300);
        }, 3000);
      }
    });

    // Add elements to container
    container.appendChild(form);
    container.appendChild(errorMsg);

    return container;
  }
}
