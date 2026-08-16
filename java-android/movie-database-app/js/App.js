// App.js
// Main app logic and controller
import TmdbApi from './api/tmdbApi.js';
import Movie from './models/Movie.js';
import Person from './models/Person.js';
import HomePage from './views/HomePage.js';
import SearchResults from './views/SearchResults.js';
import SearchBar from './components/SearchBar.js';
import SortMenu from './components/SortMenu.js';
import ErrorMessage from './components/ErrorMessage.js';
import { TMDB_API_KEY } from './utils/constants.js';

export default class App {
  constructor() {
    this.api = new TmdbApi(TMDB_API_KEY);
    this.state = {
      currentView: 'home', // 'home' or 'search'
      movies: [], // For search results
      people: [], // For search results
      topRated: [], // For home page
      popular: [], // For home page
      sort: 'az',
      error: null,
    };
  }

  // Helper to sort movies based on state.sort
  sortMovies(movies) {
    if (!movies || !Array.isArray(movies) || movies.length === 0) {
      return [];
    }

    const sortType = this.state.sort;
    let sorted = [...movies];

    // Sort according to the four required options
    try {
      switch(sortType) {
        case 'az': // Alphabetically ascending
          sorted.sort((a, b) => {
            const titleA = a.title || a.name || '';
            const titleB = b.title || b.name || '';
            return titleA.localeCompare(titleB);
          });
          break;

        case 'za': // Alphabetically descending
          sorted.sort((a, b) => {
            const titleA = a.title || a.name || '';
            const titleB = b.title || b.name || '';
            return titleB.localeCompare(titleA);
          });
          break;

        case 'scoreasc': // Score ascending
          sorted.sort((a, b) => {
            const scoreA = typeof a.voteAverage === 'number' ? a.voteAverage :
                          (typeof a.popularity === 'number' ? a.popularity : 0);
            const scoreB = typeof b.voteAverage === 'number' ? b.voteAverage :
                          (typeof b.popularity === 'number' ? b.popularity : 0);
            return scoreA - scoreB;
          });
          break;

        case 'scoredesc': // Score descending
          sorted.sort((a, b) => {
            const scoreA = typeof a.voteAverage === 'number' ? a.voteAverage :
                          (typeof a.popularity === 'number' ? a.popularity : 0);
            const scoreB = typeof b.voteAverage === 'number' ? b.voteAverage :
                          (typeof b.popularity === 'number' ? b.popularity : 0);
            return scoreB - scoreA;
          });
          break;

        default:
          // Default to alphabetical ascending
          sorted.sort((a, b) => {
            const titleA = a.title || a.name || '';
            const titleB = b.title || b.name || '';
            return titleA.localeCompare(titleB);
          });
      }
    } catch (error) {
      console.error('[App] Error sorting movies:', error);
      return movies; // Return unsorted array if sorting fails
    }

    return sorted;
  }

