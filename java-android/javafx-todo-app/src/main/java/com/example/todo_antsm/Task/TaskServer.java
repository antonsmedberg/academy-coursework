package com.example.todo_antsm.Task;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serverklass som hanterar API-endpoints och CRUD-operationer.
 */
public class TaskServer {
    private static final TaskService taskService = new TaskServiceImpl();
    private static final Logger logger = Logger.getLogger(TaskServer.class.getName());

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/tasks", new TaskHandler());
            server.setExecutor(null); // Använder standardtrådhantering
            server.start();
            logger.info("TaskServer startad och lyssnar på port 8080...");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Kunde inte starta TaskServer: ", e);
        }
    }

    /**
     * Hanterar API-anrop för CRUD-operationer.
     */
    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";
            int statusCode = 200;

            try {
                switch (method) {
                    case "GET":
                        response = getAllTasks();
                        logger.info("GET /tasks - Hämtar alla uppgifter. Antal: " + taskService.getAllTasks().size());
                        break;
                    case "POST":
                        createTask(exchange);
                        response = "Ny uppgift skapad!";
                        break;
                    case "PUT":
                        updateTask(exchange);
                        response = "Uppgift uppdaterad!";
                        break;
                    case "DELETE":
                        deleteTask(exchange);
                        response = "Uppgift borttagen!";
                        break;
                    default:
                        statusCode = 405;
                        response = "Metoden " + method + " stöds inte.";
                        logger.warning("Ogiltig metod: " + method);
                }
            } catch (Exception e) {
                statusCode = 500;
                response = "Fel: " + e.getMessage();
                logger.log(Level.SEVERE, "Fel vid hantering av API-anrop", e);
            }

            sendResponse(exchange, statusCode, response);
        }

        /**
         * Hämtar alla uppgifter och returnerar dem som en sträng.
         */
        private String getAllTasks() {
            List<Task> allTasks = taskService.getAllTasks();
            if (allTasks.isEmpty()) {
                logger.info("Inga uppgifter hittades.");
                return "[]"; // Returnera en tom JSON-array
            }

            StringBuilder sb = new StringBuilder("[");
            for (Task task : allTasks) {
                if (task instanceof DeadlineTask deadlineTask) {
                    sb.append("{\"id\":").append(task.getId())
                            .append(",\"title\":\"").append(task.getTitle())
                            .append("\",\"description\":\"").append(task.getDescription())
                            .append("\",\"deadline\":\"").append(deadlineTask.getDeadline())
                            .append("\"},");
                } else {
                    sb.append("{\"id\":").append(task.getId())
                            .append(",\"title\":\"").append(task.getTitle())
                            .append("\",\"description\":\"").append(task.getDescription())
                            .append("\"},");
                }
            }
            if (sb.length() > 1) sb.setLength(sb.length() - 1); // Ta bort sista kommatecknet
            sb.append("]");
            String result = sb.toString();
            logger.info("Uppgifter returnerade från servern: " + result);
            return result;
        }



        /**
         * Skapar en ny uppgift baserat på inkommande data.
         */
        private void createTask(HttpExchange exchange) throws IOException {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                String[] taskDetails = br.readLine().split(",");
                Task task;
                if (taskDetails.length == 3) { // DeadlineTask
                    task = new DeadlineTask(0, taskDetails[0], taskDetails[1], taskDetails[2]);
                } else if (taskDetails.length == 2) { // ToDoTask
                    task = new ToDoTask(0, taskDetails[0], taskDetails[1]);
                } else {
                    throw new IllegalArgumentException("Ogiltig data för uppgift.");
                }
                taskService.createTask(task); // Lägg till uppgift i TaskService
                logger.info("Uppgift skapad via API: " + task.getTitle() + " | Totalt antal uppgifter: " + taskService.getAllTasks().size());
            } catch (Exception e) {
                logger.warning("Fel vid skapande av uppgift: " + e.getMessage());
                sendErrorResponse(exchange, "Fel vid skapande: " + e.getMessage());
            }
        }


        /**
         * Uppdaterar en befintlig uppgift baserat på inkommande data.
         */
        private void updateTask(HttpExchange exchange) throws IOException {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                String[] taskDetails = br.readLine().split(",");
                int id = Integer.parseInt(taskDetails[0]);
                if (taskDetails.length == 4) { // DeadlineTask
                    taskService.updateTask(id, new DeadlineTask(id, taskDetails[1], taskDetails[2], taskDetails[3]));
                    logger.info("DeadlineTask uppdaterad: ID " + id);
                } else if (taskDetails.length == 3) { // ToDoTask
                    taskService.updateTask(id, new ToDoTask(id, taskDetails[1], taskDetails[2]));
                    logger.info("ToDoTask uppdaterad: ID " + id);
                } else {
                    throw new IllegalArgumentException("Ogiltig data för uppdatering.");
                }
            } catch (Exception e) {
                logger.warning("Fel vid uppdatering av uppgift: " + e.getMessage());
                sendErrorResponse(exchange, "Fel vid uppdatering: " + e.getMessage());
            }
        }

        /**
         * Tar bort en uppgift baserat på ID.
         */
        private void deleteTask(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            if (query == null || !query.startsWith("id=")) {
                throw new IllegalArgumentException("ID för uppgift saknas.");
            }
            int id = Integer.parseInt(query.split("=")[1]);
            taskService.deleteTask(id);
            logger.info("Uppgift borttagen: ID " + id);
        }

        /**
         * Skickar ett felmeddelande till klienten.
         */
        private void sendErrorResponse(HttpExchange exchange, String message) throws IOException {
            exchange.sendResponseHeaders(400, message.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(message.getBytes());
            }
        }

        /**
         * Skickar ett svar till klienten.
         */
        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.sendResponseHeaders(statusCode, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}

