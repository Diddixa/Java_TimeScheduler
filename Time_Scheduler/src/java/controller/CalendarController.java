package controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.page.MonthPage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Event;
import models.Priority;
import models.User;

import java.net.URL;
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class CalendarController {

    @FXML
    private CalendarView calendarView;
    private Thread updateTimeThread;
    /** currently logged in user */
    private User user;

   /* public static final CountDownLatch latch = new CountDownLatch(1);
    public static CalendarController calendarController = null;

    public static CalendarController waitForStartUpTest() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return calendarController;
    }

    public static void setStartUpTest(CalendarController startUpTest0) {
        calendarController = startUpTest0;
        latch.countDown();
    }

    public CalendarController() {
        setStartUpTest(this);
    }

    public void printSomething() {
        System.out.println("You called a method on the application");
    }


    /**
     * function to retrieve the logged in user from LoginController
     * @param user
     */
    public void retrieveUser(User user) {
        this.user = user;

    }



  /*  @Override
    public void start(Stage primaryStage) throws Exception {
    CalendarView calendarView = new CalendarView();
    Calendar events = new Calendar("ShiftRoster");
        events.setStyle(Calendar.Style.STYLE1);
    ZoneId id = ZoneId.of("Germany/Berlin");

    ArrayList<models.Event> event = Database.getEventsFromUser(57);

        assert event != null;
        for (Event thisEvent : event) {

            Entry<String> showEvent = new Entry<>(thisEvent.getName());

            LocalTime startTime = thisEvent.getStartTime();
            LocalTime endTime = thisEvent.getEndTime();
            LocalDate date = thisEvent.getDate();
            showEvent.setInterval(date);
            showEvent.setInterval(startTime, endTime);
            events.addEntry(showEvent);
            System.out.println(thisEvent.getName());
        }

    CalendarSource calendarSourceTasks = new CalendarSource("Events");
        calendarSourceTasks.getCalendars().addAll(events);
        calendarView.getCalendarSources().setAll(calendarSourceTasks);} */

}




