package com.example.todo_antsm;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller för att hantera UI och API-anrop.
 */
public class TaskController {

    @FXML
    private ListView<String> taskListView;

    @FXML
    private TextField taskTitleField;

    @FXML
    private TextField taskDescriptionField;

    @FXML
    private DatePicker taskDeadlinePicker;

    private static final Logger logger = Logger.getLogger(TaskController.class.getName());
    private static final String API_URL = "http://localhost:8080/tasks";

    @FXML
    private void initialize() {
        loadTasks();
    }

    @FXML
    private void handleAddTask() {
        String title = taskTitleField.getText().trim();
        String description = taskDescriptionField.getText().trim();
        String deadline = taskDeadlinePicker.getValue() != null ? taskDeadlinePicker.getValue().toString() : null;

        if (title.isEmpty() || description.isEmpty()) {
            showError("Titel och beskrivning får inte vara tomma.");
            return;
        }

        boolean success = sendPostRequest(title, description, deadline);
        if (success) {
            taskTitleField.clear();
            taskDescriptionField.clear();
            taskDeadlinePicker.setValue(null);
            showConfirmation("Uppgiften har lagts till.");
            loadTasks();
        }
    }

    @FXML
    private void handleEditTask() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showError("Välj en uppgift att redigera.");
            return;
        }

        int id = extractIdFromTask(selectedTask);
        String title = taskTitleField.getText().trim();
        String description = taskDescriptionField.getText().trim();
        String deadline = taskDeadlinePicker.getValue() != null ? taskDeadlinePicker.getValue().toString() : null;

        if (title.isEmpty() || description.isEmpty()) {
            showError("Titel och beskrivning får inte vara tomma.");
            return;
        }

        boolean success = sendPutRequest(id, title, description, deadline);
        if (success) {
            taskTitleField.clear();
            taskDescriptionField.clear();
            taskDeadlinePicker.setValue(null);
            showConfirmation("Uppgiften har uppdaterats.");
            loadTasks();
        }
    }

    @FXML
    private void handleDeleteTask() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showError("Välj en uppgift att ta bort.");
            return;
        }

        int id = extractIdFromTask(selectedTask);
        boolean success = sendDeleteRequest(id);
        if (success) {
            showConfirmation("Uppgiften har tagits bort.");
            loadTasks();
        }
    }

    private void loadTasks() {
        taskListView.getItems().clear();
        try {
            JSONArray tasks = getTasksFromServer();
            logger.info("Uppgifter hämtade: " + tasks.toString());

            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.getJSONObject(i);
                String taskText = formatTaskForDisplay(task);
                taskListView.getItems().add(taskText);
            }

            logger.info("Totalt antal uppgifter i listan: " + taskListView.getItems().size());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fel vid hämtning av uppgifter.", e);
            showError("Kunde inte ladda uppgifter.");
        }
    }

    private JSONArray getTasksFromServer() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return new JSONArray(response.toString());
        }
    }

    private boolean sendPostRequest(String title, String description, String deadline) {
        return sendRequest("POST", title, description, deadline, 0);
    }

    private boolean sendPutRequest(int id, String title, String description, String deadline) {
        return sendRequest("PUT", title, description, deadline, id);
    }

    private boolean sendDeleteRequest(int id) {
        try {
            URL url = new URL(API_URL + "?id=" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            if (conn.getResponseCode() == 200) {
                logger.info("Uppgift med ID " + id + " togs bort.");
                return true;
            } else {
                logger.warning("Kunde inte ta bort uppgift: " + conn.getResponseMessage());
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fel vid borttagning av uppgift.", e);
            return false;
        }
    }

    private boolean sendRequest(String method, String title, String description, String deadline, int id) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            String body = id > 0
                    ? id + "," + title + "," + description + (deadline != null ? "," + deadline : "")
                    : title + "," + description + (deadline != null ? "," + deadline : "");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes());
            }

            if (conn.getResponseCode() == 200) {
                logger.info(method + " /tasks lyckades: " + title);
                return true;
            } else {
                logger.warning(method + " /tasks misslyckades: " + conn.getResponseMessage());
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fel vid " + method + " av uppgift.", e);
            return false;
        }
    }

    private int extractIdFromTask(String task) {
        return Integer.parseInt(task.split("\\|")[0].split(":")[1].trim());
    }

    private String formatTaskForDisplay(JSONObject task) {
        StringBuilder taskText = new StringBuilder("ID: " + task.getInt("id"));
        taskText.append(" | Titel: ").append(task.getString("title"));
        taskText.append(" | Beskrivning: ").append(task.getString("description"));

        if (task.has("deadline")) {
            taskText.append(" | Deadline: ").append(task.getString("deadline"));
        }

        return taskText.toString();
    }

    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bekräftelse");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fel");
        alert.setHeaderText("Ett fel uppstod");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

