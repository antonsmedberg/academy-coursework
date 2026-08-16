package org.example.vehicles;

import org.example.interfaces.Rentable;

/**
 * Representerar en lastbil (Truck).
 * Ärver från Vehicle och implementerar Rentable.
 */
public class Truck extends Vehicle implements Rentable {
    private boolean isRented; // Anger om fordonet är uthyrt
    private double cargoCapacity; // Lastkapacitet i ton

    public Truck(String model, String registrationNumber, double dailyRentalPrice, double cargoCapacity) {
        super(model, registrationNumber, dailyRentalPrice);
        this.cargoCapacity = cargoCapacity;
        this.isRented = false; // Initial status är ej uthyrd
    }

    // Getter för om fordonet är uthyrt
    public boolean isRented() {
        return isRented;
    }

    // Getter för lastkapacitet
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public void rentOut() {
        if (!isRented) {
            isRented = true;
            System.out.println(getModel() + " har hyrts ut. Perfekt för tunga transporter!");
        } else {
            System.out.println(getModel() + " är redan uthyrd. Försök med ett annat fordon.");
        }
    }

    @Override
    public void returnVehicle() {
        if (isRented) {
            isRented = false;
            System.out.println(getModel() + " har återlämnats. Tack för att du hyrde lastbilen!");
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
        return "Lastbil";
    }
}
