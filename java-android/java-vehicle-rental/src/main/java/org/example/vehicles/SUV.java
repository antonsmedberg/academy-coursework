package org.example.vehicles;

import org.example.interfaces.Rentable;

/**
 * Representerar en SUV.
 * Ärver från Vehicle och implementerar Rentable.
 */
public class SUV extends Vehicle implements Rentable {
    private boolean isRented; // Anger om SUV är uthyrd
    private boolean hasFourWheelDrive; // Anger om SUV har fyrhjulsdrift

    public SUV(String model, String registrationNumber, double dailyRentalPrice, boolean hasFourWheelDrive) {
        super(model, registrationNumber, dailyRentalPrice);
        this.hasFourWheelDrive = hasFourWheelDrive;
        this.isRented = false; // Initial status är ej uthyrd
    }

    // Getter för om fordonet är uthyrt
    public boolean isRented() {
        return isRented;
    }

    // Getter för fyrhjulsdrift
    public boolean hasFourWheelDrive() {
        return hasFourWheelDrive;
    }

    @Override
    public void rentOut() {
        if (!isRented) {
            isRented = true;
            System.out.println(getModel() + " har hyrts ut. Perfekt för terrängkörning!");
        } else {
            System.out.println(getModel() + " är redan uthyrd. Försök med ett annat fordon.");
        }
    }

    @Override
    public void returnVehicle() {
        if (isRented) {
            isRented = false;
            System.out.println(getModel() + " har återlämnats. Tack för att du hyrde SUV:en!");
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
        return "SUV";
    }
}

