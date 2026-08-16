package com.example.todo_antsm;

import com.example.todo_antsm.Task.TaskServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Huvudklass som startar JavaFX-applikationen och TaskServer.
 */
public class TaskApp extends Application {
    private static final Logger logger = Logger.getLogger(TaskApp.class.getName());
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void start(Stage primaryStage) {
        // Starta servern i en separat tråd
        startServer();

        try {
            // Laddar FXML och kopplar till TaskController
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/todo_antsm/TaskApp.fxml")));
            Scene scene = new Scene(root, 800, 500); // Justerad storlek för bättre UI

            // Lägg till CSS-stilark
            String cssPath = Objects.requireNonNull(getClass().getResource("/com/example/todo_antsm/styles.css")).toExternalForm();
            scene.getStylesheets().add(cssPath);

            // Inställningar för huvudfönstret
            primaryStage.setTitle("To-Do List Management System");
            primaryStage.setScene(scene);
            primaryStage.show();

            logger.info("TaskApp startad och FXML laddad korrekt.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Kunde inte ladda FXML eller starta applikationen: ", e);
        }
    }

    /**
     * Startar TaskServer i en separat tråd.
     */
    private void startServer() {
        executorService.submit(() -> {
            try {
                TaskServer.main(new String[]{}); // Starta servern
                logger.info("TaskServer startad korrekt på port 8080.");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Kunde inte starta TaskServer: ", e);
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        executorService.shutdownNow();
        logger.info("TaskApp och TaskServer stängdes ned.");
    }

    /**
     * Huvudmetod för att starta JavaFX-applikationen.
     * @param args Argument för körningen.
     */
    public static void main(String[] args) {
        logger.info("Startar TaskApp...");
        launch(args);
    }
}




