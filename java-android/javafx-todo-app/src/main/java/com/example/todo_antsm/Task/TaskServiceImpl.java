package com.example.todo_antsm.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementering av CRUD-funktionalitet med felhantering.
 */
public class TaskServiceImpl implements TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private int currentId = 1; // Automatiskt genererade ID:n
    private static final Logger logger = Logger.getLogger(TaskServiceImpl.class.getName());

    @Override
    public void createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Uppgiften får inte vara null.");
        }
        task.setId(currentId++); // Tilldelar ett unikt ID
        tasks.add(task);
        logger.info("Uppgift skapad och tillagd i listan: " + task.getTitle() + " | Nuvarande antal uppgifter: " + tasks.size());
    }

    @Override
    public List<Task> getAllTasks() {
        logger.info("Hämtar alla uppgifter. Antal: " + tasks.size());
        for (Task task : tasks) {
            logger.info("Uppgift i listan: ID " + task.getId() + ", Titel: " + task.getTitle());
        }
        return new ArrayList<>(tasks); // Returnera en kopia av listan
    }


    @Override
    public void updateTask(int id, Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                updatedTask.setId(id); // Behåll samma ID
                tasks.set(i, updatedTask);
                logger.info("Uppgift uppdaterad: ID " + id);
                return;
            }
        }
        throw new IllegalArgumentException("Uppgift med ID " + id + " hittades inte.");
    }

    @Override
    public void deleteTask(int id) {
        boolean removed = tasks.removeIf(task -> task.getId() == id);
        if (removed) {
            logger.info("Uppgift borttagen: ID " + id);
        } else {
            throw new IllegalArgumentException("Uppgift med ID " + id + " hittades inte.");
        }
    }
}

