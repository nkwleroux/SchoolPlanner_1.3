package schoolplanner.gui.schedulepicker;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import schoolplanner.calendar.lessons.Lesson;
import schoolplanner.util.AlertHandler;
import schoolplanner.util.TimeStamp;

/**
 * @author Stijn Lemm
 */

public class ScheduleDisplay {

    //All of the elements to make an visible lessons
    private Stage window;

    //The scroll pane will ensure that the content is easily visible,
    //even when the content is bigger than the screen.
    private ScrollPane scrollPane;
    private Group drawField;

    // The pref width for the window at startup.
    private final int WIDTH = 800;

    // The scale variable will determine the size of all elements in the lessons view.
    private final double SCALE = 1;

    ScheduleDisplay(Stage window,int WIDTH, int HEIGHT) {

        this.window = window;

        this.drawField = new Group();

        this.scrollPane = new ScrollPane();

        // This will make the scroll pane scrollable with mouse.
        this.scrollPane.setPannable(true);

        // This will set the background color of the pane to a little darker grey, this will make contrast better.
        this.scrollPane.setBackground(new Background(new BackgroundFill(Color.grayRgb(70), CornerRadii.EMPTY, Insets.EMPTY)));

        this.scrollPane.setContent(drawField);

        // Setting vertical scroll bar is always displayed.
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Scene scene = new Scene(this.scrollPane, WIDTH, HEIGHT);

        this.window.setScene(scene);

        // Self-call of the startAnimationTimer method to initialise.
        this.start();
    }

    // This variable is for the adjustment to align the time lines with the labels.
    private final int lineAdjustment = 8;

    /**
     * This method will generate labels and lines for a background where the timeline is visible. This method
     * is able to be scaled with the SCALE variable.
     */
    private void addTimeLine() {

        for (int i = 0; i < 24; i++) {

            Label tempLabel;
            if (i < 10) {
                tempLabel = new Label("0" + i + ":00");
            } else {
                tempLabel = new Label(i + ":00");
            }
            tempLabel.setLayoutY(i * 60 * this.SCALE - this.lineAdjustment);
            tempLabel.setLayoutX(7);

            Line timeDivision1 = new Line();

            timeDivision1.setStartY(i * 60 * this.SCALE);
            timeDivision1.setEndY(i * 60 * this.SCALE);

            timeDivision1.setStartX(this.shiftAdjustment);
            timeDivision1.setEndX(WIDTH);
            timeDivision1.endXProperty().bind(this.scrollPane.widthProperty().subtract(this.lineAdjustment));

            Line timeDivision2 = new Line();

            timeDivision2.setStartY((i * 60 * this.SCALE)-30);
            timeDivision2.setEndY((i * 60 * this.SCALE)-30);

            timeDivision2.setStroke(Color.LIGHTGREY);

            timeDivision2.setStartX(this.shiftAdjustment);
            timeDivision2.setEndX(WIDTH);
            timeDivision2.endXProperty().bind(this.scrollPane.widthProperty().subtract(this.lineAdjustment));

            this.drawField.getChildren().add(timeDivision2);
            this.drawField.getChildren().add(timeDivision1);
            this.drawField.getChildren().add(tempLabel);
        }
        Line verticalDivider = new Line();
        verticalDivider.setEndX(this.shiftAdjustment);
        verticalDivider.setStartX(this.shiftAdjustment);

        verticalDivider.setStartY(0);
        verticalDivider.setEndY(23 * 60 * this.SCALE);

        this.drawField.getChildren().add(verticalDivider);
    }

    // This variable will determine the width of each column
    private int columnSize = 200;

    /**
     * This method will set the column width of the schedules.
     * @param columnSize The column size that the user wants to change to.
     */
    void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    // This final variable is the amount that has to be shifted that there is no overlapping.
    private final int shiftAdjustment = 40;

    // This variable keeps the space between the schedules.
    private final int spaceBetweenSchedules = 26;

