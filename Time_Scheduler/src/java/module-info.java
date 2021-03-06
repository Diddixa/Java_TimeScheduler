module com.javaproject.time_scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires com.calendarfx.view;
    requires activation;
    requires javax.mail.api;
    requires itextpdf;

    opens com.javaproject.time_scheduler to javafx.fxml;
    exports com.javaproject.time_scheduler;
    exports controller;
    opens controller to javafx.fxml;
    exports models;
    opens models to javafx.fxml;
}