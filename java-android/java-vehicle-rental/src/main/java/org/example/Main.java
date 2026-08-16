package org.example;

import org.example.interfaces.Rentable;
import org.example.vehicles.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Huvudklass som hanterar användarinteraktion och fordonshantering.
 */
public class Main {
    // ANSI färgkoder för konsollen
    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {
        // Lista med tillgängliga fordon
        ArrayList<Rentable> vehicles = new ArrayList<>();
        vehicles.add(new Car("Toyota Corolla", "ABC123", 500, 5));
        vehicles.add(new SUV("Land Rover Defender", "DEF456", 800, true));
        vehicles.add(new Truck("Volvo FH", "GHI789", 1200, 20));
        vehicles.add(new Convertible("Mazda MX-5", "JKL012", 700, true));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                printMenu();
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> showVehicles(vehicles);
                    case 2 -> rentVehicle(scanner, vehicles);
                    case 3 -> returnVehicle(scanner, vehicles);
                    case 4 -> calculateCost(scanner, vehicles);
                    case 5 -> {
                        System.out.println(GREEN + "Programmet avslutas. Tack för att du använde tjänsten!" + RESET);
                        scanner.close();
                        return;
                    }
                    default -> System.out.println(RED + "Ogiltigt val. Ange ett nummer mellan 1-5." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Felaktig inmatning! Ange ett heltal mellan 1-5." + RESET);
                scanner.next(); // Rensa bufferten
            }
        }
    }

    /**
     * Skriver ut huvudmenyn med färger och struktur.
     */
    private static void printMenu() {
        System.out.println(CYAN + "\n--- Virtuell Biluthyrning ---" + RESET);
        System.out.println(BLUE + "1. Visa alla fordon" + RESET);
        System.out.println(BLUE + "2. Hyra ett fordon" + RESET);
        System.out.println(BLUE + "3. Returnera ett fordon" + RESET);
        System.out.println(BLUE + "4. Beräkna hyreskostnad" + RESET);
        System.out.println(BLUE + "5. Avsluta" + RESET);
        System.out.print(YELLOW + "Välj ett alternativ: " + RESET);
    }

    /**
     * Visar alla fordon i listan och deras status med färg för status.
     */
    private static void showVehicles(ArrayList<Rentable> vehicles) {
        System.out.println(CYAN + "\nTillgängliga fordon:" + RESET);
        if (vehicles.isEmpty()) {
            System.out.println(RED + "Inga fordon finns tillgängliga." + RESET);
            return;
        }

        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = (Vehicle) vehicles.get(i);
            String rentalStatus = ((Rentable) v).isRented() ? RED + "Uthyrd" + RESET : GREEN + "Tillgänglig" + RESET;
            System.out.printf("%d. %s - %s (%s) [%s]%n",
                    i + 1, v.getVehicleType(), v.getModel(), v.getRegistrationNumber(), rentalStatus);
        }
    }

    /**
     * Hanterar processen att hyra ett fordon.
     */
    private static void rentVehicle(Scanner scanner, ArrayList<Rentable> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println(RED + "Inga fordon finns tillgängliga för uthyrning." + RESET);
            return;
        }

        System.out.print(YELLOW + "\nAnge numret för fordonet du vill hyra: " + RESET);
        int rentIndex = getValidatedIndex(scanner, vehicles.size());

        if (rentIndex == -1) {
            System.out.println(RED + "Ogiltigt val. Försök igen." + RESET);
            return;
        }

        Rentable vehicle = vehicles.get(rentIndex);
        if (vehicle.isRented()) {
            System.out.println(RED + "Detta fordon är redan uthyrt. Försök med ett annat." + RESET);
        } else {
            vehicle.rentOut();
            System.out.println(GREEN + "Fordonet hyrdes framgångsrikt!" + RESET);
        }
    }

    /**
     * Hanterar processen att returnera ett fordon.
     */
    private static void returnVehicle(Scanner scanner, ArrayList<Rentable> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println(RED + "Inga fordon finns att returnera." + RESET);
            return;
        }

        System.out.print(YELLOW + "\nAnge numret för fordonet du vill returnera: " + RESET);
        int returnIndex = getValidatedIndex(scanner, vehicles.size());

        if (returnIndex == -1) {
            System.out.println(RED + "Ogiltigt val. Försök igen." + RESET);
            return;
        }

        Rentable vehicle = vehicles.get(returnIndex);
        if (vehicle.isRented()) {
            vehicle.returnVehicle();
            System.out.println(GREEN + "Fordonet har återlämnats!" + RESET);
        } else {
            System.out.println(RED + "Detta fordon är inte uthyrt." + RESET);
        }
    }

    /**
     * Beräknar hyreskostnaden för ett fordon.
     */
    private static void calculateCost(Scanner scanner, ArrayList<Rentable> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println(RED + "Inga fordon finns tillgängliga." + RESET);
            return;
        }

        System.out.print(YELLOW + "\nAnge numret för fordonet du vill beräkna kostnaden för: " + RESET);
        int index = getValidatedIndex(scanner, vehicles.size());

        if (index == -1) {
            System.out.println(RED + "Ogiltigt val. Försök igen." + RESET);
            return;
        }

        Rentable vehicle = vehicles.get(index);
        System.out.print(YELLOW + "Ange antal dagar du vill hyra fordonet: " + RESET);
        try {
            int days = scanner.nextInt();
            if (days <= 0) {
                System.out.println(RED + "Antalet dagar måste vara större än 0." + RESET);
                return;
            }
            double cost = vehicle.calculateRentalCost(days);
            System.out.printf(GREEN + "Hyreskostnaden för %s i %d dagar är: %.2f SEK.%n" + RESET,
                    ((Vehicle) vehicle).getModel(), days, cost);
        } catch (InputMismatchException e) {
            System.out.println(RED + "Ogiltig inmatning! Ange ett giltigt heltal." + RESET);
            scanner.next(); // Rensa bufferten
        }
    }

    /**
     * Validerar användarinmatning för index.
     * @param scanner Scanner-objektet för att läsa in data.
     * @param size Antal element i listan.
     * @return Ett giltigt index eller -1 om ogiltigt.
     */
    private static int getValidatedIndex(Scanner scanner, int size) {
        try {
            int index = scanner.nextInt() - 1;
            if (index >= 0 && index < size) {
                return index;
            }
        } catch (InputMismatchException e) {
            scanner.next(); // Rensa bufferten
        }
        return -1; // Ogiltigt index
    }
}