  // Renders the HomePage with current state
  renderHomePage(activeTabIndex = 0) {
    console.log(`[App] renderHomePage called with activeTabIndex: ${activeTabIndex}`);
    this.state.currentView = 'home';

    try {
      // Ensure we have arrays to work with
      if (!Array.isArray(this.state.topRated)) {
        console.warn('[App] topRated is not an array, initializing empty array');
        this.state.topRated = [];
      }

      if (!Array.isArray(this.state.popular)) {
        console.warn('[App] popular is not an array, initializing empty array');
        this.state.popular = [];
      }

      // Sort movies based on current sort setting
      const topRatedSorted = this.sortMovies(this.state.topRated);
      const popularSorted = this.sortMovies(this.state.popular);

      console.log('[App] Sorted movies for rendering:', {
        topRated: topRatedSorted.length,
        popular: popularSorted.length
      });

      // Create HomePage instance with all required callbacks
      this.homePage = new HomePage(
        topRatedSorted,
        popularSorted,
        (sortType) => this.handleSort(sortType),
        (label, index) => {
          if (this.homePage) {
            this.homePage.activeTabIndex = index;
          }
        },
        this.api.getImageUrl.bind(this.api),
        this.state.sort,
        activeTabIndex,
        (movieId) => this.handleWatchTrailer(movieId) // Pass the watch trailer callback
      );

      // Get DOM elements
      const mainRoot = document.getElementById('top-rated-root');
      const searchRoot = document.getElementById('search-results-root');

      if (!mainRoot) {
        console.error('[App] Could not find top-rated-root element');
        throw new Error('Could not find top-rated-root element');
      }

      if (!searchRoot) {
        console.warn('[App] Could not find search-results-root element');
      } else {
        // Hide search results
        searchRoot.innerHTML = '';
        searchRoot.style.display = 'none';
      }

      // Clear and show main content
      mainRoot.innerHTML = '';
      mainRoot.style.display = 'block';

      // Get the rendered content
      console.log('[App] Rendering HomePage content');
      const homePageContent = this.homePage.render();

      if (!homePageContent) {
        console.error('[App] HomePage.render() returned null or undefined');
        throw new Error('HomePage.render() returned null or undefined');
      }

      // Append the homepage content
      mainRoot.appendChild(homePageContent);

      // Verify content was added
      console.log('[App] HomePage rendered successfully. Child nodes:', mainRoot.childNodes.length);

      // Force a reflow to ensure content is displayed
      mainRoot.style.display = 'none';
      mainRoot.offsetHeight; // Force reflow
      mainRoot.style.display = 'block';
    } catch (error) {
      console.error('[App] Error rendering HomePage:', error);
      this.setError('Error displaying movie data. Please refresh the page and try again.');

      // Add a simple fallback UI
      const mainRoot = document.getElementById('top-rated-root');
      if (mainRoot) {
        mainRoot.innerHTML = `
          <div style="padding: 2rem; text-align: center;">
            <h2 style="color: #e50914;">Unable to display movies</h2>
            <p>There was an error rendering the movie content. Please try refreshing the page.</p>
            <button onclick="window.location.reload()" style="background: #e50914; color: white; border: none; padding: 0.5rem 1rem; border-radius: 4px; cursor: pointer; margin-top: 1rem;">
              Refresh Page
            </button>
          </div>
        `;
      }
    }
  }

  // Renders Search Results
  renderSearchResults() {
    this.clearError();
    this.state.currentView = 'search';

    const moviesSorted = this.sortMovies(this.state.movies);
    const peopleSorted = this.state.people;

    // Create SearchResults with back to home and watch trailer callbacks
    const searchResults = new SearchResults(
      moviesSorted,
      peopleSorted,
      this.api,
      () => {
        // Back to home callback
        this.state.movies = [];
        this.state.people = [];
        this.renderHomePage();
      },
      (movieId) => this.handleWatchTrailer(movieId) // Pass the watch trailer callback
    );

    const mainRoot = document.getElementById('top-rated-root');
    const searchRoot = document.getElementById('search-results-root');
    mainRoot.innerHTML = '';
    searchRoot.innerHTML = '';
    mainRoot.style.display = 'none';
    searchRoot.style.display = 'block';
    searchRoot.appendChild(searchResults.render());
  }

  // Handle search submissions
  async handleSearch(query) {
    this.clearError();

    // Handle empty search
    if (!query) {
      this.setError('Please enter a search term');
      return;
    }

    // Show loading message
    this.setError(`Searching for "${query}"...`);

    try {
      // Fetch both movies and people in parallel for better performance
      const [movieResults, personResults] = await Promise.all([
        this.api.searchMovies(query).catch(err => {
          console.error('[App] Movie search error:', err);
          return [];
        }),
        this.api.searchPeople(query).catch(err => {
          console.error('[App] Person search error:', err);
          return [];
        })
      ]);

      console.log(`[App] Search results for "${query}":`, {
        movies: movieResults.length,
        people: personResults.length
      });

      // Process movie results - only keep valid entries with images
      this.state.movies = movieResults
        .filter(movie => movie.title && movie.poster_path)
        .map(movie => new Movie(movie));

      // Process people results - only keep valid entries with images
      this.state.people = personResults
        .filter(person => person.name && person.profile_path)
        .map(person => ({
          id: person.id,
          name: person.name,
          profilePath: person.profile_path,
          popularity: person.popularity,
          department: person.known_for_department || 'Unknown',
          knownFor: (person.known_for || []).slice(0, 3) // Limit to 3 items for consistency
        }));

      // Clear loading message
      this.clearError();

      // Check if we have any results
      if (this.state.movies.length === 0 && this.state.people.length === 0) {
        this.setError(`No results found for "${query}". Try different search terms or check spelling.`);
      }

      // Always render search results, even if empty
      this.renderSearchResults();
    } catch (error) {
      console.error('[App] Search error:', error);
      this.setError('An unexpected error occurred during search. Please try again.');
      this.renderSearchResults();
    }
  }

