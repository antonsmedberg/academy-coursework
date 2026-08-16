// SortMenu.js
// Sorting dropdown menu
export default class SortMenu {
  constructor(onSortChange, initialSortValue = 'az') {
    this.onSortChange = onSortChange;
    this.currentSort = initialSortValue;
  }

  render() {
    // Create main container with premium design
    const container = document.createElement('div');
    container.className = 'sort-menu-container';

    // Create icon element with better icon
    const iconWrapper = document.createElement('div');
    iconWrapper.className = 'sort-icon';
    iconWrapper.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="18" height="18"><path fill="none" d="M0 0h24v24H0z"/><path d="M3 18h6v-2H3v2zM3 6v2h18V6H3zm0 7h12v-2H3v2z" fill="currentColor"/></svg>';

    // Create hidden label for accessibility
    const label = document.createElement('label');
    label.textContent = 'Sort by';
    label.className = 'visually-hidden';
    label.setAttribute('for', 'sort-select');

    // Create select with custom styling
    const selectWrapper = document.createElement('div');
    selectWrapper.className = 'select-wrapper';

    const select = document.createElement('select');
    select.id = 'sort-select';
    select.className = 'premium-select';

    // Create options with icons and better descriptions
    select.innerHTML = `
      <option value="az" ${this.currentSort === 'az' ? 'selected' : ''}>A to Z (Title)</option>
      <option value="za" ${this.currentSort === 'za' ? 'selected' : ''}>Z to A (Title)</option>
      <option value="scoreasc" ${this.currentSort === 'scoreasc' ? 'selected' : ''}>Lowest Rating First</option>
      <option value="scoredesc" ${this.currentSort === 'scoredesc' ? 'selected' : ''}>Highest Rating First</option>
    `;

    // Update state and call callback when changed
    // Add tooltip to explain sorting
    const tooltip = document.createElement('div');
    tooltip.className = 'sort-tooltip';
    tooltip.innerHTML = `<div class="tooltip-content">Choose how to order the movies</div>`;

    // Add event listeners
    select.addEventListener('change', (e) => {
      try {
        this.currentSort = e.target.value;
        console.log('[SortMenu] Sort changed to:', this.currentSort);

        // Call the callback if it exists
        if (typeof this.onSortChange === 'function') {
          this.onSortChange(this.currentSort);
        } else {
          console.error('[SortMenu] onSortChange is not a function');
        }

        // Add animation class when changing
        container.classList.add('sort-changed');
        setTimeout(() => container.classList.remove('sort-changed'), 500);
      } catch (error) {
        console.error('[SortMenu] Error handling sort change:', error);
      }
    });

    // Assemble the component - icon closer to dropdown
    selectWrapper.appendChild(select);

    container.appendChild(label); // Hidden label for accessibility
    container.appendChild(iconWrapper); // Icon right before the select
    container.appendChild(selectWrapper);
    container.appendChild(tooltip);

    return container;
  }
}
