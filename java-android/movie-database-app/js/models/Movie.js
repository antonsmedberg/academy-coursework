// Movie.js
// Movie data model
export default class Movie {
  constructor({ id, title, release_date, overview, poster_path, backdrop_path, popularity, vote_average }) {
    this.id = id;
    this.title = title || 'Unknown Title';
    this.releaseDate = release_date || 'Unknown Date';
    this.overview = overview || '';
    this.description = overview || ''; // Keep for backward compatibility
    this.posterPath = poster_path || backdrop_path || null;
    this.popularity = popularity || 0;
    this.voteAverage = vote_average || 0;
  }
}
