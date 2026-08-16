// TabSwitcher.js
// Accessible, robust tab switcher for HomePage
export default class TabSwitcher {
  constructor(tabs, onTabChange = () => {}, initialActiveIdx = 0) {
    this.tabs = tabs; // [{ label: string, content: HTMLElement }]
    this.activeIdx = initialActiveIdx;
    this.tabBtns = [];
    this.tabPanels = [];
    this.onTabChange = onTabChange; // Callback for parent
  }

  render() {
    console.log(`[TabSwitcher] Rendering with ${this.tabs.length} tabs, active index: ${this.activeIdx}`);

    const wrapper = document.createElement('div');
    wrapper.className = 'tab-switcher';
    wrapper.id = 'tab-switcher'; // Add ID for debugging

    // Create tab list (buttons container)
    const tabList = document.createElement('div');
    tabList.className = 'tab-list';
    tabList.setAttribute('role', 'tablist');

    // Create panel
    const tabPanel = document.createElement('div');
    tabPanel.className = 'tab-panel active';
    tabPanel.setAttribute('role', 'tabpanel');
    tabPanel.setAttribute('tabindex', '0');
    tabPanel.id = 'tab-panel'; // Add ID for debugging

    // Ensure activeIdx is valid
    if (this.activeIdx < 0 || this.activeIdx >= this.tabs.length) {
      console.warn(`[TabSwitcher] Invalid activeIdx: ${this.activeIdx}, resetting to 0`);
      this.activeIdx = 0;
    }

    // Create tab buttons
    this.tabBtns = this.tabs.map((tab, idx) => {
      const btn = document.createElement('button');
      btn.className = 'tab-btn' + (idx === this.activeIdx ? ' active' : '');
      btn.textContent = tab.label;
      btn.setAttribute('role', 'tab');
      btn.setAttribute('aria-selected', idx === this.activeIdx ? 'true' : 'false');
      btn.setAttribute('id', `tab-${tab.label.replace(/\s+/g, '-')}`);

      // Add click event listener
      btn.addEventListener('click', () => {
        console.log(`[TabSwitcher] Tab ${idx} clicked`);
        this.setActive(idx, tabList, tabPanel);
      });

      // Add keyboard event listener
      btn.addEventListener('keydown', (e) => {
        this.handleKeydown(e, idx, tabList, tabPanel);
      });

      tabList.appendChild(btn);
      return btn;
    });

    // Associate panel with active tab initially
    if (this.tabs[this.activeIdx]) {
      tabPanel.setAttribute('aria-labelledby', `tab-${this.tabs[this.activeIdx].label.replace(/\s+/g, '-')}`);
    }

    // Initial panel content
    tabPanel.innerHTML = '';

    try {
      if (this.tabs[this.activeIdx] && this.tabs[this.activeIdx].content) {
        console.log(`[TabSwitcher] Setting initial content for tab ${this.activeIdx}`);

        // Append the content directly
        const content = this.tabs[this.activeIdx].content;
        tabPanel.appendChild(content);

        console.log(`[TabSwitcher] Content appended, panel now has ${tabPanel.childNodes.length} children`);
      } else {
        console.error('[TabSwitcher] No content found for active tab:', this.activeIdx);
        // Add a fallback message
        const noContent = document.createElement('p');
        noContent.textContent = 'No content available for this tab.';
        noContent.style.padding = '1rem';
        noContent.style.textAlign = 'center';
        tabPanel.appendChild(noContent);
      }
    } catch (error) {
      console.error('[TabSwitcher] Error setting initial tab content:', error);
      // Add error message
      const errorMsg = document.createElement('p');
      errorMsg.textContent = 'Error loading content. Please try refreshing the page.';
      errorMsg.style.padding = '1rem';
      errorMsg.style.textAlign = 'center';
      errorMsg.style.color = '#e50914';
      tabPanel.appendChild(errorMsg);
    }

    // Append both to wrapper
    wrapper.appendChild(tabList);
    wrapper.appendChild(tabPanel);

    console.log('[TabSwitcher] Render complete');
    return wrapper;
  }

  setActive(idx, tabList, tabPanel) {
    console.log(`[TabSwitcher] Setting active tab to ${idx}`);

    // Validate index
    if (idx < 0 || idx >= this.tabs.length) {
      console.error(`[TabSwitcher] Invalid tab index: ${idx}`);
      return;
    }

    this.activeIdx = idx;

    // Update tab button states
    this.tabBtns.forEach((btn, i) => {
      btn.className = 'tab-btn' + (i === idx ? ' active' : '');
      btn.setAttribute('aria-selected', i === idx ? 'true' : 'false');
    });

    // Update panel association
    if (this.tabs[idx]) {
      tabPanel.setAttribute('aria-labelledby', `tab-${this.tabs[idx].label.replace(/\s+/g, '-')}`);
    }

    // Call the callback with the new tab label and index
    if (this.onTabChange && typeof this.onTabChange === 'function') {
      try {
        this.onTabChange(this.tabs[idx].label, idx);
      } catch (error) {
        console.error('[TabSwitcher] Error in onTabChange callback:', error);
      }
    }

    // Swap panel content
    try {
      console.log('[TabSwitcher] Clearing panel content');
      tabPanel.innerHTML = ''; // Clear previous content

      if (this.tabs[idx] && this.tabs[idx].content) {
        console.log('[TabSwitcher] Appending new content');

        // Create a wrapper to ensure content is properly contained
        const contentWrapper = document.createElement('div');
        contentWrapper.className = 'tab-content-wrapper';
        contentWrapper.appendChild(this.tabs[idx].content);

        // Append the wrapper to the panel
        tabPanel.appendChild(contentWrapper);

        console.log(`[TabSwitcher] Content appended, panel now has ${tabPanel.childNodes.length} children`);
      } else {
        console.error('[TabSwitcher] No content found for tab index:', idx);
        // Add a fallback message
        const noContent = document.createElement('p');
        noContent.textContent = 'No content available for this tab.';
        noContent.style.padding = '1rem';
        noContent.style.textAlign = 'center';
        tabPanel.appendChild(noContent);
      }
    } catch (error) {
      console.error('[TabSwitcher] Error setting tab content:', error);
      // Add error message
      const errorMsg = document.createElement('p');
      errorMsg.textContent = 'Error loading content. Please try refreshing the page.';
      errorMsg.style.padding = '1rem';
      errorMsg.style.textAlign = 'center';
      errorMsg.style.color = '#e50914';
      tabPanel.appendChild(errorMsg);
    }
  }

  handleKeydown(e, idx, tabList, tabPanel) {
    if (e.key === 'ArrowRight' || e.key === 'ArrowDown') {
      e.preventDefault();
      const next = (idx + 1) % this.tabs.length;
      this.tabBtns[next].focus();
      this.setActive(next, tabList, tabPanel);
    } else if (e.key === 'ArrowLeft' || e.key === 'ArrowUp') {
      e.preventDefault();
      const prev = (idx - 1 + this.tabs.length) % this.tabs.length;
      this.tabBtns[prev].focus();
      this.setActive(prev, tabList, tabPanel);
    }
  }
}
