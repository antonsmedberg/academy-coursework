// SearchResults.js
// Shows search results for movies and people
import MovieCard from '../components/MovieCard.js';
import PersonCard from '../components/PersonCard.js';

export default class SearchResults {
  constructor(movies, people, api, onBackToHome = null, onWatchTrailer = null) {
    this.movies = movies;
    this.people = people;
    this.api = api;
    this.onBackToHome = onBackToHome; // Callback to return to home page
    this.onWatchTrailer = onWatchTrailer; // Callback to watch trailer
    this.moviesPage = 1;
    this.peoplePage = 1;
    this.itemsPerPage = 8;
  }

  render() {
    // Create the main container for search results
    const resultsContainer = document.createElement('div');
    resultsContainer.className = 'search-results-content'; // Add a class for potential styling

    // Create header section for back button and title
    const headerSection = document.createElement('div');
    headerSection.className = 'search-header';

    // Create left side for back button
    const headerLeft = document.createElement('div');
    headerLeft.className = 'search-header-left';

    // Add back button if callback is provided
    if (this.onBackToHome) {
      const backButton = document.createElement('button');
      backButton.className = 'back-button';
      backButton.innerHTML = '<i class="fa-solid fa-arrow-left"></i> Back';
      backButton.onclick = () => this.onBackToHome();
      headerLeft.appendChild(backButton);
    }

    // Create center for title
    const headerCenter = document.createElement('div');
    headerCenter.className = 'search-header-center';

    // Add search results title
    const searchTitle = document.createElement('h2');
    searchTitle.className = 'search-title';
    searchTitle.textContent = 'Search Results';
    headerCenter.appendChild(searchTitle);

    // Create right side (empty for symmetry)
    const headerRight = document.createElement('div');
    headerRight.className = 'search-header-right';

    // Add all sections to header
    headerSection.appendChild(headerLeft);
    headerSection.appendChild(headerCenter);
    headerSection.appendChild(headerRight);

    resultsContainer.appendChild(headerSection);

    if (this.movies.length === 0 && this.people.length === 0) {
      // If App.js already set an error, this might be redundant, but good fallback
      resultsContainer.innerHTML = '<div class="info-message">No results found. Try another search term!</div>';
      return resultsContainer;
    }
    // No success message needed - App.js handles errors

    if (this.movies.length > 0) {
      const heading = document.createElement('h2');
      heading.textContent = 'Movies';
      resultsContainer.appendChild(heading);
      const movieList = document.createElement('div');
      movieList.className = 'card-list';
      // Pagination logic
      const start = (this.moviesPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      const pageMovies = this.movies.slice(start, end);
      pageMovies.forEach(movie => {
        // Create movie card with trailer callback and show details
        const cardObj = new MovieCard(
          movie,
          this.api.getImageUrl(movie.posterPath),
          this.onWatchTrailer, // Pass the callback from App.js
          true // Show details in search results
        );
        const card = cardObj.render();
        movieList.appendChild(card);
      });
      resultsContainer.appendChild(movieList);

      // Add movie pagination right after movie list
      if (this.movies.length > this.itemsPerPage) {
        const moviePagination = this.createPagination('movies', this.movies.length, this.moviesPage);
        moviePagination.className = 'pagination movie-pagination';
        resultsContainer.appendChild(moviePagination);
      }

      // Add a divider between movies and people
      if (this.people.length > 0) {
        const divider = document.createElement('hr');
        divider.className = 'results-divider';
        resultsContainer.appendChild(divider);
      }
    }

    if (this.people.length > 0) {
      const heading = document.createElement('h2');
      heading.textContent = 'People';
      resultsContainer.appendChild(heading);
      const peopleList = document.createElement('div');
      peopleList.className = 'card-list';
      // Pagination logic
      const start = (this.peoplePage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      const pagePeople = this.people.slice(start, end);
      pagePeople.forEach(person => {
        const card = new PersonCard(person, this.api.getImageUrl(person.profilePath)).render();
        peopleList.appendChild(card);
      });
      resultsContainer.appendChild(peopleList);

      // Add people pagination right after people list
      if (this.people.length > this.itemsPerPage) {
        const peoplePagination = this.createPagination('people', this.people.length, this.peoplePage);
        peoplePagination.className = 'pagination people-pagination';
        resultsContainer.appendChild(peoplePagination);
      }
    }

    // No trailer modal needed here - moved to App.js

    return resultsContainer; // Return the populated container
  }

  // Trailer functionality moved to App.js

  // Add pagination function
  createPagination(type, totalItems, currentPage) {
    const paginationDiv = document.createElement('div');
    paginationDiv.className = 'pagination';

    const totalPages = Math.ceil(totalItems / this.itemsPerPage);

    // Previous button
    const prevButton = document.createElement('button');
    prevButton.className = 'pagination-btn prev-btn';
    prevButton.innerHTML = '<i class="fa-solid fa-chevron-left"></i> Previous';
    prevButton.disabled = currentPage === 1;
    prevButton.onclick = () => this.changePage(type, currentPage - 1);
    paginationDiv.appendChild(prevButton);

    // Page numbers
    const pageNumbersContainer = document.createElement('div');
    pageNumbersContainer.className = 'page-numbers';

    // Determine which page numbers to show
    let startPage = Math.max(1, currentPage - 2);
    let endPage = Math.min(totalPages, startPage + 4);

    // Adjust if we're near the end
    if (endPage - startPage < 4 && startPage > 1) {
      startPage = Math.max(1, endPage - 4);
    }

    // First page button if not starting at 1
    if (startPage > 1) {
      const firstPageBtn = document.createElement('button');
      firstPageBtn.className = 'pagination-btn page-number';
      firstPageBtn.textContent = '1';
      firstPageBtn.onclick = () => this.changePage(type, 1);
      pageNumbersContainer.appendChild(firstPageBtn);

      // Add ellipsis if needed
      if (startPage > 2) {
        const ellipsis = document.createElement('span');
        ellipsis.className = 'pagination-ellipsis';
        ellipsis.textContent = '...';
        pageNumbersContainer.appendChild(ellipsis);
      }
    }

    // Page number buttons
    for (let i = startPage; i <= endPage; i++) {
      const pageBtn = document.createElement('button');
      pageBtn.className = 'pagination-btn page-number' + (i === currentPage ? ' active' : '');
      pageBtn.textContent = i.toString();
      pageBtn.onclick = () => this.changePage(type, i);
      pageNumbersContainer.appendChild(pageBtn);
    }

    // Last page button if not ending at totalPages
    if (endPage < totalPages) {
      // Add ellipsis if needed
      if (endPage < totalPages - 1) {
        const ellipsis = document.createElement('span');
        ellipsis.className = 'pagination-ellipsis';
        ellipsis.textContent = '...';
        pageNumbersContainer.appendChild(ellipsis);
      }

      const lastPageBtn = document.createElement('button');
      lastPageBtn.className = 'pagination-btn page-number';
      lastPageBtn.textContent = totalPages.toString();
      lastPageBtn.onclick = () => this.changePage(type, totalPages);
      pageNumbersContainer.appendChild(lastPageBtn);
    }

    paginationDiv.appendChild(pageNumbersContainer);

    // Next button
    const nextButton = document.createElement('button');
    nextButton.className = 'pagination-btn next-btn';
    nextButton.innerHTML = 'Next <i class="fa-solid fa-chevron-right"></i>';
    nextButton.disabled = currentPage === totalPages;
    nextButton.onclick = () => this.changePage(type, currentPage + 1);
    paginationDiv.appendChild(nextButton);

    return paginationDiv;
  }

  // Add page change handler
  changePage(type, newPage) {
    if (type === 'movies') {
      this.moviesPage = newPage;
    } else if (type === 'people') {
      this.peoplePage = newPage;
    }

    // Get the container and replace it with the new render
    const searchRoot = document.getElementById('search-results-root');
    if (searchRoot) {
      searchRoot.innerHTML = '';
      searchRoot.appendChild(this.render());

      // Scroll to top of results
      searchRoot.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }
}
