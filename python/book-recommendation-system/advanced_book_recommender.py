# Advanced Fantasy and Sci-Fi Book Recommender
# Features:
# - Enhanced dataset
# - User preference input
# - Cosine similarity for better recommendations

import pandas as pd
import numpy as np
from sklearn.preprocessing import OneHotEncoder, StandardScaler
from sklearn.compose import ColumnTransformer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.pipeline import Pipeline

class BookData:
    """Handles loading and preprocessing of book data."""
    def __init__(self):
        self.books_df = None
        self.feature_matrix = None
        self.preprocessor = None

    def load_data(self):
        """Loads an extended dataset of books."""
        # Extended dataset
        self.books_df = pd.DataFrame({
            'title': [
                'The Hobbit', 'Dune', 'Mistborn', 'Neuromancer', 'The Name of the Wind',
                'The Way of Kings', 'A Game of Thrones', 'The Colour of Magic', 'Assassin\'s Apprentice',
                'The Eye of the World', 'Snow Crash', 'The Blade Itself', 'Gardens of the Moon',
                'The Lies of Locke Lamora', 'The Final Empire', 'The Black Prism', 'Dragonflight'
            ],
            'subgenre': [
                'High Fantasy', 'Sci-Fi', 'Epic Fantasy', 'Cyberpunk', 'Epic Fantasy',
                'Epic Fantasy', 'Epic Fantasy', 'Comic Fantasy', 'Epic Fantasy',
                'Epic Fantasy', 'Cyberpunk', 'Grimdark Fantasy', 'Epic Fantasy',
                'Fantasy Heist', 'Epic Fantasy', 'Sci-Fi Fantasy', 'Sci-Fi'
            ],
            'themes': [
                'Quest, Dragons', 'Politics, Space', 'Magic, Heists', 'AI, Hacking', 'Magic, Music',
                'Magic, War', 'Politics, Dragons', 'Magic, Humor', 'Magic, Assassins',
                'Magic, Prophecy', 'Virtual Reality', 'War, Revenge', 'Magic, Empire',
                'Heists, Friendship', 'Magic, Revolution', 'Dragons, Telepathy', 'AI, Gender'
            ],
            'rating': [4.7, 4.5, 4.6, 4.3, 4.8, 4.7, 4.6, 4.2, 4.4, 4.5, 4.3, 4.4, 4.3, 4.5, 4.6, 4.4, 4.2]
        })
        print("Data loaded successfully.")

    def preprocess_data(self):
        """Prepares the dataset using a preprocessing pipeline."""
        transformer = ColumnTransformer(
            transformers=[
                ('num', StandardScaler(), ['rating']),
                ('cat', OneHotEncoder(handle_unknown='ignore'), ['subgenre', 'themes'])
            ]
        )
        self.preprocessor = transformer
        self.feature_matrix = transformer.fit_transform(self.books_df)
        print("Data preprocessed successfully.")


class RecommendationModel:
    """Uses cosine similarity to recommend books."""
    def __init__(self, book_data):
        self.book_data = book_data

    def get_recommendations(self, user_preferences):
        """Recommends books based on user preferences."""
        user_vector = self.book_data.preprocessor.transform(pd.DataFrame(user_preferences))
        similarity_scores = cosine_similarity(user_vector, self.book_data.feature_matrix)
        top_indices = similarity_scores.argsort()[0][-5:][::-1]
        return self.book_data.books_df.iloc[top_indices]['title'].tolist()


class UserInterface:
    """Handles user interaction."""
    def __init__(self, recommendation_model):
        self.recommendation_model = recommendation_model

    def run(self):
        """Runs the user interface."""
        print("Welcome to the Advanced Book Recommender!")
        user_preferences = {'rating': [4.5]}  # Replace with dynamic user input
        recommendations = self.recommendation_model.get_recommendations(user_preferences)
        
        print("\nBased on your preferences, we recommend:")
        for book in recommendations:
            print(f"- {book}")


def main():
    """Main function to run the advanced recommendation system."""
    book_data = BookData()
    book_data.load_data()
    book_data.preprocess_data()

    recommendation_model = RecommendationModel(book_data)

    ui = UserInterface(recommendation_model)
    ui.run()


if __name__ == "__main__":
    main()
