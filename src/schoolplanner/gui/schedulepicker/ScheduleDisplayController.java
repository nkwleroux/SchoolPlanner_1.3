package schoolplanner.gui.schedulepicker;

import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import schoolplanner.calendar.lessons.Lesson;
import schoolplanner.calendar.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stijn Lemm
 */

public class ScheduleDisplayController extends ScheduleDisplay {

    // This is the list that determines which lessons's to display.
    private List<Schedule> displayedSchedules;

    public ScheduleDisplayController(Stage window,int WIDTH, int HEIGHT) {

        super(window,WIDTH,HEIGHT);
        this.displayedSchedules = new ArrayList<>();

    }

    /**
     * This method will add schedules to the displayedSchedules list to let the viewer know which schedules to display.
     * @param schedule The parameter lessons is the lessons that is going to be printed with the rest.
     */
    public void addSchedule(Schedule schedule) {

        // Here the new lessons will be added to the list of displaying schedules.
        this.displayedSchedules.add(schedule);

        // To ensure an stable process, we again render the new lessons's with the added ones.
        this.update();

    }

    /**
     * This method will add schedules to the displayedSchedules list to let the viewer know which schedules to display.
     *
     * @param schedule     The parameter lessons is the lessons that is going to be printed with the rest.
     * @param columnNumber This is an optional number to add an lessons with an column number for preferences.
     */
    public void addSchedule(Schedule schedule, int columnNumber) {

        // Here the new lessons will be added to the list of displaying schedules.
        this.displayedSchedules.add(columnNumber, schedule);

        // To ensure an stable process, we again render the new lessons's with the added ones.
        this.update();

    }

    /**
     * This method will delete the specific lessons sent in the parameter. After that the ScheduleViewer will
     * re-render the whole screen without the specified lessons.
     * @param schedule the specific lessons object that will be deleted.
     */
    public void removeSchedule(Schedule schedule) {

        // This will remove the parameter lessons from the list of the displayed schedules.
        this.displayedSchedules.remove(schedule);

        // After removing an lessons its important to render the new list to the view.
        this.update();

    }

    /**
     * This method will delete the specific index of lessons in the list sent in the parameter.
     * After that the ScheduleViewer will re-render the whole screen without the specified lessons.
     * @param columnNumber the specific index of an object that will be deleted.
     */
    public void removeSchedule(int columnNumber) {

        // This will remove the parameter column number from the list of the displayed schedules.
        this.displayedSchedules.remove(columnNumber);

        // After removing an lessons its important to render the new list to the view.
        this.update();

    }

    /**
     * This method is the method to change the column width, this method is here and not in the ScheduleViewer class
     * because of the refreshing needed to do this task.
     * @param amountOfPixels the amount of pixels to change to.
     */
    public void setColumnWidth(int amountOfPixels) {

        // First of all set the amount of pixels in the ScheduleViewer class.
        super.setColumnSize(amountOfPixels);

        // After that re-update the whole view to change the width.
        this.update();
    }

    // The final sensitivity for the slider to change the column width.
    private final int sensitivity = 400;

    /**
     * This method is the method to change the column width, this method is here and not in the ScheduleViewer class
     * because of the refreshing needed to do this task.
     * @param percentage the amount of pixels to change to.
     */
    public void setColumnWidth(float percentage) {

        // Change float into int with a specific sensitivity.
        int tempAmountOfPixels = (int) (percentage * this.sensitivity);

        // First of all set the amount of pixels in the ScheduleViewer class.
        super.setColumnSize(tempAmountOfPixels);

        // After that re-update the whole view to change the width.
        this.update();
    }

    /**
     * This method will if called reset the whole group of lessons displayed, after that it will add all of the lessons
     * of all the schedules again. This method is made to ensure that there is never an empty column.
     */
    private void update() {

        // First the pane is cleared.
        super.clear();

        // Second the foreach loop will loop through every lessons asked to display.
        for (Schedule schedule : this.displayedSchedules) {

            // Every lessons has its own set of lessons, these are each added by looping through this foreach loop.
            for (Lesson lesson : schedule.getLessons()) {
                super.addLesson(lesson, this.displayedSchedules.indexOf(schedule));
            }
        }
    }

    /**
     * Getter to get the ScrollPane.
     * @return ScrollPane that is displayed.
     */
    @Override
    public ScrollPane getScrollPane() {
        return super.getScrollPane();
    }

    /**
     * Method used to startAnimationTimer the scene/application.
     */
    public void start(){
        super.start();
    }

    /**
     * Clears the ScrollPane.
     */
    public void clear(){super.clear();}

    /**
     * Getter to get the displayed schedules.
     * @return The displayed schedules on the ScrollPane.
     */
    public List<Schedule> getDisplayedSchedules() {
        return displayedSchedules;
    }

}
