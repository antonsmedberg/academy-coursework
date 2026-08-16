package org.example.vehicles;

import org.example.interfaces.Rentable;

/**
 * Representerar en cabriolet (Convertible).
 * Ärver från Vehicle och implementerar Rentable.
 */
public class Convertible extends Vehicle implements Rentable {
    private boolean isRented; // Anger om fordonet är uthyrt
    private boolean hasRetractableRoof; // Anger om taket är fällbart

    public Convertible(String model, String registrationNumber, double dailyRentalPrice, boolean hasRetractableRoof) {
        super(model, registrationNumber, dailyRentalPrice);
        this.hasRetractableRoof = hasRetractableRoof;
        this.isRented = false; // Initial status är ej uthyrd
    }

    // Getter för om fordonet är uthyrt
    public boolean isRented() {
        return isRented;
    }

    // Getter för om taket är fällbart
    public boolean hasRetractableRoof() {
        return hasRetractableRoof;
    }

    @Override
    public void rentOut() {
        if (!isRented) {
            isRented = true;
            System.out.println(getModel() + " har hyrts ut. Njut av att köra med taket nedfällt!");
        } else {
            System.out.println(getModel() + " är redan uthyrd. Försök med ett annat fordon.");
        }
    }

    @Override
    public void returnVehicle() {
        if (isRented) {
            isRented = false;
            System.out.println(getModel() + " har återlämnats. Tack för att du hyrde cabrioleten!");
        } else {
            System.out.println(getModel() + " är inte uthyrd. Kontrollera igen.");
        }
    }

    @Override
    public double calculateRentalCost(int days) {
        return days * getDailyRentalPrice();
    }

    @Override
    public String getVehicleType() {
        return "Cabriolet";
    }
}

