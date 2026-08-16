package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

/**
 * Singleton-klass för att hantera studenter.
 * Hanterar inläsning, lagring, och sökning av studentuppgifter.
 */
public class StudentManagementSystem {
    private static final Logger logger = Logger.getLogger(StudentManagementSystem.class.getName());
    private static StudentManagementSystem instance;
    private final Map<String, Student> students; // Datastruktur för lagring av studenter
    private static final String FILE_NAME = System.getProperty("user.dir") + File.separator + "students.txt";

    // Privat konstruktor för Singleton-mönstret
    private StudentManagementSystem() {
        students = new HashMap<>();
        loadFromFile(); // Försök att läsa in data från fil vid start
    }

    /**
     * Hämtar den enda instansen av klassen.
     *
     * @return Instansen av StudentManagementSystem
     */
    public static synchronized StudentManagementSystem getInstance() {
        if (instance == null) {
            instance = new StudentManagementSystem();
        }
        return instance;
    }

    /**
     * Lägger till en ny student efter validering av ID.
     *
     * @param id    Studentens ID
     * @param name  Studentens namn
     * @param grade Studentens betyg
     */
    public void addStudent(String id, String name, String grade) {
        if (students.containsKey(id)) {
            logger.warning("Student med ID " + id + " finns redan.");
            System.out.println("Student med ID " + id + " finns redan.");
            return;
        }
        try {
            Student student = new Student(id, name, grade);
            students.put(id, student);
            logger.info("Student tillagd: " + student);
            System.out.println("Studenten har lagts till.");
        } catch (IllegalArgumentException e) {
            logger.warning("Fel vid tillägg av student: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sparar alla studenter till en textfil i CSV-format.
     */
    public void saveToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_NAME))) {
            for (Student student : students.values()) {
                writer.write(student.getId() + ";" + student.getName() + ";" + student.getGrade());
                writer.newLine();
            }
            logger.info("Data sparad till fil: " + FILE_NAME);
            System.out.println("Studentdata har sparats till fil.");
        } catch (IOException e) {
            logger.severe("Kunde inte spara data: " + e.getMessage());
            System.out.println("Ett fel uppstod vid sparandet till fil.");
        }
    }

    /**
     * Läser in studenter från en textfil.
     */
    public void loadFromFile() {
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) {
            logger.info("Ingen fil hittades. Börjar med en tom lista.");
            System.out.println("Ingen fil hittades. Börjar med en tom lista.");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            students.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    try {
                        Student student = new Student(parts[0].trim(), parts[1].trim(), parts[2].trim());
                        students.put(student.getId(), student);
                    } catch (IllegalArgumentException e) {
                        logger.warning("Ogiltig rad i filen: " + line);
                    }
                } else {
                    logger.warning("Ogiltig rad i filen: " + line);
                }
            }
            logger.info("Studentdata inläst från fil.");
            System.out.println("Studentdata har lästs in från fil.");
        } catch (IOException e) {
            logger.severe("Kunde inte läsa in data: " + e.getMessage());
            System.out.println("Ett fel uppstod vid inläsning från fil.");
        }
    }

    /**
     * Söker efter en student baserat på ID.
     *
     * @param id ID för studenten som ska sökas
     * @return Studentobjekt eller null om ingen hittas
     */
    public Student findStudentById(String id) {
        return students.get(id);
    }

    /**
     * Visar alla studenter.
     */
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Det finns inga studenter att visa.");
        } else {
            students.values().forEach(System.out::println);
        }
    }

    /**
     * Avslutar programmet och sparar data.
     */
    public void exit() {
        saveToFile();
        logger.info("Programmet avslutas.");
        System.out.println("Programmet avslutas. Tack för att du använde systemet!");
    }
}

