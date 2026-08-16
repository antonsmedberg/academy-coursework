// tmdbApi.js
// Handles all TMDB API requests
import { TMDB_BASE_URL, TMDB_IMAGE_BASE_URL } from '../utils/constants.js';

export default class TmdbApi {
  constructor(apiKey) {
    console.log('[TmdbApi] Constructor - initializing with API key');
    this.apiKey = apiKey;
    this.baseUrl = TMDB_BASE_URL;
    this.imageBaseUrl = TMDB_IMAGE_BASE_URL;
  }

  async getTopRatedMovies() {
    try {
      const url = `${this.baseUrl}/movie/top_rated?api_key=${this.apiKey}&language=en-US&page=1`;
      const res = await fetch(url);
      if (!res.ok) {
        console.error(`API Error: ${res.status} - ${res.statusText}`);
        throw new Error(`Failed to fetch top rated movies: ${res.status} ${res.statusText}`);
      }
      const data = await res.json();
      return data.results.slice(0, 10);
    } catch (error) {
      console.error('Error fetching top rated movies:', error);
      throw error;
    }
  }

  async getPopularMovies() {
    try {
      const url = `${this.baseUrl}/movie/popular?api_key=${this.apiKey}&language=en-US&page=1`;
      const res = await fetch(url);
      if (!res.ok) {
        console.error(`API Error: ${res.status} - ${res.statusText}`);
        throw new Error(`Failed to fetch popular movies: ${res.status} ${res.statusText}`);
      }
      const data = await res.json();
      return data.results.slice(0, 10);
    } catch (error) {
      console.error('Error fetching popular movies:', error);
      throw error;
    }
  }

  async searchMovies(query) {
    try {
      const url = `${this.baseUrl}/search/movie?api_key=${this.apiKey}&language=en-US&query=${encodeURIComponent(query)}`;
      const res = await fetch(url);
      if (!res.ok) {
        console.error(`API Error: ${res.status} - ${res.statusText}`);
        throw new Error(`Failed to search movies: ${res.status} ${res.statusText}`);
      }
      const data = await res.json();
      return data.results;
    } catch (error) {
      console.error('Error searching movies:', error);
      throw error;
    }
  }

  async searchPeople(query) {
    try {
      const url = `${this.baseUrl}/search/person?api_key=${this.apiKey}&language=en-US&query=${encodeURIComponent(query)}`;
      const res = await fetch(url);
      if (!res.ok) {
        console.error(`API Error: ${res.status} - ${res.statusText}`);
        throw new Error(`Failed to search people: ${res.status} ${res.statusText}`);
      }
      const data = await res.json();
      return data.results;
    } catch (error) {
      console.error('Error searching people:', error);
      throw error;
    }
  }

  getImageUrl(path) {
    // Check if path exists and is not null/undefined/empty
    if (path && typeof path === 'string' && path.trim() !== '') {
      return `${this.imageBaseUrl}${path}`;
    }

    // Return a placeholder image URL
    return 'https://via.placeholder.com/300x450?text=No+Image';
  }

  async getMovieTrailer(movieId) {
    try {
      const url = `${this.baseUrl}/movie/${movieId}/videos?api_key=${this.apiKey}&language=en-US`;
      const res = await fetch(url);
      if (!res.ok) {
        console.error(`API Error: ${res.status} - ${res.statusText}`);
        throw new Error(`Failed to fetch trailer: ${res.status} ${res.statusText}`);
      }
      const data = await res.json();

      // Find the first YouTube trailer
      const trailer = data.results.find(v => v.type === 'Trailer' && v.site === 'YouTube');

      // If no YouTube trailer found, try to find any video
      if (!trailer && data.results.length > 0) {
        console.log('No YouTube trailer found, using first available video');
        return data.results[0].key;
      }

      return trailer ? trailer.key : null;
    } catch (error) {
      console.error('Error fetching movie trailer:', error);
      throw error;
    }
  }
}
