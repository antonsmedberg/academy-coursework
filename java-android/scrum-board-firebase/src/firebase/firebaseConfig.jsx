// src/firebase/firebaseConfig.jsx
import { initializeApp } from "firebase/app";
import { getDatabase } from "firebase/database";
import { getAuth } from "firebase/auth";

/**
 * Firebase-konfiguration ☁️
 *
 * Alla inställningar för att ansluta till Firebase.
 * OPTIMERING: Bara nödvändiga imports.
 */
const firebaseConfig = {
  apiKey: "AIzaSyAQpjQvbTzyP-RdBU2TJ_5W6_V4k25BNQ4",
  authDomain: "scrum-board-app-f63b8.firebaseapp.com",
  databaseURL:
    "https://scrum-board-app-f63b8-default-rtdb.europe-west1.firebasedatabase.app/",
  projectId: "scrum-board-app-f63b8",
  storageBucket: "scrum-board-app-f63b8.appspot.com",
  messagingSenderId: "1045481371546",
  appId: "1:1045481371546:web:c9c8e2c4c3d7a7c1c9c8e2",
};

// Startar Firebase med konfigurationen
const app = initializeApp(firebaseConfig);

// Anslutning till databasen
const db = getDatabase(app);

// Autentisering
const auth = getAuth(app);

export { db, auth };
