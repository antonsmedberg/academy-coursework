# Improved Fantasy and Sci-Fi Book Recommendation System
# Features:
# - Refactored code for readability
# - Enhanced error handling
# - Clearer and more structured comments

import pandas as pd
import numpy as np
from sklearn.neighbors import NearestNeighbors
from sklearn.preprocessing import OneHotEncoder

class BookData:
    """Handles loading and preprocessing of book data."""
    def __init__(self):
        self.books_df = None
        self.feature_matrix = None
        self.encoder = None

    def load_data(self):
        """Loads book data into a DataFrame."""
        # Hardcoded dataset; replace with database or API for production
        self.books_df = pd.DataFrame({
            'title': ['The Hobbit', 'Dune', 'Mistborn', 'Neuromancer', 'The Name of the Wind'],
            'author': ['J.R.R. Tolkien', 'Frank Herbert', 'Brandon Sanderson', 'William Gibson', 'Patrick Rothfuss'],
            'subgenre': ['High Fantasy', 'Sci-Fi', 'Epic Fantasy', 'Cyberpunk', 'Epic Fantasy'],
            'themes': ['Quest, Dragons', 'Politics, Space', 'Magic, Heists', 'AI, Hacking', 'Magic, Music'],
            'length': [310, 412, 541, 271, 662],
            'year': [1937, 1965, 2006, 1984, 2007],
            'rating': [4.7, 4.5, 4.6, 4.3, 4.8]
        })
        print("Data loaded successfully.")

    def preprocess_data(self):
        """Encodes and preprocesses book data into feature matrix."""
        self.encoder = OneHotEncoder(sparse_output=False, handle_unknown='ignore')
        
        # Encode subgenre
        subgenre_encoded = self.encoder.fit_transform(self.books_df[['subgenre']])
        subgenre_columns = self.encoder.get_feature_names_out(['subgenre'])
        
        # Split and encode themes
        themes_split = self.books_df['themes'].str.split(', ', expand=True)
        themes_encoded = self.encoder.fit_transform(themes_split.fillna('Unknown'))
        themes_columns = self.encoder.get_feature_names_out()

        # Combine encoded features with ratings
        self.feature_matrix = np.hstack((subgenre_encoded, themes_encoded, self.books_df[['rating']].values))
        print("Data preprocessed successfully.")


class RecommendationModel:
    """Builds and uses the k-NN model for recommendations."""
    def __init__(self, book_data):
        self.book_data = book_data
        self.model = None

    def train_model(self):
        """Trains the Nearest Neighbors model."""
        self.model = NearestNeighbors(n_neighbors=3, metric='euclidean')
        self.model.fit(self.book_data.feature_matrix)
        print("Model trained successfully.")

    def get_recommendations(self, book_title):
        """Finds similar books based on the given book title."""
        book_index = self.book_data.books_df[self.book_data.books_df['title'] == book_title].index
        if len(book_index) == 0:
            return "Book not found in the database.", []

        # Find nearest neighbors
        book_features = self.book_data.feature_matrix[book_index]
        distances, indices = self.model.kneighbors(book_features)
        recommended_books = self.book_data.books_df.iloc[indices[0][1:]]['title'].tolist()
        
        return recommended_books, indices[0][1:]


class UserInterface:
    """Handles user interaction with the system."""
    def __init__(self, recommendation_model):
        self.recommendation_model = recommendation_model

    def run(self):
        """Runs the CLI for the recommender system."""
        print("Welcome to the Fantasy Book Recommender!")
        favorite_book = input("Enter the title of a fantasy book you enjoy: ").strip()
        recommendations, _ = self.recommendation_model.get_recommendations(favorite_book)
        
        if recommendations:
            print("\nBased on your favorite book, we recommend:")
            for book in recommendations:
                print(f"- {book}")
        else:
            print("Sorry, no recommendations found.")


def main():
    """Main function to run the recommendation system."""
    book_data = BookData()
    book_data.load_data()
    book_data.preprocess_data()

    recommendation_model = RecommendationModel(book_data)
    recommendation_model.train_model()

    ui = UserInterface(recommendation_model)
    ui.run()


if __name__ == "__main__":
    main()
