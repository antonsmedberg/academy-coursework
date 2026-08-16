package com.example.todo_antsm.Task;

/**
 * Abstrakt klass för grundstruktur i en uppgift.
 */
public abstract class Task {
    protected int id;
    protected String title;
    protected String description;

    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getter för ID
    public int getId() {
        return id;
    }

    // Setter för ID
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    // Abstrakt metod för att visa uppgifter
    public abstract void displayTask();
}