  // Handle sort changes
  handleSort(sortType) {
    this.state.sort = sortType;
    if (this.state.currentView === 'home') {
      const activeTabIndex = this.homePage ? this.homePage.activeTabIndex : 0;
      this.renderHomePage(activeTabIndex);
    } else if (this.state.currentView === 'search') {
      this.renderSearchResults();
    }
  }

  // Handle watch trailer button click
  async handleWatchTrailer(movieId) {
    try {
      console.log('[App] Fetching trailer for movie ID:', movieId);

      // Show loading message
      this.showTrailerModal('<i class="fa-solid fa-spinner fa-spin"></i> Loading trailer...', null, 'loading');

      // Fetch trailer key
      const trailerKey = await this.api.getMovieTrailer(movieId);

      if (trailerKey) {
        console.log('[App] Trailer found:', trailerKey);
        // Update modal with trailer iframe
        this.showTrailerModal('', trailerKey);
      } else {
        console.warn('[App] No trailer found for movie ID:', movieId);
        this.showTrailerModal(
          '<i class="fa-solid fa-video-slash"></i> No trailer available for this movie.<br><small>Try another movie instead.</small>',
          null,
          'error'
        );
      }
    } catch (error) {
      console.error('[App] Error fetching trailer:', error);
      this.showTrailerModal(
        '<i class="fa-solid fa-circle-exclamation"></i> Failed to load trailer. Please try again later.',
        null,
        'error'
      );
    }
  }

  // Show trailer modal with content
  showTrailerModal(message = '', trailerKey = null, messageType = '') {
    // Get or create modal
    let modal = document.getElementById('trailer-modal');

    if (!modal) {
      // Create modal if it doesn't exist
      modal = document.createElement('div');
      modal.id = 'trailer-modal';
      modal.className = 'trailer-modal';

      // Create modal content
      const modalContent = document.createElement('div');
      modalContent.className = 'trailer-modal-content';

      // Create close button
      const closeBtn = document.createElement('span');
      closeBtn.className = 'trailer-modal-close';
      closeBtn.innerHTML = '&times;';
      closeBtn.onclick = () => this.hideTrailerModal();
      modalContent.appendChild(closeBtn);

      // Create video container
      const videoContainer = document.createElement('div');
      videoContainer.id = 'trailer-video-container';
      modalContent.appendChild(videoContainer);

      // Add modal content to modal
      modal.appendChild(modalContent);

      // Add click handler to close when clicking outside
      modal.onclick = (e) => {
        if (e.target === modal) this.hideTrailerModal();
      };

      // Add modal to body
      document.body.appendChild(modal);
    }

    // Get video container
    const videoContainer = document.getElementById('trailer-video-container');

    // Update content
    if (trailerKey) {
      // Show trailer
      videoContainer.innerHTML = `<iframe src="https://www.youtube.com/embed/${trailerKey}?autoplay=1" frameborder="0" allowfullscreen></iframe>`;
    } else if (message) {
      // Show message with appropriate styling based on message type
      let messageClass = '';

      if (messageType === 'error') {
        messageClass = 'trailer-error';
      } else if (messageType === 'loading') {
        messageClass = 'trailer-loading';
      }

      videoContainer.innerHTML = `
        <div class="trailer-message ${messageClass}">
          ${message}
        </div>
      `;
    }

    // Show modal
    modal.style.display = 'flex';
    // Use requestAnimationFrame to ensure display change is applied before adding visible class
    requestAnimationFrame(() => {
      modal.classList.add('visible');
    });
  }

