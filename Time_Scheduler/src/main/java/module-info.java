module com.javaproject.time_scheduler {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    requires com.calendarfx.view;

    opens com.javaproject.time_scheduler to javafx.fxml;
    exports com.javaproject.time_scheduler;
}