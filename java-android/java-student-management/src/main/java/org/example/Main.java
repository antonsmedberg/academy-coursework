package org.example;

import java.util.Scanner;

/**
 * Huvudklass för att köra Studenthanteringssystemet.
 */
public class Main {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {
        StudentManagementSystem sms = StudentManagementSystem.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addStudent(sms, scanner);
                    break;
                case "2":
                    sms.saveToFile();
                    break;
                case "3":
                    sms.loadFromFile();
                    break;
                case "4":
                    searchStudent(sms, scanner);
                    break;
                case "5":
                    sms.displayAllStudents();
                    break;
                case "6":
                    sms.exit();
                    scanner.close();
                    return;

                default:
                    System.out.println(RED + "Ogiltigt alternativ. Försök igen." + RESET);
            }
        }
    }

    private static void printMenu() {
        System.out.println(CYAN + "\n==== Studenthanteringssystem ====" + RESET);
        System.out.println(YELLOW + "1. Lägg till student" + RESET);
        System.out.println(YELLOW + "2. Spara studenter till fil" + RESET);
        System.out.println(YELLOW + "3. Läs in studenter från fil" + RESET);
        System.out.println(YELLOW + "4. Sök student via ID" + RESET);
        System.out.println(YELLOW + "5. Visa alla studenter" + RESET);
        System.out.println(YELLOW + "6. Avsluta" + RESET);
        System.out.print(GREEN + "Välj ett alternativ: " + RESET);
    }

    private static void addStudent(StudentManagementSystem sms, Scanner scanner) {
        System.out.print(CYAN + "Ange student-ID: " + RESET);
        String id = scanner.nextLine().trim();
        System.out.print(CYAN + "Ange studentens namn: " + RESET);
        String name = scanner.nextLine().trim();
        System.out.print(CYAN + "Ange studentens betyg: " + RESET);
        String grade = scanner.nextLine().trim();
        sms.addStudent(id, name, grade);
    }

    private static void searchStudent(StudentManagementSystem sms, Scanner scanner) {
        System.out.print(CYAN + "Ange student-ID att söka efter: " + RESET);
        String id = scanner.nextLine().trim();
        Student student = sms.findStudentById(id);
        if (student != null) {
            System.out.println(GREEN + student + RESET);
        } else {
            System.out.println(RED + "Ingen student hittades med det ID." + RESET);
        }
    }
}