  // Hide trailer modal
  hideTrailerModal() {
    const modal = document.getElementById('trailer-modal');
    if (modal) {
      // Remove visible class
      modal.classList.remove('visible');

      // Wait for transition to complete before hiding
      modal.addEventListener('transitionend', () => {
        modal.style.display = 'none';
        // Clear video container to stop playback
        const videoContainer = document.getElementById('trailer-video-container');
        if (videoContainer) videoContainer.innerHTML = '';
      }, { once: true });
    }
  }

  setError(message) {
    const errorRoot = document.getElementById('error-message-root');
    errorRoot.innerHTML = '';
    const errorMessage = new ErrorMessage(message);
    errorRoot.appendChild(errorMessage.render());
    errorRoot.scrollIntoView({ behavior: 'smooth', block: 'center' });
  }

  clearError() {
    document.getElementById('error-message-root').innerHTML = '';
  }

  async init() {
    console.log('[App] Initializing application...');

    // Initialize search bar
    try {
      this.searchBar = new SearchBar((query) => this.handleSearch(query));
      const searchBarRoot = document.getElementById('search-bar-root');
      if (!searchBarRoot) {
        console.error('[App] Search bar root element not found');
        throw new Error('Search bar root element not found');
      }
      searchBarRoot.appendChild(this.searchBar.render());
      console.log('[App] Search bar initialized');
    } catch (error) {
      console.error('[App] Error initializing search bar:', error);
    }

    // Show loading message
    this.setError('Loading movie data... Please wait.');
    console.log('[App] Loading message displayed');

    // Initialize with dummy data in case API fails
    this.state.topRated = [
      new Movie({
        id: 1,
        title: 'Sample Movie 1',
        release_date: '2023-01-01',
        overview: 'This is a sample movie to ensure rendering works.',
        poster_path: null,
        vote_average: 8.5
      })
    ];

    this.state.popular = [
      new Movie({
        id: 2,
        title: 'Sample Movie 2',
        release_date: '2023-02-01',
        overview: 'Another sample movie to ensure rendering works.',
        poster_path: null,
        vote_average: 7.8
      })
    ];

    // Try to fetch real data
    let topRatedError = null;
    let popularError = null;

    // Fetch top rated movies
    try {
      console.log('[App] Fetching top rated movies...');
      const topRatedData = await this.api.getTopRatedMovies();
      console.log('[App] Top rated movies fetched:', topRatedData?.length || 0);

      if (topRatedData && Array.isArray(topRatedData) && topRatedData.length > 0) {
        this.state.topRated = topRatedData.map(movie => new Movie(movie));
        console.log('[App] Processed top rated movies:', this.state.topRated.length);
      } else {
        console.warn('[App] No top rated movies returned or invalid format');
      }
    } catch (error) {
      console.error('[App] Error fetching top rated movies:', error);
      topRatedError = error;
    }

    // Fetch popular movies
    try {
      console.log('[App] Fetching popular movies...');
      const popularData = await this.api.getPopularMovies();
      console.log('[App] Popular movies fetched:', popularData?.length || 0);

      if (popularData && Array.isArray(popularData) && popularData.length > 0) {
        this.state.popular = popularData.map(movie => new Movie(movie));
        console.log('[App] Processed popular movies:', this.state.popular.length);
      } else {
        console.warn('[App] No popular movies returned or invalid format');
      }
    } catch (error) {
      console.error('[App] Error fetching popular movies:', error);
      popularError = error;
    }

    // Clear loading message
    this.clearError();
    console.log('[App] Loading message cleared');

    // Render the homepage with whatever data we have
    console.log('[App] Rendering home page with data:', {
      topRated: this.state.topRated.length,
      popular: this.state.popular.length
    });

    try {
      this.renderHomePage();
      console.log('[App] HomePage rendered');
    } catch (error) {
      console.error('[App] Error rendering HomePage:', error);
      this.setError('Error rendering movie data. Please refresh the page and try again.');
    }

    // Show error message if both API calls failed but we're using dummy data
    if (topRatedError && popularError) {
      const errorMessage = 'Failed to load movie data from the API. Showing sample data instead.';
      console.error('[App] API errors:', errorMessage);
      this.setError(errorMessage + ' Please check your connection and refresh to see real data.');
    }

    return true; // Signal successful initialization
  }
}
