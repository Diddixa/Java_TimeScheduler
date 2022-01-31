module com.javaproject.time_scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
<<<<<<< Updated upstream
=======
    requires com.calendarfx.view;
    requires java.logging;
    requires activation;
    requires javax.mail.api;
>>>>>>> Stashed changes

    opens com.javaproject.time_scheduler to javafx.fxml;
    exports com.javaproject.time_scheduler;
    exports controller;
    opens controller to javafx.fxml;
    exports models;
    opens models to javafx.fxml;
}