    /**
     * This method adds a lesson to the visible lessons, the method will make an rectangle with an x and y that
     * represents the begin time of the lesson, the height of the rectangle will show how long the lesson is.
     * @param column this variable is the number of the column the lesson has to be placed.
     */
    void addLesson(Lesson lesson, int column) {

        Rectangle rectangle = new Rectangle();

        // This event handler will handle clicks on the lesson blocks.
        rectangle.setOnMouseClicked((event) -> {

            AlertHandler.show(Alert.AlertType.INFORMATION, "Information dialog", "Lesson information",
                    "Location: " + lesson.getClassRoom().getName() + "\nGroup: " + lesson.getGroup().getGroupName()
                            + "\nSubject: " + lesson.getSubject().getName() + "\nBegin time: "
                            + lesson.getStartTime().toString() + "\nEnd time: " + lesson.getEndTime().toString()) ;
        });

        Label subjectLabel = new Label(lesson.getSubject().getName());

        if (column == 0) {
            rectangle.setX((this.columnSize * column) + this.shiftAdjustment + (13));
        } else {
            rectangle.setX((this.columnSize * column) + this.shiftAdjustment + (this.spaceBetweenSchedules * (column)) + 13);
        }

        rectangle.setY(timeToAmountOfPixels(lesson.getStartTime()) * this.SCALE);

        rectangle.setWidth(this.columnSize);
        rectangle.setHeight(timeToAmountOfPixels(lesson.getStartTime().timeBetween(lesson.getEndTime())) * this.SCALE);

        subjectLabel.setLayoutX(rectangle.getX() + (5));
        subjectLabel.setLayoutY(timeToAmountOfPixels(lesson.getStartTime()) * this.SCALE);

        if(lesson.getStartTime().timeBetween(lesson.getEndTime()).getHours() == 0 && lesson.getStartTime().timeBetween(lesson.getEndTime()).getMinutes() < 20){
            subjectLabel.setFont(new Font((lesson.getStartTime().timeBetween(lesson.getEndTime()).getMinutes()/20.0)*12));
            subjectLabel.setLayoutY(timeToAmountOfPixels(lesson.getStartTime()) * this.SCALE - (lesson.getStartTime().timeBetween(lesson.getEndTime()).getMinutes()/20.0)*1.5);
        }

        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1.5);

        rectangle.setArcHeight(15);
        rectangle.setArcWidth(15);

        Line scheduleDivider = new Line();
        scheduleDivider.setStartX(rectangle.getX() + this.columnSize + 13);
        scheduleDivider.setEndX(rectangle.getX() + this.columnSize + 13);

        scheduleDivider.setStartY(0);
        scheduleDivider.setEndY(23 * 60 * this.SCALE);

        this.drawField.getChildren().add(scheduleDivider);

        this.drawField.getChildren().add(rectangle);
        this.drawField.getChildren().add(subjectLabel);

    }

    /**
     * The method will simply convert hours and minutes to the amount of pixels according to the scale 1 pixel equals
     * one minute.
     *
     * @param time the time that has to be converted.
     * @return returns the amount of pixels in an integer
     */
    private int timeToAmountOfPixels(TimeStamp time) {

        return (time.getHours() * 60) + (time.getMinutes());

    }

    /**
     * This method will startAnimationTimer the displaying of the UI, by adding an timeline and showing the window.
     */
    protected void start() {

        // Add the timeline to the empty ScrollPane.
        this.addTimeLine();

        // Show the window.
        window.show();

    }

    /**
     * @return the current scroll pane, this is to place the lessons in the application wherever needed.
     */
    public ScrollPane getScrollPane() {

        return scrollPane;

    }

    /**
     * This method will clear the screen and re-render the screen to an empty timeline. This method is able to detach the
     * references between all of the elements in the ScheduleViewer like rectangles, labels, lines, etc. The garbage
     * collector will delete these items afterwards.
     */
    protected void clear(){

        // This will clear the group of elements which will detach all of the references of the rectangles, labels etc.
        this.drawField.getChildren().clear();

        // Re-render the whole screen again to the empty timeline.
        this.start();

    }

}
