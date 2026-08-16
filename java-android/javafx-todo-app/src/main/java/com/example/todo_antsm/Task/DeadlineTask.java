package com.example.todo_antsm.Task;

public class DeadlineTask extends Task {
    private String deadline;

    public DeadlineTask(int id, String title, String description, String deadline) {
        super(id, title, description);
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }

    @Override
    public void displayTask() {
        System.out.println("Uppgift ID: " + id);
        System.out.println("Titel: " + title);
        System.out.println("Beskrivning: " + description);
        System.out.println("Deadline: " + deadline);
    }
}
