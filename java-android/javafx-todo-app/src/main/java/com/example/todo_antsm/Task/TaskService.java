package com.example.todo_antsm.Task;

import java.util.*;

/**
 * Gränssnitt för CRUD-operationer
 */
public interface TaskService {
    void createTask(Task task);
    List<Task> getAllTasks();
    void updateTask(int id, Task updatedTask) throws IllegalArgumentException;
    void deleteTask(int id) throws IllegalArgumentException;
}
