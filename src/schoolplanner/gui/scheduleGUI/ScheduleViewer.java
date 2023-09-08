package schoolplanner.gui.scheduleGUI;

import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import schoolplanner.School;
import schoolplanner.gui.schedulepicker.ScheduleDisplayController;
import schoolplanner.gui.startscreen.StartScreenC;
import schoolplanner.calendar.Schedule;
import schoolplanner.util.AlertHandler;


import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

/**
 * @author Nicholas Le Roux & Stijn Lemm
 */
public class ScheduleViewer {

    //The Height and Width of the screen.
    private final int WIDTH = 1000;
    private final int HEIGHT = 500;

    //The Height and Width of the Startscreen.
    private final int STARTSCREENWIDTH = 640;
    private final int STARTSCREENHEIGHT = 480;

    private Stage stage;
    private TextField textFieldScheduleName;
    private ToolBar toolBar;
    private Button buttonAddSchedule;
    private Button buttonRemove;
    private Button buttonResetTable;
    private Button buttonBack;
    private School school;
    private ScheduleDisplayController scheduleDisplayController;
    private ComboBox<String> comboBoxCriteria;
    private ComboBox<Date> comboBoxSchedules;
    private ArrayList<String> usedScheduleName;

    public ScheduleViewer(Stage window, School school){
        stage = window;
        textFieldScheduleName = new TextField();
        toolBar = new ToolBar();
        buttonAddSchedule = new Button("Add lesson");
        buttonRemove = new Button("Remove chosen lesson");
        buttonResetTable = new Button("Clear displayed lessons");
        buttonBack = new Button("Back");
        this.school = school;
        this.scheduleDisplayController = new ScheduleDisplayController(window,WIDTH,HEIGHT);
        comboBoxCriteria = new ComboBox<>();
        comboBoxSchedules = new ComboBox<>();
        usedScheduleName = new ArrayList<>();
    }

    /**
     * Method used in another class to startAnimationTimer the scene/application.
     * Used to set the stage and the functionality of the buttons.
     */
    public void start(){

        //Sets the stage.
        setStage();

        //Checks if the button 'Back' has been pressed and changes the scene to the startAnimationTimer screen.
        setButtonBack();

        //Checks if the button 'Add lessons' has been pressed and adds the lessons of the name given in the TextField.
        setButtonAddSchedule();

        //Checks if the button 'Remove chosen lessons' has been pressed and deletes the selected lessons.
        setButtonRemove();

        //Checks if the button 'Reset schedules' has been pressed and clears the TableView.
        setButtonResetTable();

    }

