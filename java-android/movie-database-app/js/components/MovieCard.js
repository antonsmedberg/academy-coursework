// MovieCard.js
// Renders a movie card
export default class MovieCard {
  constructor(movie, imageUrl, onWatchTrailer = null, showDetails = false) {
    this.movie = movie;
    this.imageUrl = imageUrl;
    this.onWatchTrailer = onWatchTrailer;
    this.showDetails = showDetails; // Whether to show movie details (description, etc.)
  }

  render() {
    const card = document.createElement('div');
    card.className = 'movie-card';

    // Create image container
    const imageContainer = document.createElement('div');
    imageContainer.className = 'card-image-container';

    // Create image element
    const image = document.createElement('img');
    image.className = 'card-image';

    // Add loading attribute for better performance
    image.loading = 'lazy';

    // Set image source
    if (this.imageUrl && !this.imageUrl.includes('null')) {
      // Preload image to get dimensions
      const preloadImg = new Image();
      preloadImg.onload = () => {
        // If image is very wide or very tall, adjust container class
        const aspectRatio = preloadImg.width / preloadImg.height;
        if (aspectRatio > 0.8) {
          imageContainer.classList.add('wide-poster');
        } else if (aspectRatio < 0.6) {
          imageContainer.classList.add('tall-poster');
        }
        // Set the actual image source after preloading
        image.src = this.imageUrl;
      };
      preloadImg.onerror = () => {
        image.src = 'images/no-poster.png';
        imageContainer.classList.add('no-image');
      };
      preloadImg.src = this.imageUrl;
    } else {
      // Use a better poster placeholder
      image.src = 'images/no-poster.png';
      imageContainer.classList.add('no-image');
    }

    image.alt = this.movie.title || 'Movie poster';

    // Handle image loading errors
    image.onerror = () => {
      image.src = 'images/no-poster.png';
      imageContainer.classList.add('no-image');
    };

    imageContainer.appendChild(image);
    card.appendChild(imageContainer);

    const movieInfo = document.createElement('div');
    movieInfo.className = 'movie-info';
    movieInfo.innerHTML = `<h3>${this.movie.title}</h3>`;
    // Only show description if showDetails is true
    if (this.showDetails) {
      // Use overview property if available, fallback to description for backward compatibility
      const overview = this.movie.overview || this.movie.description;
      if (overview && overview.trim() !== '') {
        const descP = document.createElement('p');
        descP.className = 'movie-desc';
        descP.textContent = overview;
        movieInfo.appendChild(descP);
      }
    }

    // Add movie info section with date and rating
    const infoSection = document.createElement('div');
    infoSection.className = 'movie-info-section';

    // Add release date
    if (this.movie.releaseDate) {
      const dateDiv = document.createElement('div');
      dateDiv.className = 'movie-date';
      dateDiv.innerHTML = `<i class="fa-solid fa-calendar"></i> ${this.movie.releaseDate}`;
      infoSection.appendChild(dateDiv);
    }

    // Add rating with star icon
    const ratingDiv = document.createElement('div');
    ratingDiv.className = 'movie-rating';
    ratingDiv.innerHTML = `<i class="fa-solid fa-star"></i> ${this.movie.voteAverage ? this.movie.voteAverage.toFixed(1) : 'N/A'}`;
    infoSection.appendChild(ratingDiv);

    // Add info section to movie info
    movieInfo.appendChild(infoSection);

    // Create empty details div for spacing
    const detailsDiv = document.createElement('div');
    detailsDiv.className = 'movie-details';

    // Add details at the bottom of the card (after content)
    card.appendChild(movieInfo);
    card.appendChild(detailsDiv);

    // Add Watch Trailer button logic
    if (this.onWatchTrailer) {
      // Create trailer button - assume all movies have trailers
      // The API will handle the case when a trailer isn't available
      const button = document.createElement('button');
      button.className = 'trailer-btn';
      button.innerHTML = '<i class="fa-solid fa-play"></i> Trailer';
      button.title = 'Watch trailer';

      button.onclick = (e) => {
        e.stopPropagation(); // Prevent card click if any
        this.onWatchTrailer(this.movie.id);
      };

      card.appendChild(button);
    }

    return card;
  }
}
