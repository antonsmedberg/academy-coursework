// HomePage.js
// Shows top lists (Top Rated and Popular Movies)
import MovieCard from '../components/MovieCard.js';
import TabSwitcher from '../components/TabSwitcher.js';
import SortMenu from '../components/SortMenu.js';

export default class HomePage {
  constructor(topRatedMovies, popularMovies, handleSort, onTabChange, getImageUrlFn, currentSort = 'az', activeTabIndex = 0, onWatchTrailer = null) {
    console.log(`[HomePage] Constructor - Received activeTabIndex: ${activeTabIndex}`);
    this.topRatedMovies = topRatedMovies;
    this.popularMovies = popularMovies;
    this.handleSort = handleSort;
    this.onTabChange = onTabChange; // Store the tab change callback
    this.getImageUrl = getImageUrlFn; // Store the image URL function
    this.currentSort = currentSort; // Store the current sort value
    this.activeTabIndex = activeTabIndex; // Use provided active tab index
    this.onWatchTrailer = onWatchTrailer; // Store the watch trailer callback
    this.titleElement = document.createElement('h2');
    this.titleElement.className = 'tab-title';
    this.titleElement.setAttribute('aria-live', 'polite');
  }

  updateTitle(newTitle) {
    this.titleElement.textContent = newTitle;
  }

  render() {
    console.log('[HomePage] Rendering with movies:', {
      topRated: this.topRatedMovies?.length || 0,
      popular: this.popularMovies?.length || 0
    });

    try {
      // Create container for the home page content
      const homePageWrapper = document.createElement('div');
      homePageWrapper.className = 'homepage-content-wrapper';
      homePageWrapper.id = 'homepage-wrapper'; // Add ID for debugging

      // Build content for each tab
      const topRatedList = document.createElement('div');
      topRatedList.className = 'card-list';
      topRatedList.id = 'top-rated-list'; // Add ID for debugging

      // Check if we have top rated movies
      if (this.topRatedMovies && Array.isArray(this.topRatedMovies) && this.topRatedMovies.length > 0) {
        console.log('[HomePage] Rendering top rated movies:', this.topRatedMovies.length);

        this.topRatedMovies.forEach((movie, index) => {
          try {
            console.log(`[HomePage] Rendering top rated movie ${index + 1}:`, movie.title);

            // Use the MovieCard component with the trailer callback
            const imageUrl = this.getImageUrl(movie.posterPath);
            const card = new MovieCard(movie, imageUrl, this.onWatchTrailer).render();

            // Add to list
            topRatedList.appendChild(card);
          } catch (error) {
            console.error('[HomePage] Error rendering top rated movie card:', error);
          }
        });
      } else {
        console.warn('[HomePage] No top rated movies to display');
        const noMoviesMsg = document.createElement('p');
        noMoviesMsg.className = 'no-results-message';
        noMoviesMsg.textContent = 'No top rated movies available at the moment. Please try again later.';
        topRatedList.appendChild(noMoviesMsg);
      }

      const popularList = document.createElement('div');
      popularList.className = 'card-list';
      popularList.id = 'popular-list'; // Add ID for debugging

      // Check if we have popular movies
      if (this.popularMovies && Array.isArray(this.popularMovies) && this.popularMovies.length > 0) {
        console.log('[HomePage] Rendering popular movies:', this.popularMovies.length);

        this.popularMovies.forEach((movie, index) => {
          try {
            console.log(`[HomePage] Rendering popular movie ${index + 1}:`, movie.title);

            // Use the MovieCard component with the trailer callback
            const imageUrl = this.getImageUrl(movie.posterPath);
            const card = new MovieCard(movie, imageUrl, this.onWatchTrailer).render();

            // Add to list
            popularList.appendChild(card);
          } catch (error) {
            console.error('[HomePage] Error rendering popular movie card:', error);
          }
        });
      } else {
        console.warn('[HomePage] No popular movies to display');
        const noMoviesMsg = document.createElement('p');
        noMoviesMsg.className = 'no-results-message';
        noMoviesMsg.textContent = 'No popular movies available at the moment. Please try again later.';
        popularList.appendChild(noMoviesMsg);
      }

      // Create containers for tab content
      const topRatedContainer = document.createElement('div');
      topRatedContainer.className = 'tab-content';
      topRatedContainer.id = 'top-rated-container'; // Add ID for debugging
      topRatedContainer.appendChild(topRatedList);

      const popularContainer = document.createElement('div');
      popularContainer.className = 'tab-content';
      popularContainer.id = 'popular-container'; // Add ID for debugging
      popularContainer.appendChild(popularList);

      // Define tabs configuration
      const tabs = [
        { label: 'Top 10 Highest Rated', content: topRatedContainer },
        { label: 'Top 10 Most Popular', content: popularContainer }
      ];

      // Create SortMenu with callback and current sort value
      const sortMenu = new SortMenu(this.handleSort, this.currentSort).render();

      // Set initial title based on the actual active tab index
      const safeActiveIndex = (this.activeTabIndex >= 0 && this.activeTabIndex < tabs.length)
        ? this.activeTabIndex
        : 0;
      this.updateTitle(tabs[safeActiveIndex].label);

      // Create TabSwitcher with callback and pass the active tab index
      console.log(`[HomePage] render - Passing activeTabIndex to TabSwitcher: ${safeActiveIndex}`);
      const tabSwitcher = new TabSwitcher(tabs, (label, index) => {
        this.updateTitle(label);
        this.activeTabIndex = index; // Store the active tab index
        if (this.onTabChange) this.onTabChange(label, index);
      }, safeActiveIndex).render();

      // Append elements in correct order
      homePageWrapper.appendChild(sortMenu); // 1. Sort Menu at top
      homePageWrapper.appendChild(this.titleElement); // 2. Title above tabs
      homePageWrapper.appendChild(tabSwitcher); // 3. Tab Switcher

      console.log('[HomePage] Render complete, returning wrapper');
      return homePageWrapper;
    } catch (error) {
      console.error('[HomePage] Error in render method:', error);

      // Return a simple error message if rendering fails
      const errorElement = document.createElement('div');
      errorElement.style.padding = '2rem';
      errorElement.style.textAlign = 'center';
      errorElement.style.color = '#e50914';
      errorElement.innerHTML = '<h3>Error Rendering Content</h3><p>There was a problem displaying the movie content. Please try refreshing the page.</p>';
      return errorElement;
    }
  }
}