    /**
     * Sets the components of the stage.
     */
    private void setStage(){
        BorderPane borderPane = new BorderPane();
        textFieldScheduleName.setAlignment(Pos.CENTER);
        textFieldScheduleName.setPromptText("Input name");
        comboBoxCriteria.setPromptText("Filter criteria");

        //Sets the types of criteria to the criteria ComboBox.
        comboBoxCriteria.getItems().addAll("Teacher","Group","Subject","ClassRoom");

        //Sets the dates to the schedule ComboBox.
        for (Date date: school.getCalendar().getSchedules().keySet()) {
            comboBoxSchedules.getItems().add(date);
        }

        comboBoxSchedules.getSelectionModel().selectFirst();

        //Adds all the components to the toolbar on the top of the screen.
        toolBar.getItems().addAll(buttonBack,buttonResetTable,buttonRemove, buttonAddSchedule,
                comboBoxCriteria,textFieldScheduleName,comboBoxSchedules);
        toolBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        borderPane.setTop(toolBar);
        borderPane.setCenter(this.scheduleDisplayController.getScrollPane());

        Scene scene = new Scene(borderPane,WIDTH,HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

//--------------------------------------------------------------------------------------------------------------------//

    //UI inputs (Buttons, textfield, tableview...etc.

    /**
     * Button 'Back'
     * Sets the action that the button back performs.
     */
    private void setButtonBack() {
        buttonBack.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(StartScreenC.class.getResource("startscreen.fxml"));
                Parent root = loader.load();
                StartScreenC startScreenC = loader.getController();
                startScreenC.setSchool(school);
                final Scene startScreen = new Scene(root);
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                primaryStage.setMinWidth(STARTSCREENWIDTH);
                primaryStage.setMinHeight(STARTSCREENHEIGHT);
                primaryStage.setTitle("School calendar Application");
                primaryStage.setScene(startScreen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Button 'Add lessons'
     * Button to add a lesson to the TableView.
     */
    private void setButtonAddSchedule() {
        buttonAddSchedule.setOnAction(event -> {
            if((!(  this.comboBoxSchedules.getSelectionModel().isEmpty() &&
                    this.textFieldScheduleName.getText().isEmpty() &&
                    this.textFieldScheduleName.getText().equals("")))&&
                    this.comboBoxCriteria.getValue() != null) {

                Schedule filteredSchedule = null;

                for (int i = 0; i < school.getCalendar().getSchedules().get(comboBoxSchedules.getValue()).getLessons().size(); i++) {
                    if(comboBoxCriteria.getValue().equals("Teacher")){
                        try {
                            int teacherCode = Integer.parseInt(this.textFieldScheduleName.getText());
                            if (school.getCalendar().getSchedules().get(comboBoxSchedules.getValue()).getLessons().get(i).getTeacher().getTeacherCode() == teacherCode) {
                                filteredSchedule = school.getCalendar().getFilteredSchedule
                                        (comboBoxSchedules.getSelectionModel().getSelectedItem(),
                                                this.comboBoxCriteria.getValue(),
                                                this.textFieldScheduleName.getText());
                            }else {
                                if(i == school.getCalendar().getSchedules().get(comboBoxSchedules.getValue()).getLessons().size()-1 &&
                                        !usedScheduleName.contains(this.textFieldScheduleName.getText())) {
                                    AlertHandler.show(Alert.AlertType.ERROR,
                                            "Add exception",
                                            "An error while trying to add a lesson to the pane.",
                                            "The input name does not exist in " + comboBoxCriteria.getValue() + ".");
                                }
                            }
                        }catch (NumberFormatException e){
                            if(i == school.getCalendar().getSchedules().get(comboBoxSchedules.getValue()).getLessons().size()-1 &&
                                    !usedScheduleName.contains(this.textFieldScheduleName.getText())) {
                                AlertHandler.show(Alert.AlertType.ERROR,
                                        "Add exception",
                                        "An error while trying to add a lesson to the pane.",
                                        "Input a teacher code. Name of the teacher is not accepted");
                            }
                        }
                    } else if (school.getCalendar().getSchedules().get(comboBoxSchedules.getValue()).getLessons().get(i).getGroup().getGroupName().equals(this.textFieldScheduleName.getText())
                            || school.getCalendar().getSchedules().get(comboBoxSchedules.getValue()).getLessons().get(i).getSubject().getName().equals(this.textFieldScheduleName.getText())
                            || school.getCalendar().getSchedules().get(comboBoxSchedules.getValue()).getLessons().get(i).getClassRoom().getName().equals(this.textFieldScheduleName.getText())) {

                        filteredSchedule = school.getCalendar().getFilteredSchedule
                                (comboBoxSchedules.getSelectionModel().getSelectedItem(),
                                        this.comboBoxCriteria.getValue(),
                                        this.textFieldScheduleName.getText());
                    }else {
                        if(i == school.getCalendar().getSchedules().get(comboBoxSchedules.getValue()).getLessons().size()-1 &&
                                !usedScheduleName.contains(this.textFieldScheduleName.getText())) {
                            AlertHandler.show(Alert.AlertType.ERROR,
                                    "Add exception",
                                    "An error while trying to add a lesson to the pane.",
                                    "The input name does not exist in " + comboBoxCriteria.getValue()
                                            + " or the lesson is empty.");
                        }
                    }

                    if (filteredSchedule != null) {
                        if (!usedScheduleName.contains(this.textFieldScheduleName.getText())) {
                            this.scheduleDisplayController.addSchedule(filteredSchedule);
                            if(!this.textFieldScheduleName.getText().isEmpty() || !this.textFieldScheduleName.getText().equals("")) {
                                usedScheduleName.add(this.textFieldScheduleName.getText());
                            }
                        }
                    }
                }
                this.textFieldScheduleName.clear();
            }else {
                AlertHandler.show(Alert.AlertType.ERROR,
                        "Criteria exception",
                        "An error while trying to add a lesson to the pane.",
                        "Choose a criteria type.");
            }
        });
    }

    /**
     * Button 'Remove chosen lesson'
     * Button deletes the selected lesson from the TableView.
     */
    private void setButtonRemove() {
        buttonRemove.setOnAction(event -> {
            if(!(comboBoxSchedules.getSelectionModel().isEmpty() && this.comboBoxCriteria.getValue().isEmpty() &&
                    this.textFieldScheduleName.getText().isEmpty() && this.textFieldScheduleName.getText().equals(""))){

                   Schedule filteredSchedule = school.getCalendar().getFilteredSchedule
                                (comboBoxSchedules.getSelectionModel().getSelectedItem(),
                                        this.comboBoxCriteria.getValue(),
                                        this.textFieldScheduleName.getText());

                if (usedScheduleName.contains(this.textFieldScheduleName.getText())) {
                    try {
                        for (int schedule = 0; schedule < scheduleDisplayController.getDisplayedSchedules().size(); schedule++) {
                            for (int lesson = 0; lesson < scheduleDisplayController.getDisplayedSchedules().get(schedule).getLessons().size(); lesson++) {

                                scheduleDisplayController.removeSchedule(scheduleDisplayController.getDisplayedSchedules().get(schedule));
                                usedScheduleName.remove(this.textFieldScheduleName.getText());

                            }
                        }
                    }catch (IndexOutOfBoundsException e){ e.getMessage(); }
                }else {
                    AlertHandler.show(Alert.AlertType.ERROR,
                            "Remove exception",
                            "An error while trying to remove a lesson from the pane.",
                            "Schedule does not exist on the pane.");
                }
            }
        });
    }

    /**
     * Button 'Reset schedules'
     * Button clears the TableView.
     */
    private void setButtonResetTable() {
        buttonResetTable.setOnAction(event -> {
            scheduleDisplayController.getDisplayedSchedules().clear();
            usedScheduleName.clear();
            scheduleDisplayController.clear();
        });
    }
}

