package org.example;

import java.io.Serializable;

/**
 * Klass som representerar en student.
 * Implementerar Serializable för att stödja filhantering.
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;   // Unikt student-ID
    private final String name; // Studentens namn
    private final String grade; // Studentens betyg (A-F)

    /**
     * Konstruktor för att skapa en student med validering av indata.
     *
     * @param id    Studentens unika ID
     * @param name  Studentens fullständiga namn
     * @param grade Studentens betyg (A-F)
     * @throws IllegalArgumentException Om någon av parametrarna är ogiltig
     */
    public Student(String id, String name, String grade) {
        if (id == null || !id.matches("\\w+")) {
            throw new IllegalArgumentException("Ogiltigt ID. ID måste vara alfanumeriskt.");
        }
        if (name == null || name.trim().isEmpty() || !name.matches("[a-zA-ZåäöÅÄÖ\\s]+")) {
            throw new IllegalArgumentException("Ogiltigt namn. Namn får endast innehålla bokstäver och mellanslag.");
        }
        if (grade == null || !grade.matches("[A-Fa-f]")) {
            throw new IllegalArgumentException("Ogiltigt betyg. Betyget måste vara en bokstav mellan A och F.");
        }
        this.id = id.trim();
        this.name = name.trim();
        this.grade = grade.trim().toUpperCase();
    }

    // Getter för student-ID
    public String getId() {
        return id;
    }

    // Getter för namn
    public String getName() {
        return name;
    }

    // Getter för betyg
    public String getGrade() {
        return grade;
    }

    /**
     * Formaterad representation av studentens uppgifter.
     *
     * @return String som representerar studenten
     */
    @Override
    public String toString() {
        return "Student ID: " + id + ", Namn: " + name + ", Betyg: " + grade;
    }
}

