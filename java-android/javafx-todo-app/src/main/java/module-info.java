module com.example.todo_antsm {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.httpserver;
    requires java.desktop;
    requires java.logging;
    requires org.json;

    opens com.example.todo_antsm to javafx.fxml;
    exports com.example.todo_antsm;
}