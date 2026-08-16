package org.example.interfaces;

/**
 * Interface som definierar uthyrningsfunktioner för fordon.
 */
public interface Rentable {

    /**
     * Hyra ut ett fordon.
     */
    void rentOut();

    /**
     * Returnera ett fordon.
     */
    void returnVehicle();

    /**
     * Beräkna hyreskostnad baserat på antal dagar.
     * @param days Antal dagar som fordonet ska hyras.
     * @return Totala kostnaden för uthyrningen.
     * @throws IllegalArgumentException Om antalet dagar är mindre än eller lika med noll.
     */
    double calculateRentalCost(int days);

    /**
     * Kontrollera om fordonet är uthyrt.
     * @return True om fordonet är uthyrt, annars false.
     */
    boolean isRented();
}


