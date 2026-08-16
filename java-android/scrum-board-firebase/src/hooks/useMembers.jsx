// src/hooks/useMembers.jsx
import { useState, useEffect } from "react";
import { db } from "../firebase/firebaseConfig.jsx";

// SMART IMPORT: Bara det vi behöver från Firebase (mindre paket = snabbare app)
import { ref, onValue, push, remove } from "firebase/database";

/**
 * Min teamchef-assistent! 👥
 *
 * Som en HR-avdelning för min app - håller koll på alla i teamet.
 * Lägger till nya kollegor, säger hej då till de som slutar,
 * och håller listan uppdaterad i realtid så alla ser samma sak.
 *
 * @returns {Object} Allt du behöver för att hantera teamet
 */
const useMembers = () => {
  // Mina tre viktiga tillstånd för teamhantering
  const [members, setMembers] = useState([]); // Alla hjältar i teamet
  const [loading, setLoading] = useState(true); // Hämtar vi data just nu?
  const [error, setError] = useState(null); // Har något gått snett?

  // Adressen i Firebase där alla teammedlemmar bor
  const MEMBERS_REF_PATH = "members";

  /**
   * Min realtidslyssnare - som att ha örat mot väggen! 👂
   *
   * Sätter upp en direktlinje till Firebase som berättar direkt
   * när någon ny kommer till teamet eller när någon lämnar.
   * Ingen behöver uppdatera sidan - allt händer automatiskt!
   */
  useEffect(() => {
    const membersRef = ref(db, MEMBERS_REF_PATH);

    try {
      // Startar min "teamspaning" - lyssnar på alla förändringar
      const unsubscribe = onValue(
        membersRef,
        (snapshot) => {
          const data = snapshot.val();
          if (data) {
            // Förvandlar Firebase-data till en snygg array med alla teammedlemmar
            const membersArray = Object.entries(data).map(([id, member]) => ({
              id,
              ...member,
            }));
            setMembers(membersArray);
          } else {
            // Tomt team? Inga problem, vi börjar med en tom lista
            setMembers([]);
          }
          setLoading(false);
        },
        (error) => {
          console.error("useMembers: Error fetching members:", error);
          setError(error.message);
          setLoading(false);
        }
      );

      // Städar upp när komponenten försvinner (som att stänga av radion)
      return () => {
        unsubscribe();
      };
    } catch (error) {
      console.error("useMembers: Exception in effect:", error);
      setError(error.message);
      setLoading(false);
    }
  }, []);

  /**
   * Välkomnar en ny teammedlem! 🎉
   *
   * Som att skriva in någon i gästboken - sparar all info
   * om den nya personen så alla kan se vem som är med i teamet.
   *
   * @param {Object} memberData - Allt om den nya personen (namn, roll, avatar)
   * @returns {Object} Berättar om det gick bra eller inte
   */
  const addMember = async (memberData) => {
    try {
      const membersRef = ref(db, MEMBERS_REF_PATH);
      await push(membersRef, memberData);
      return { success: true };
    } catch (error) {
      setError(error.message);
      return { success: false, error: error.message };
    }
  };

  /**
   * Säger hej då till en teammedlem 👋
   *
   * Som att sudda ut någon från gästboken - tar bort personen
   * helt från teamet. Inga spår kvar!
   *
   * @param {string} memberId - Vem som ska lämna teamet
   * @returns {Object} Berättar om det gick bra eller inte
   */
  const deleteMember = async (memberId) => {
    try {
      const memberRef = ref(db, `${MEMBERS_REF_PATH}/${memberId}`);
      await remove(memberRef);
      return { success: true };
    } catch (error) {
      setError(error.message);
      return { success: false, error: error.message };
    }
  };

  // Packar ihop allt i en snygg låda för andra komponenter att använda
  return {
    members, // Alla fantastiska människor i teamet
    loading, // Håller vi på att hämta data?
    error, // Har något gått fel?
    addMember, // Välkomna nya medlemmar
    deleteMember, // Säga hej då till medlemmar
  };
};

export default useMembers;
