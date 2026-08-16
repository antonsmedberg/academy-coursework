package org.example.vehicles;

import org.example.interfaces.Rentable;

/**
 * Representerar en bil (Car).
 * Ärver från Vehicle och implementerar Rentable.
 */
public class Car extends Vehicle implements Rentable {
    private boolean isRented; // Anger om bilen är uthyrd
    private int numberOfSeats; // Antal säten i bilen

    // Konstruktor
    public Car(String model, String registrationNumber, double dailyRentalPrice, int numberOfSeats) {
        super(model, registrationNumber, dailyRentalPrice);
        this.numberOfSeats = numberOfSeats;
        this.isRented = false; // Bilen är inte uthyrd initialt
    }

    // Getter för antal säten
    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    // Getter för isRented
    public boolean isRented() {
        return isRented;
    }

    @Override
    public void rentOut() {
        if (!isRented) {
            isRented = true;
            System.out.println(getModel() + " har hyrts ut.");
        } else {
            System.out.println(getModel() + " är redan uthyrd. Försök med ett annat fordon.");
        }
    }

    @Override
    public void returnVehicle() {
        if (isRented) {
            isRented = false;
            System.out.println(getModel() + " har återlämnats. Tack!");
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
        return "Bil";
    }
}
