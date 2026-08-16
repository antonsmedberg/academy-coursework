package org.example.vehicles;

/**
 * Abstrakt basklass som representerar ett fordon.
 * Innehåller gemensamma attribut och metoder för alla typer av fordon.
 */
public abstract class Vehicle {
    private final String model; // Fordonsmodell
    private final String registrationNumber; // Registreringsnummer
    private final double dailyRentalPrice; // Hyrespris per dag

    /**
     * Konstruktor för att initialisera fordon.
     * @param model Modell av fordonet (måste inte vara tom).
     * @param registrationNumber Registreringsnummer (måste inte vara tom).
     * @param dailyRentalPrice Hyrespris per dag (måste vara positivt).
     */
    public Vehicle(String model, String registrationNumber, double dailyRentalPrice) {
        if (model == null || model.isEmpty()) {
            throw new IllegalArgumentException("Modell kan inte vara null eller tom.");
        }
        if (registrationNumber == null || registrationNumber.isEmpty()) {
            throw new IllegalArgumentException("Registreringsnummer kan inte vara null eller tomt.");
        }
        if (dailyRentalPrice <= 0) {
            throw new IllegalArgumentException("Hyrespriset måste vara större än noll.");
        }

        this.model = model;
        this.registrationNumber = registrationNumber;
        this.dailyRentalPrice = dailyRentalPrice;
    }

    /**
     * Hämtar fordonets modell.
     * @return Modell som en sträng.
     */
    public String getModel() {
        return model;
    }

    /**
     * Hämtar fordonets registreringsnummer.
     * @return Registreringsnummer som en sträng.
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Hämtar fordonets hyrespris per dag.
     * @return Hyrespris per dag som en double.
     */
    public double getDailyRentalPrice() {
        return dailyRentalPrice;
    }

    /**
     * Abstrakt metod för att hämta fordonstyp.
     * Måste implementeras av subklasser.
     * @return Typ av fordon som en sträng.
     */
    public abstract String getVehicleType();

    /**
     * Generisk metod för att visa fordonets information.
     * @return En strängrepresentation av fordonet.
     */
    @Override
    public String toString() {
        return String.format("%s [%s] - %.2f SEK/dag", model, registrationNumber, dailyRentalPrice);
    }
}
