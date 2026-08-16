// src/hooks/useAuth.jsx
import { useState, useEffect, createContext, useContext } from "react";

// OPTIMERING: Bara använda Firebase-funktioner
import {
  signOut,
  onAuthStateChanged,
  signInAnonymously,
  deleteUser,
} from "firebase/auth";
import { auth } from "../firebase/firebaseConfig.jsx";

/**
 * Hanterar inloggning och autentisering 🚪
 *
 * Anonyma inloggningar utan krångel med konton.
 * Kommer ihåg vem som är inloggad.
 */

// Gemensam kontext för autentisering
const AuthContext = createContext({
  currentUser: null,
  loading: true,
  error: null,
  loginAnonymously: () => Promise.resolve({ success: false }),
  logout: () => Promise.resolve({ success: false }),
  deleteCurrentUser: () => Promise.resolve({ success: false }),
  isAnonymous: false,
});

// Hook för att komma åt autentisering
export const useAuth = () => {
  const context = useContext(AuthContext);

  // Returnerar alltid något användbart
  return context;
};

// Provider-komponent som ger tillgång till autentisering
export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Använder den redan initialiserade Firebase Auth-instansen från config

  /**
   * Öppnar dörren för anonyma gäster 🎭
   *
   * Som att säga "kom in, du behöver inte visa legitimation!"
   * Firebase skapar en tillfällig identitet åt dig automatiskt.
   */
  const loginAnonymously = async () => {
    try {
      setError(null);
      const userCredential = await signInAnonymously(auth);
      return { success: true, user: userCredential.user };
    } catch (error) {
      console.error("Inloggningsfel:", error.message);
      setError("Kunde inte logga in");
      return { success: false, error: "Kunde inte logga in" };
    }
  };

  /**
   * Städar upp efter gäster som går hem 🧹
   *
   * Som att torka av bordet efter att någon ätit - vi vill inte
   * att Firebase fylls med gamla anonyma konton som ingen använder.
   * Bra miljötänk för databasen!
   */
  const deleteCurrentUser = async () => {
    try {
      if (!currentUser) {
        return { success: false, error: "Ingen användare att ta bort" };
      }

      await deleteUser(currentUser);
      console.log("Användare borttagen från Firebase 🧹");
      return { success: true };
    } catch (error) {
      console.error("Kunde inte ta bort användare:", error.message);
      return {
        success: false,
        error: "Borttagning misslyckades: " + error.message,
      };
    }
  };

  /**
   * Säger hej då och stänger dörren 👋
   *
   * Som att lämna en fest - vi säger tack för ikväll och går hem.
   * Firebase glömmer vem vi var och vi börjar om från början nästa gång.
   */
  const logout = async () => {
    try {
      await signOut(auth);
      return { success: true };
    } catch (error) {
      console.error("Utloggningsfel:", error.message);
      setError("Kunde inte logga ut. Försök igen.");
      return { success: false, error: "Kunde inte logga ut. Försök igen." };
    }
  };

  /**
   * Lyssnar på ändringar i inloggningsstatus 👂
   */
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setCurrentUser(user);
      setLoading(false);
    });

    return unsubscribe; // Städar upp när komponenten försvinner
  }, []); // Kör bara en gång

  // Packar ihop allt för andra komponenter
  const value = {
    currentUser,
    loading,
    error,
    loginAnonymously,
    logout,
    deleteCurrentUser,
    isAnonymous: currentUser?.isAnonymous || false,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export default useAuth;
