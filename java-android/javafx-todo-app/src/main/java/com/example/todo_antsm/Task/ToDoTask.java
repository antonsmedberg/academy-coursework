package com.example.todo_antsm.Task;

/**
 * Implementering av Task.
 */
public class ToDoTask extends Task {
    public ToDoTask(int id, String title, String description) {
        super(id, title, description);
    }

    @Override
    public void displayTask() {
        System.out.println("Uppgift ID: " + id);
        System.out.println("Titel: " + title);
        System.out.println("Beskrivning: " + description);
    }
}


