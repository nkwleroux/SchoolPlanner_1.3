package schoolplanner.gui.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import schoolplanner.School;
import schoolplanner.calendar.Schedule;
import schoolplanner.calendar.lessons.npc.Group;
import schoolplanner.calendar.lessons.npc.Student;
import schoolplanner.calendar.lessons.npc.Teacher;
import schoolplanner.calendar.lessons.rooms.ClassRoom;
import schoolplanner.calendar.lessons.rooms.StaffRoom;
import schoolplanner.calendar.lessons.subject.Subject;
import schoolplanner.util.SimplePropertyConverter;
import schoolplanner.util.AlertHandler;
import schoolplanner.calendar.Calendar;
import schoolplanner.gui.startscreen.StartScreenC;
import schoolplanner.calendar.lessons.*;
import schoolplanner.util.TimeStamp;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.sql.Date;
import java.util.*;

/**
 * @author Tom Spekman & Nicholas Le Roux.
 */
public class Setting {

    private HashMap<String, Subject> teachSubjects;
    private HashMap<String, Teacher> teacherHM;
    private ArrayList<Lesson> lessonsArrayList;

    private BorderPane borderPane;

    private School school;

    private DatePicker scheduleDatePicker;

    private GridPane gridPane;
    private GridPane editGP;

    private TextField name;
    private TextField textIDNumber;
    private TextField lessonStartTimeHour;
    private TextField lessonStartTimeMin;
    private TextField lessonEndTimeHour;
    private TextField lessonEndTimeMin;

    private Button buttonBack;
    private Button buttonAdd;
    private Button buttonSave;
    private Button buttonDelete;
    private Button buttonSaveFile;
    private Button buttonLoad;

    private ComboBox<Date> dateBox;
    private ComboBox<String> teacherSubject;
    private ComboBox<String> dateList;
    private ComboBox<String> lessonRoomBox;
    private ComboBox<String> teacherBox;
    private ComboBox<String> groupList;
    private ComboBox<String> fileList;
    private ComboBox<ObjectTypes> addListBox;

    private VBox vBoxName;
    private VBox vBoxID;
    private VBox vBoxSubjectName;
    private VBox vBoxTeach;
    private VBox vBoxGroup;
    private VBox vBoxCenter;
    private VBox vBoxTeachList;
    private HBox editHBox;
    private HBox hBox;

    private int idNumber;
    private Random random;
    private Lesson selectedLesson;
    private Stage stage;
    private ToolBar toolBar;
    private BorderPane menuPane;
    private Region region;

    private ListView<String> globalListView;
    private TableView<SimplePropertyConverter> globalTableView;

    //schedule
    private TextField textFieldYear;
    private TextField textFieldMonth;
    private TextField textFieldDay;

    //student
    private ArrayList<Student> students;

    //teacher
    private ArrayList<Teacher> teachers;

    private Insets padding;

    /**
     * Constructor for the class Setting. Its function in the application is to serve as the settings GUI.
     *
     * @param window the stage is given from the start menu. In this class it uses the stage to change the scene.
     * @param school is the central point class that holds all the data required for the application to run. It is used
     *               to get data for the schedule.
     */
    public Setting(Stage window, School school) {
        this.borderPane = new BorderPane();
        this.addListBox = new ComboBox<>();
        this.name = new TextField();
        this.buttonBack = new Button("Back");
        this.buttonAdd = new Button("Add");
        this.buttonDelete = new Button("Delete");
        this.buttonSave = new Button("Save");
        this.toolBar = new ToolBar();
        this.menuPane = new BorderPane();
        this.region = new Region();
        this.hBox = new HBox();
        this.hBox.setSpacing(10);
        this.hBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.school = school;
        this.stage = window;
        this.padding = new Insets(10, 10, 10, 10);
        this.random = new Random();
        this.students = new ArrayList<>();
        this.teachers = new ArrayList<>();
    }

    /**
     * Start method of the GUI. Used in the startmenu to display the Setting.
     * This method sets global properties which will be used even when switching from tabs in settings.
     * It sets the top and center of the BorderPane.
     */
    public void start() {
        setScene();
        getTiledRooms();
        borderPane.setCenter(changeOptionMenu(addListBox.getSelectionModel().getSelectedItem()));
        this.addListBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (!addListBox.getSelectionModel().getSelectedItem().equals(ObjectTypes.SaveLoad) &&
                    !addListBox.getSelectionModel().getSelectedItem().equals(ObjectTypes.Room)) {
                hBox.getChildren().remove(1, hBox.getChildren().size());
                hBox.getChildren().addAll(buttonAdd, buttonSave, buttonDelete);
                region.prefWidthProperty().unbind();
                region.prefWidthProperty().bind((borderPane.widthProperty().subtract(310)));
            } else if (addListBox.getSelectionModel().getSelectedItem().equals(ObjectTypes.Room)) {
                hBox.getChildren().removeAll(buttonAdd, buttonSave, buttonDelete);
                hBox.getChildren().addAll(buttonSave);
                region.prefWidthProperty().unbind();
                region.prefWidthProperty().bind((borderPane.widthProperty().subtract(201)));
            } else if (addListBox.getSelectionModel().getSelectedItem().equals(ObjectTypes.SaveLoad)) {
                hBox.getChildren().removeAll(buttonAdd, buttonSave, buttonDelete);
                region.prefWidthProperty().unbind();
                region.prefWidthProperty().bind((borderPane.widthProperty().subtract(150)));
            }

            borderPane.setCenter(changeOptionMenu(addListBox.getSelectionModel().getSelectedItem()));
        });

        hBox.getChildren().addAll(addListBox, buttonAdd, buttonSave, buttonDelete);

        Scene scene = new Scene(borderPane, 600, 360);

        buttonBack.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(StartScreenC.class.getResource("Startscreen.fxml"));
                Parent root = loader.load();
                StartScreenC startscreenC = loader.getController();
                startscreenC.setSchool(school);
                Scene startScreen = new Scene(root);
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                primaryStage.setTitle("School calendar Application");
                primaryStage.setScene(startScreen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Method to set the base properties of the scene of the stage.
     */
    private void setScene() {

        this.addListBox.getItems().addAll(ObjectTypes.values());
        this.addListBox.getSelectionModel().selectFirst();

        menuPane.setLeft(hBox);
        menuPane.setRight(buttonBack);
        menuPane.getRight().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        region.prefWidthProperty().bind((borderPane.widthProperty().subtract(310)));
        menuPane.setCenter(region);
        toolBar.getItems().add(menuPane);
        this.borderPane.setTop(toolBar);

    }

    /**
     * method used to set the components in the center of the BorderPane.
     *
     * @param value Changes according to the type of setting that is selected. The value is used in the switch case to
     *              determine which method needs to be called.
     * @return the BorderPane with the center changed to the selected settings type.
     */
    private BorderPane changeOptionMenu(@NotNull ObjectTypes value) {

        globalListView = new ListView<>();
        vBoxCenter = new VBox();
        editGP = new GridPane();
        gridPane = new GridPane();
        gridPane.setHgap(10);
        vBoxName = new VBox();
        vBoxID = new VBox();
        vBoxSubjectName = new VBox();
        vBoxTeach = new VBox();
        vBoxTeachList = new VBox();
        vBoxGroup = new VBox();
        editHBox = new HBox();

        BorderPane settingsBorderPane = new BorderPane();
        ComboBox<ObjectTypes> editComboBox = new ComboBox<>();
        editComboBox.getItems().addAll(ObjectTypes.values());
        editComboBox.getSelectionModel().selectFirst();

        editHBox.setSpacing(5);
        editHBox.getChildren().add(editComboBox);
        textIDNumber = new TextField();
        textIDNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textIDNumber.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        switch (value) {
            case Schedule: {
                settingsBorderPane.setCenter(getSchedulePane());
                break;
            }
            case Lesson: {
                settingsBorderPane.setCenter(getLessonPane());
                break;
            }
            case Subject: {
                settingsBorderPane.setCenter(getSubjectPane());
                break;
            }
            case Room: {
                settingsBorderPane.setCenter(getRoomPane());
                break;
            }
            case Group: {
                settingsBorderPane.setCenter(getGroupPane());
                break;
            }
            case Student: {
                settingsBorderPane.setCenter(getStudentPane());
                break;
            }
            case Teacher: {
                settingsBorderPane.setCenter(getTeacherPane());
                break;
            }
            case SaveLoad: {
                settingsBorderPane.setCenter(getSaveLoadPane());
                break;
            }
            default:
                break;
        }
        return settingsBorderPane;
    }

    /**
     * This method is used to create the pane in which all the
     * components of the setting option Schedule will be displayed.
     *
     * @return A VBox that will be placed in the center of the BorderPane.
     */
    private Node getSchedulePane() {

        HBox hBoxTextField = new HBox();
        HBox hBoxYear = new HBox();
        HBox hBoxMonth = new HBox();
        HBox hBoxDay = new HBox();

        textFieldYear = new TextField();
        textFieldMonth = new TextField();
        textFieldDay = new TextField();
        scheduleDatePicker = new DatePicker();
        scheduleDatePicker.getEditor().setEditable(false);
        scheduleDatePicker.setFocusTraversable(false);

        vBoxName.getChildren().addAll(new Label("Date:"), scheduleDatePicker);

        for (Date date : school.getCalendar().getSchedules().keySet()) {
            globalListView.getItems().add(String.valueOf(date));
        }

        hBoxYear.getChildren().addAll(new Label("Year:     "), textFieldYear);
        textFieldMonth.setTranslateX(1);
        hBoxMonth.getChildren().addAll(new Label("Month: "), textFieldMonth);
        hBoxDay.getChildren().addAll(new Label("Day:      "), textFieldDay);
        vBoxCenter.getChildren().addAll(vBoxName, globalListView, hBoxTextField, hBoxDay, hBoxMonth, hBoxYear);
        vBoxCenter.setSpacing(10);
        vBoxCenter.setPadding(padding);

        globalListView.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue != null) {
                textFieldYear.setText(newValue.substring(0, 4));
                textFieldMonth.setText(newValue.substring(5, 7));
                textFieldDay.setText(newValue.substring(8, 10));
            }
        });

        buttonsSchedule();

        return vBoxCenter;
    }

    /**
     * Method used to set the function of add, save and delete for settings option Schedule.
     */
    private void buttonsSchedule() {
        buttonSave.setOnAction(event -> {
            //error checks for if the TextFields are empty or if year/month/day is wrong,
            if (textFieldYear.getText().isEmpty() || textFieldMonth.getText().isEmpty() ||
                    textFieldDay.getText().isEmpty()) {
                AlertHandler.show(Alert.AlertType.ERROR,
                        "Date exception",
                        "An error while trying to edit a date",
                        "Please choose a date");
            } else if (textFieldYear.getText().length() != 4) {
                AlertHandler.show(Alert.AlertType.ERROR,
                        "Year exception",
                        "An error while trying to change date",
                        "The year has to be a 4 digit date");
            } else if (textFieldMonth.getText().length() > 2
                    || Integer.parseInt(textFieldMonth.getText()) > 12) {
                AlertHandler.show(Alert.AlertType.ERROR,
                        "Month exception",
                        "An error while trying to change date",
                        "The entered month has to be a digit below 13");
            } else if (textFieldDay.getText().length() > 2
                    || Integer.parseInt(textFieldDay.getText()) > 31) {
                AlertHandler.show(Alert.AlertType.ERROR,
                        "Day exception",
                        "An error while trying to change date",
                        "The entered day has to be a digit below 32");
            } else {

                Date newDate = Date.valueOf(textFieldYear.getText() + "-" +
                        textFieldMonth.getText() + "-" + textFieldDay.getText());
                Date oldDate = Date.valueOf(globalListView.getSelectionModel().getSelectedItem());
                Schedule scheduleValue = school.getCalendar().getSchedules().get(
                        Date.valueOf(globalListView.getSelectionModel().getSelectedItem()));

                if (school.getCalendar().getSchedules().containsKey(oldDate)) {
                    //Adds the new key/name with the old value/data
                    school.getCalendar().getSchedules().put(newDate, scheduleValue);
                    //removes the old key/name with the duplicate old value/data
                    school.getCalendar().removeSchedule(oldDate);

                    textFieldYear.clear();
                    textFieldMonth.clear();
                    textFieldDay.clear();

                    globalListView.getItems().clear();
                    for (Date date : school.getCalendar().getSchedules().keySet()) {
                        globalListView.getItems().add(String.valueOf(date));
                    }
                }
            }

        });

        buttonDelete.setOnAction(event -> {
            if (!globalListView.getSelectionModel().isEmpty()) {
                school.getCalendar().removeSchedule(Date.valueOf(globalListView.getSelectionModel().getSelectedItem()));

                //clears TextFields
                textFieldYear.clear();
                textFieldMonth.clear();
                textFieldDay.clear();

                //sets TableView
                globalListView.getItems().clear();
                for (Date date : school.getCalendar().getSchedules().keySet()) {
                    globalListView.getItems().add(String.valueOf(date));
                }
            } else {
                AlertHandler.show(Alert.AlertType.ERROR,
                        "Delete exception",
                        "An error while trying to delete a date",
                        "Click on the desired date that you want to delete");
            }
        });

        buttonAdd.setOnAction(e -> {
            try {
                Date key = Date.valueOf(scheduleDatePicker.getValue());
                if (!school.getCalendar().getSchedules().containsKey(key)) {
                    school.getCalendar().addSchedule(key);
                    globalListView.getItems().add(key.toString());

                    scheduleDatePicker.getEditor().clear();
                    scheduleDatePicker.getEditor().setText("<Schedule created>");
                } else {
                    scheduleDatePicker.getEditor().clear();
                    scheduleDatePicker.getEditor().setText("<Schedule already exist>");
                }

            } catch (NullPointerException ex) {
                scheduleDatePicker.getEditor().setText("<Choose a date>");
            }
        });
    }

    /**
     * This method is used to create the pane in which all the
     * components of the setting option Lesson will be displayed.
     *
     * @return A VBox that will be placed in the center of the BorderPane.
     */
    private Node getLessonPane() {
        editGP.setVgap(2);
        VBox vBoxID = new VBox();
        VBox vBoxSubjectName = new VBox();
        VBox vBoxStartTime = new VBox();
        VBox vBoxEndTime = new VBox();
        VBox vBoxSchedule = new VBox();
        VBox vBoxRoomType = new VBox();
        VBox vBoxGroup = new VBox();
        HBox hBoxStart = new HBox();
        HBox hBoxEnd = new HBox();
        HBox combineBox = new HBox();
        lessonsArrayList = new ArrayList<>();

        editGP.setHgap(10);
        editGP.setVgap(10);

        DatePicker datePicker = new DatePicker();
        teacherHM = new HashMap<>();

        dateBox = new ComboBox<>();
        dateBox.setPromptText("<No date selected>");
        dateBox.getItems().addAll(school.getCalendar().getSchedules().keySet());

        dateBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            globalListView.getItems().clear();
            lessonsArrayList.clear();
            for (Lesson lesson : school.getCalendar().getSchedules().get(newValue).getLessons()) {
                String name = lesson.getSubject().getName() + " - " + lesson.getTeacher().getName() + " - " + lesson.getStartTime().toString() + "-" + lesson.getEndTime().toString();
                globalListView.getItems().add(name);
                lessonsArrayList.add(lesson);
            }
        });

        dateList = new ComboBox<>();
        dateList.setPromptText("<No date selected>");
        for (Date date : school.getCalendar().getSchedules().keySet()) {
            dateList.getItems().add(date.toString());
        }
        if (editHBox.getChildren().size() > 1) {
            editHBox.getChildren().set(1, dateList);
        } else {
            editHBox.getChildren().add(dateList);
        }

        datePicker.setMaxWidth(100);
        vBoxSchedule.getChildren().addAll(new Label("Date:"), dateList);
        editGP.add(vBoxSchedule, 1, 1);

        lessonStartTimeHour = new TextField();
        lessonStartTimeMin = new TextField();
        lessonStartTimeHour.setPromptText("00");
        lessonStartTimeMin.setPromptText("00");
        lessonStartTimeHour.setMinWidth(30);
        lessonStartTimeHour.setMaxWidth(30);
        lessonStartTimeMin.setMinWidth(30);
        lessonStartTimeMin.setMaxWidth(30);
        lessonStartTimeHour.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                lessonStartTimeHour.setText(newValue.replaceAll("[^\\d]", ""));
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            if (lessonStartTimeHour.getText().length() > 2) {
                String s = lessonStartTimeHour.getText().substring(0, 2);
                lessonStartTimeHour.setText(s);
                newValue = newValue.substring(0, 2);
            }
            if (newValue.length() > 0 && Integer.parseInt(newValue) > 24) {
                lessonStartTimeHour.setText("24");
            }
        });
        lessonStartTimeMin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                lessonStartTimeMin.setText(newValue.replaceAll("[^\\d]", ""));
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            if (lessonStartTimeMin.getText().length() > 2) {
                String s = lessonStartTimeMin.getText().substring(0, 2);
                lessonStartTimeMin.setText(s);
                newValue = newValue.substring(0, 2);
            }
            if (newValue.length() > 0 && Integer.parseInt(newValue) > 59) {
                lessonStartTimeMin.setText("59");
            }
        });

        lessonEndTimeHour = new TextField();
        lessonEndTimeMin = new TextField();
        lessonEndTimeHour.setPromptText("00");
        lessonEndTimeMin.setPromptText("00");
        lessonEndTimeHour.setMinWidth(30);
        lessonEndTimeHour.setMaxWidth(30);
        lessonEndTimeMin.setMinWidth(30);
        lessonEndTimeMin.setMaxWidth(30);
        lessonEndTimeHour.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                lessonEndTimeHour.setText(newValue.replaceAll("[^\\d]", ""));
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            if (lessonEndTimeHour.getText().length() > 2) {
                String s = lessonEndTimeHour.getText().substring(0, 2);
                lessonEndTimeHour.setText(s);
                newValue = newValue.substring(0, 2);
            }
            if (newValue.length() > 0) {
                if (Integer.parseInt(newValue) > 24 && newValue.length() > oldValue.length()) {
                    lessonEndTimeHour.setText("24");
                }
            }
        });
        lessonEndTimeMin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                lessonEndTimeMin.setText(newValue.replaceAll("[^\\d]", ""));
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            if (lessonEndTimeMin.getText().length() > 2) {
                String s = lessonEndTimeMin.getText().substring(0, 2);
                lessonEndTimeMin.setText(s);
                newValue = newValue.substring(0, 2);
            }
            if (newValue.length() > 0) {
                if (Integer.parseInt(newValue) > 59) {
                    lessonEndTimeMin.setText("59");
                }
            }
        });

        hBoxStart.getChildren().clear();
        hBoxStart.getChildren().addAll(lessonStartTimeHour, new Label(":"), lessonStartTimeMin);
        vBoxStartTime.getChildren().addAll(new Label("Starting time:"), hBoxStart);
        editGP.add(vBoxStartTime, 1, 2);

        hBoxEnd.getChildren().clear();
        hBoxEnd.getChildren().addAll(lessonEndTimeHour, new Label(":"), lessonEndTimeMin);
        vBoxEndTime.getChildren().addAll(new Label("End time:"), hBoxEnd);
        editGP.add(vBoxEndTime, 2, 2);

        teacherSubject = new ComboBox<>();
        teacherSubject.setPromptText("<No subject selected>");
        teacherSubject.setPrefWidth(180);
        teacherSubject.getItems().addAll(school.getCalendar().getSubjects().keySet());
        vBoxSubjectName.getChildren().addAll(new Label("Teaching Subject:"), teacherSubject);
        editGP.add(vBoxSubjectName, 1, 3);

        groupList = new ComboBox<>();
        ArrayList<String> tempList = new ArrayList<>(school.getCalendar().getGroups().keySet());
        tempList.remove("No group");
        groupList.getItems().addAll(tempList);
        groupList.setPromptText("<No group selected>");
        groupList.setPrefWidth(180);
        vBoxGroup.getChildren().addAll(new Label("Group:"), groupList);
        editGP.add(vBoxGroup, 2, 3);

        teacherBox = new ComboBox<>();
        teacherBox.setPromptText("<No teacher selected>");
        teacherBox.setPrefWidth(180);
        for (int key : school.getCalendar().getTeachers().keySet()) {
            String name = school.getCalendar().getTeachers().get(key).getName() + " - ID : " + key;
            teacherBox.getItems().add(name);
            teacherHM.put(name, school.getCalendar().getTeachers().get(key));
        }
        vBoxID.getChildren().addAll(new Label("Teacher:"), teacherBox);
        editGP.add(vBoxID, 1, 4);

        lessonRoomBox = new ComboBox<>();
        lessonRoomBox.setPromptText("<No classroom selected>");
        lessonRoomBox.setPrefWidth(180);
        lessonRoomBox.getItems().addAll(school.getCalendar().getClassRooms().keySet());
        vBoxRoomType.getChildren().addAll(new Label("Classroom:"), lessonRoomBox);
        editGP.add(vBoxRoomType, 2, 4);

        globalListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedLesson = lessonsArrayList.get(globalListView.getSelectionModel().getSelectedIndex());
                dateList.setValue(dateBox.getSelectionModel().getSelectedItem().toString());

                lessonStartTimeHour.setText(selectedLesson.getStartTime().getHours() + "");
                lessonStartTimeMin.setText(selectedLesson.getStartTime().getMinutes() + "");

                lessonEndTimeHour.setText(selectedLesson.getEndTime().getHours() + "");
                lessonEndTimeMin.setText(selectedLesson.getEndTime().getMinutes() + "");

                teacherSubject.getSelectionModel().clearSelection();
                teacherSubject.setValue(selectedLesson.getSubject().getName());

                teacherBox.getSelectionModel().clearSelection();
                teacherBox.setValue(selectedLesson.getTeacher().getName() + " - ID : " + selectedLesson.getTeacher().getTeacherCode());

                groupList.getSelectionModel().clearSelection();
                groupList.setValue(selectedLesson.getGroup().getGroupName());

                lessonRoomBox.getSelectionModel().clearSelection();
                lessonRoomBox.setValue(selectedLesson.getClassRoom().getName());
            }
        });

        combineBox.getChildren().add(globalListView);
        combineBox.getChildren().add(editGP);
        combineBox.setSpacing(5);

        VBox editLessonCenter = new VBox();
        HBox dateSelectBox = new HBox();
        Label label = new Label("Date:");
        label.setFont(new Font(25));
        dateSelectBox.getChildren().addAll(label, dateBox);
        dateBox.setTranslateY(5);
        dateSelectBox.setSpacing(10);
        editLessonCenter.getChildren().addAll(dateSelectBox, combineBox);
        editLessonCenter.setPadding(new Insets(10, 10, 10, 10));

        buttonsLesson();
        return editLessonCenter;
    }

    /**
     * Method used to set the function of add, save and delete for settings option Lesson.
     */
    private void buttonsLesson() {
        buttonSave.setOnAction(event -> {
            try {
                school.getCalendar().getSchedules().get(dateBox.getSelectionModel().getSelectedItem()).getLessons().remove(selectedLesson);
                school.getCalendar().addLesson(Date.valueOf(dateList.getSelectionModel().getSelectedItem()),
                        teacherHM.get(teacherBox.getSelectionModel().getSelectedItem()).getTeacherCode(),
                        lessonRoomBox.getSelectionModel().getSelectedItem(),
                        teacherSubject.getSelectionModel().getSelectedItem(),
                        groupList.getSelectionModel().getSelectedItem(),
                        new TimeStamp(Integer.parseInt(lessonStartTimeHour.getText()), Integer.parseInt(lessonStartTimeMin.getText())),
                        new TimeStamp(Integer.parseInt(lessonEndTimeHour.getText()), Integer.parseInt(lessonEndTimeMin.getText())));
            } catch (Exception e) {
                school.getCalendar().getSchedules().get(dateBox.getSelectionModel().getSelectedItem()).getLessons().add(selectedLesson);
            }
            globalListView.getItems().clear();
            lessonsArrayList.clear();
            for (Lesson lesson : school.getCalendar().getSchedules().get(dateBox.getSelectionModel().getSelectedItem()).getLessons()) {
                String name = lesson.getSubject().getName() + " - " + lesson.getTeacher().getName() + " - " + lesson.getStartTime().toString() + "-" + lesson.getEndTime().toString();
                globalListView.getItems().add(name);
                lessonsArrayList.add(lesson);
            }
            globalListView.getSelectionModel().select(globalListView.getItems().size() - 1);
        });

        buttonDelete.setOnAction(event -> {
            int removedLesson = globalListView.getSelectionModel().getSelectedIndex();
            this.school.getCalendar().getSchedules().get(dateBox.getSelectionModel().getSelectedItem()).getLessons().remove(selectedLesson);
            lessonsArrayList.remove(globalListView.getSelectionModel().getSelectedIndex());
            globalListView.getItems().remove(removedLesson);
        });

        buttonAdd.setOnAction(event -> {
            if (!lessonStartTimeHour.getText().isEmpty() && !lessonStartTimeMin.getText().isEmpty()
                    && !lessonEndTimeHour.getText().isEmpty() && !lessonEndTimeMin.getText().isEmpty()
                    && !teacherSubject.getSelectionModel().isEmpty() && !teacherBox.getSelectionModel().isEmpty()
                    && !lessonRoomBox.getSelectionModel().isEmpty()) {
                Date key = Date.valueOf(dateList.getSelectionModel().getSelectedItem());
                try {
                    school.getCalendar().addLesson(key, teacherHM.get(teacherBox.getSelectionModel().getSelectedItem()).getTeacherCode(),
                            lessonRoomBox.getSelectionModel().getSelectedItem(),
                            teacherSubject.getSelectionModel().getSelectedItem(),
                            groupList.getSelectionModel().getSelectedItem(),
                            new TimeStamp(Integer.parseInt(lessonStartTimeHour.getText()), Integer.parseInt(lessonStartTimeMin.getText())),
                            new TimeStamp(Integer.parseInt(lessonEndTimeHour.getText()), Integer.parseInt(lessonEndTimeMin.getText()))
                    );
                    teacherBox.setValue("<No teacher selected>");
                    lessonRoomBox.setValue("<No classroom selected>");
                    teacherSubject.setValue("<No subject selected>");
                    groupList.setValue("<No groups selected>");
                    lessonStartTimeHour.clear();
                    lessonStartTimeMin.clear();
                    lessonEndTimeHour.clear();
                    lessonEndTimeMin.clear();
                    dateList.setValue("<No Schedule selected>");

                    globalListView.getItems().clear();
                    lessonsArrayList.clear();
                    for (Lesson lesson : school.getCalendar().getSchedules().get(dateBox.getSelectionModel().getSelectedItem()).getLessons()) {
                        String name = lesson.getSubject().getName() + " - " + lesson.getTeacher().getName() + " - " + lesson.getStartTime().toString() + "-" + lesson.getEndTime().toString();
                        globalListView.getItems().add(name);
                        lessonsArrayList.add(lesson);
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

    }

    /**
     * This method is used to create the pane in which all the
     * components of the setting option Student will be displayed.
     *
     * @return A VBox that will be placed in the center of the BorderPane.
     */
    private Node getStudentPane() {
        vBoxName.getChildren().add(new Label("Full name:"));
        name = new TextField();
        name.setPromptText("<Enter a name>");
        vBoxName.getChildren().add(name);
        gridPane.add(vBoxName, 1, 1);

        groupList = new ComboBox<>();
        groupList.getItems().addAll(school.getCalendar().getGroups().keySet());
        groupList.getItems().remove("No group");
        groupList.setPromptText("<Select a Group>");
        vBoxGroup.getChildren().addAll(new Label("Group:"), groupList);
        gridPane.add(vBoxGroup, 2, 1);

        vBoxID.getChildren().add(new Label("ID Number(optional):"));
        textIDNumber.setPromptText("<Enter a Number>");
        vBoxID.getChildren().add(textIDNumber);
        gridPane.add(vBoxID, 1, 2);

        gridPane.setTranslateX(-10);
        gridPane.setPadding(new Insets(0, 0, 10, 0));

        //tableview
        globalTableView = new TableView<>();

        TableColumn<SimplePropertyConverter, String> nameTB = new TableColumn<>("Student Name");
        nameTB.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameTB.prefWidthProperty().bind(borderPane.widthProperty().divide(2).subtract(53));
        TableColumn<SimplePropertyConverter, Integer> IDTB = new TableColumn<>("Student ID");
        IDTB.setCellValueFactory(new PropertyValueFactory<>("ID"));
        IDTB.prefWidthProperty().bind(borderPane.widthProperty().divide(3).subtract(125));
        TableColumn<SimplePropertyConverter, String> groupTB = new TableColumn<>("Group");
        groupTB.setCellValueFactory(new PropertyValueFactory<>("studentGroup"));
        groupTB.prefWidthProperty().bind(borderPane.widthProperty().divide(2).subtract(53));

        globalTableView.getItems().clear();
        students.clear();

        if (!school.getCalendar().getStudents().isEmpty()) {
            for (Integer i : school.getCalendar().getStudents().keySet()) {
                globalTableView.getItems().add(new SimplePropertyConverter(school.getCalendar().getStudents().get(i)));

                students.add(school.getCalendar().getStudents().get(i));
            }
        }

        globalTableView.getColumns().addAll(nameTB, IDTB, groupTB);

        globalTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                name.setText(students.get(globalTableView.getItems().indexOf(newValue)).getName());
                textIDNumber.setText(String.valueOf(students.get(globalTableView.getItems().indexOf(newValue)).getStudentNumber()));
                groupList.setValue(students.get(globalTableView.getItems().indexOf(newValue)).getGroupName());
            }
        });

        vBoxCenter = new VBox();
        vBoxCenter.getChildren().addAll(gridPane, globalTableView);
        vBoxCenter.setPadding(padding);


        buttonsStudent();

        return vBoxCenter;
    }

    /**
     * Method used to set the function of add, save and delete for settings option Student.
     */
    private void buttonsStudent() {
        buttonSave.setOnAction(event -> {

            if (globalTableView.getSelectionModel().getSelectedIndex() > 0 || !globalTableView.getSelectionModel().isEmpty()) {
                if (!name.getText().isEmpty() && !textIDNumber.getText().isEmpty()) {
                    Student oldStudent = students.get(globalTableView.getSelectionModel().getSelectedIndex());
                    Student newStudent = new Student(name.getText(), Integer.parseInt(textIDNumber.getText()));

                    if (!school.getCalendar().getStudents().containsKey(Integer.parseInt(textIDNumber.getText()))
                            && !school.getCalendar().getTeachers().containsKey(Integer.parseInt(textIDNumber.getText()))
                            || oldStudent.getStudentNumber() == newStudent.getStudentNumber()) {
                        newStudent.setGroup(school.getCalendar().getGroups().get(groupList.getValue()));
                        if (newStudent.getStudentNumber() == oldStudent.getStudentNumber()) {
                            school.getCalendar().getStudents().put(oldStudent.getStudentNumber(), newStudent);
                        } else {
                            school.getCalendar().getStudents().put(newStudent.getStudentNumber(), newStudent);
                            school.getCalendar().getStudents().remove(oldStudent.getStudentNumber());
                        }
                        school.getCalendar().getGroups().get(oldStudent.getGroupName()).removeStudent(oldStudent);
                        school.getCalendar().getGroups().get(newStudent.getGroupName()).addStudent(newStudent);
                        students.clear();
                        globalTableView.getItems().clear();

                        if (!school.getCalendar().getStudents().isEmpty()) {
                            for (Integer i : school.getCalendar().getStudents().keySet()) {
                                globalTableView.getItems().add(new SimplePropertyConverter(school.getCalendar().getStudents().get(i)));
                                students.add(school.getCalendar().getStudents().get(i));
                            }
                        }

                        name.clear();
                        textIDNumber.clear();
                        groupList.getSelectionModel().clearSelection();
                    } else if (name.getText().isEmpty()) {
                        AlertHandler.show(Alert.AlertType.ERROR,
                                "Edit Error",
                                "An error occurred while trying to edit a student.",
                                "ID number already exists.");
                    }
                } else if (name.getText().isEmpty()) {
                    AlertHandler.show(Alert.AlertType.ERROR,
                            "Edit Error",
                            "An error occurred while trying to edit a student.",
                            "Students name cannot be blank");
                } else if (textIDNumber.getText().isEmpty()) {
                    AlertHandler.show(Alert.AlertType.ERROR,
                            "Edit Error",
                            "An error occurred while trying to edit a student.",
                            "ID number cannot be blank.");
                }
            } else {
                AlertHandler.show(Alert.AlertType.ERROR,
                        "Edit Error",
                        "An error occurred while trying to edit a student.",
                        "Select a student to edit.");
            }
        });

        buttonDelete.setOnAction(event -> {

            Student oldStudent = students.get(globalTableView.getSelectionModel().getSelectedIndex());

            school.getCalendar().getGroups().get(oldStudent.getGroupName()).removeStudent(oldStudent);
            school.getCalendar().removeStudent(oldStudent);

            students.clear();
            globalTableView.getItems().clear();

            if (!school.getCalendar().getStudents().isEmpty()) {
                for (Integer i : school.getCalendar().getStudents().keySet()) {
                    globalTableView.getItems().add(new SimplePropertyConverter(school.getCalendar().getStudents().get(i)));
                    students.add(school.getCalendar().getStudents().get(i));
                }
            }
            name.clear();
            textIDNumber.clear();
            groupList.setValue("<Select a group>");
        });

        buttonAdd.setOnAction(event -> {
            if (!name.getText().isEmpty() && !groupList.getSelectionModel().isEmpty()) {
                Student newStudent;
                if (textIDNumber.getText().isEmpty()) {
                    idNumber = random.nextInt(999999);
                    while (this.school.getCalendar().getTeachers().containsKey(idNumber) || this.school.getCalendar().getStudents().containsKey(idNumber)) {
                        idNumber = random.nextInt(999999);
                    }
                    newStudent = new Student(name.getText(), idNumber);
                } else {
                    newStudent = new Student(name.getText(), Integer.parseInt(textIDNumber.getText()));
                }
                if (!school.getCalendar().getGroups().get(groupList.getValue()).getStudents().
                        containsKey(newStudent.getStudentNumber()) &&
                        !school.getCalendar().getStudents().containsKey(newStudent.getStudentNumber()) &&
                        !school.getCalendar().getTeachers().containsKey(newStudent.getStudentNumber())) {
                    school.getCalendar().getGroups().get(groupList.getValue()).addStudent(newStudent);
                    school.getCalendar().addStudent(newStudent);
                    globalTableView.getItems().add(new SimplePropertyConverter(newStudent));
                    students.add(newStudent);
                    textIDNumber.clear();
                    name.clear();
                    groupList.getSelectionModel().clearSelection();
                } else {
                    AlertHandler.show(Alert.AlertType.ERROR,
                            "Add Error",
                            "An error while trying to add a new student.",
                            "Student with that ID number already exists.");
                }
            } else if (name.getText().isEmpty()) {
                AlertHandler.show(Alert.AlertType.ERROR,
                        "Add Error",
                        "An error occurred while trying to add a student.",
                        "Set a name for the student.");
            } else if (groupList.getSelectionModel().isEmpty()) {
                AlertHandler.show(Alert.AlertType.ERROR,
                        "Add Error",
                        "An error occurred while trying to add a student.",
                        "Select a group for the student.");
            }
        });
    }

    /**
     * This method is used to create the pane in which all the
     * components of the setting option Teacher will be displayed.
     *
     * @return A VBox that will be placed in the center of the BorderPane.
     */
    private Node getTeacherPane() {
        vBoxName.getChildren().add(new Label("Full name:"));
        name = new TextField();
        name.setPromptText("<Enter a name>");
        vBoxName.getChildren().add(name);
        vBoxName.getChildren().add(new Label("ID Number(optional):"));
        vBoxName.getChildren().add(textIDNumber);

        textIDNumber.setPromptText("<Enter a Number>");
        gridPane.add(vBoxName, 1, 1);

        vBoxSubjectName.getChildren().add(new Label("Teaching Subject:"));
        teachSubjects = new HashMap<>();
        teacherSubject = new ComboBox<>();
        teacherSubject.setPromptText("<No subject selected>");
        for (String key : school.getCalendar().getSubjects().keySet()) {
            teacherSubject.getItems().add(school.getCalendar().getSubjects().get(key).getName());
        }
        teacherSubject.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String subjectName = teacherSubject.getSelectionModel().getSelectedItem();
            if (!teachSubjects.containsKey(subjectName) && subjectName != null) {
                teachSubjects.put(subjectName, school.getCalendar().getSubjects().get(subjectName));
                HBox hBoxSubjectList = new HBox();
                Button removeSubject = new Button("Delete");
                Label subjectLabel = new Label(subjectName);
                removeSubject.setOnAction(event -> {
                    vBoxTeachList.getChildren().remove(hBoxSubjectList);
                    teachSubjects.remove(subjectName);
                });
                hBoxSubjectList.getChildren().addAll(subjectLabel, removeSubject);
                vBoxTeachList.getChildren().add(hBoxSubjectList);
            }
        });

        vBoxSubjectName.getChildren().add(teacherSubject);
        gridPane.add(vBoxSubjectName, 2, 1);

        vBoxTeach.getChildren().add(new Label("Selected subjects:"));
        vBoxTeach.getChildren().add(vBoxTeachList);
        gridPane.add(vBoxTeach, 3, 1);

        gridPane.setTranslateX(-10);
        gridPane.setPadding(new Insets(0, 0, 10, 0));

        //tableview
        globalTableView = new TableView<>();
//        globalTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SimplePropertyConverter, String> nameTB = new TableColumn<>("Teacher Name");
        nameTB.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameTB.prefWidthProperty().bind(borderPane.widthProperty().divide(2).subtract(53));
        TableColumn<SimplePropertyConverter, Integer> IDTB = new TableColumn<>("Teacher ID");
        IDTB.setCellValueFactory(new PropertyValueFactory<>("ID"));
        IDTB.prefWidthProperty().bind(borderPane.widthProperty().divide(3).subtract(125));
        TableColumn<SimplePropertyConverter, String> subjectsTB = new TableColumn<>("Subjects");
        subjectsTB.setCellValueFactory(new PropertyValueFactory<>("teacherSubjects"));
        subjectsTB.prefWidthProperty().bind(borderPane.widthProperty().divide(2).subtract(53));

        globalTableView.getItems().clear();
        teachers.clear();

        if (!school.getCalendar().getTeachers().isEmpty()) {
            for (Integer i : school.getCalendar().getTeachers().keySet()) {
                globalTableView.getItems().add(new SimplePropertyConverter(school.getCalendar().getTeachers().get(i)));
                teachers.add(school.getCalendar().getTeachers().get(i));
            }
        }

        globalTableView.getColumns().addAll(nameTB, IDTB, subjectsTB);

        //selected item in list
        globalTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            teacherSubject.getSelectionModel().clearSelection();
            teachSubjects.clear();
            vBoxTeachList.getChildren().clear();
            name.clear();
            textIDNumber.clear();
            if (newValue != null) {
                name.setText(teachers.get(globalTableView.getItems().indexOf(newValue)).getName());
                textIDNumber.setText(String.valueOf(teachers.get(globalTableView.getItems().indexOf(newValue)).getTeacherCode()));
                for (String subject : school.getCalendar().getTeachers().get
                        (teachers.get(globalTableView.getItems().indexOf(newValue)).getTeacherCode()).getSubjects()) {
                    teachSubjects.put(subject, school.getCalendar().getSubjects().get(subject));
                    HBox hBoxSubjectList = new HBox();
                    Button removeSubject = new Button("Delete");
                    Label subjectLabel = new Label(subject);
                    removeSubject.setOnAction(event -> {
                        teachSubjects.remove(subject);
                        vBoxTeachList.getChildren().remove(hBoxSubjectList);
                    });
                    teacherSubject.setValue(subject);
                    hBoxSubjectList.getChildren().addAll(subjectLabel, removeSubject);

                    vBoxTeachList.getChildren().add(hBoxSubjectList);
                }
            }
        });

        vBoxCenter = new VBox();
        vBoxCenter.getChildren().addAll(gridPane, globalTableView);
        vBoxCenter.setPadding(padding);

        buttonsTeacher();

        return vBoxCenter;
    }

    /**
     * Method used to set the function of add, save and delete for settings option Teacher.
     */
    private void buttonsTeacher() {

        buttonSave.setOnAction(event -> {

            if (!teacherSubject.getValue().isEmpty() && !globalTableView.getSelectionModel().isEmpty()) {
                Teacher oldTeacher = teachers.get(globalTableView.getSelectionModel().getSelectedIndex());
                Teacher newTeacher = new Teacher(name.getText(), Integer.parseInt(textIDNumber.getText()),
                        teachSubjects);
                if (!school.getCalendar().getStudents().containsKey(Integer.parseInt(textIDNumber.getText()))
                        && !school.getCalendar().getTeachers().containsKey(Integer.parseInt(textIDNumber.getText()))
                        || oldTeacher.getTeacherCode() == newTeacher.getTeacherCode()) {


                    int tableIndex = globalTableView.getSelectionModel().getSelectedIndex();

                    globalTableView.getItems().set(tableIndex,
                            new SimplePropertyConverter(newTeacher));
                    teachers.set(tableIndex, newTeacher);

                    school.getCalendar().getTeachers().put(oldTeacher.getTeacherCode(), newTeacher);
                    for (String subject : teachSubjects.keySet()) {

                        school.getCalendar().getSubjects().get(subject).addTeacher(newTeacher);
                    }
                    name.clear();
                    textIDNumber.clear();
                    teacherSubject.getSelectionModel().clearSelection();
                } else {
                    AlertHandler.show(Alert.AlertType.ERROR,
                            "Edit Error",
                            "An error occurred while trying to edit a teacher.",
                            "Select a teacher to edit.");
                }
            } else {
                if (globalTableView.getSelectionModel().isEmpty()) {
                    AlertHandler.show(Alert.AlertType.ERROR,
                            "Edit Error",
                            "An error occurred while trying to edit a teacher.",
                            "Select a teacher to edit.");

                }
            }
        });

        buttonDelete.setOnAction(event -> {

            if (!globalTableView.getSelectionModel().isEmpty()) {
                Teacher oldTeacher = teachers.get(globalTableView.getSelectionModel().getSelectedIndex());

                for (String subject : school.getCalendar().getSubjects().keySet()) {
                    if (school.getCalendar().getSubjects().get(subject).getTeachers().containsKey(oldTeacher.getTeacherCode())) {
                        school.getCalendar().getSubjects().get(subject).removeTeacher(oldTeacher);
                    }
                }
                school.getCalendar().removeTeacher(oldTeacher);

                name.clear();
                textIDNumber.clear();
                teacherSubject.getSelectionModel().clearSelection();
                teachers.clear();
                globalTableView.getItems().clear();

                if (!school.getCalendar().getTeachers().isEmpty()) {
                    for (Integer i : school.getCalendar().getTeachers().keySet()) {
                        globalTableView.getItems().add(new SimplePropertyConverter(school.getCalendar().getTeachers().get(i)));
                        teachers.add(school.getCalendar().getTeachers().get(i));
                    }
                }
            } else {
                AlertHandler.show(Alert.AlertType.ERROR,
                        "Delete Error",
                        "An error occurred while trying to delete a teacher.",
                        "Select a teacher to delete.");
            }
        });

        buttonAdd.setOnAction(event -> {
            if (!name.getText().isEmpty() && !this.teacherSubject.getSelectionModel().isEmpty()) {
                Teacher newTeacher;
                if (textIDNumber.getText().isEmpty()) {
                    idNumber = random.nextInt(999999);
                    while (this.school.getCalendar().getTeachers().containsKey(idNumber) || this.school.getCalendar().getStudents().containsKey(idNumber))
                        idNumber = random.nextInt(999999);
                    newTeacher = new Teacher(name.getText(), idNumber, teachSubjects);
                } else if (!school.getCalendar().getTeachers().containsKey(Integer.parseInt(textIDNumber.getText())) &&
                        !school.getCalendar().getStudents().containsKey(Integer.parseInt(textIDNumber.getText()))) {
                    textIDNumber.clear();
                    AlertHandler.show(Alert.AlertType.ERROR,
                            "Add Error",
                            "An error while trying to add a new teacher.",
                            "Number already taken ");
                    return;
                } else {
                    newTeacher = new Teacher(name.getText(), Integer.parseInt(textIDNumber.getText()), teachSubjects);
                }

                if (!school.getCalendar().getStudents().containsKey(Integer.parseInt(textIDNumber.getText()))
                        && !school.getCalendar().getTeachers().containsKey(Integer.parseInt(textIDNumber.getText()))) {
                    this.school.getCalendar().addTeacher(newTeacher);
                    globalTableView.getItems().add(new SimplePropertyConverter(newTeacher));
                    teachers.add(school.getCalendar().getTeachers().get(newTeacher.getTeacherCode()));
                } else {
                    AlertHandler.show(Alert.AlertType.ERROR,
                            "Add Error",
                            "An error while trying to add a new teacher.",
                            "Number already taken ");
                }

                teacherSubject.getSelectionModel().clearSelection();
                vBoxTeachList.getChildren().clear();
                teachSubjects.clear();
                textIDNumber.clear();
                name.clear();
            } else {
                if (name.getText().isEmpty()) {
                    name.clear();
                    AlertHandler.show(Alert.AlertType.ERROR,
                            "Add Error",
                            "An error while trying to add a new teacher.",
                            "Enter a name");
                } else if (teacherSubject.getSelectionModel().isEmpty()) {
                    AlertHandler.show(Alert.AlertType.ERROR,
                            "Add Error",
                            "An error while trying to add a new teacher.",
                            "No subject selected.");
                }
            }
        });

    }

    /**
     * This method is used to create the pane in which all the
     * components of the setting option Group will be displayed.
     *
     * @return A VBox that will be placed in the center of the BorderPane.
     */
    private Node getGroupPane() {
        name = new TextField();
        name.setPromptText("<Enter a Group name>");
        vBoxName.getChildren().addAll(new Label("Group name"), name);
        gridPane.add(vBoxName, 1, 1);

        gridPane.setTranslateX(-10);
        gridPane.setPadding(new Insets(0, 0, 10, 0));

        globalListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                name.setText(newValue));

        for (String groupName : school.getCalendar().getGroups().keySet()) {
            if (!groupName.equals("No group")) {
                globalListView.getItems().add(groupName);
            }
        }

        vBoxCenter = new VBox();
        vBoxCenter.getChildren().addAll(gridPane, globalListView);
        vBoxCenter.setPadding(padding);

        buttonsGroup();

        return vBoxCenter;
    }

    /**
     * Method used to set the function of add, save and delete for settings option Group.
     */
    private void buttonsGroup() {
        buttonSave.setOnAction(event -> {
            String oldGroup = globalListView.getSelectionModel().getSelectedItem();
            Group newGroup = school.getCalendar().getGroups().get
                    (globalListView.getSelectionModel().getSelectedItem());
            String newGroupName = name.getText();

            school.getCalendar().getGroups().remove(oldGroup);
            school.getCalendar().getGroups().put(newGroupName, newGroup);
            newGroup.setGroupName(newGroupName);

            globalListView.getItems().clear();
            for (String groupName : school.getCalendar().getGroups().keySet()) {
                if (!groupName.equals("No group")) {
                    globalListView.getItems().add(groupName);
                }
            }
            name.clear();

        });

        buttonDelete.setOnAction(event -> {
            Group oldGroup = school.getCalendar().getGroups().get(globalListView.getSelectionModel().getSelectedItem());
            school.getCalendar().getGroups().remove(globalListView.getSelectionModel().getSelectedItem());
            Group noGroup = school.getCalendar().getGroups().get("No group");
            for (Integer i : school.getCalendar().getStudents().keySet()) {
                if (school.getCalendar().getStudents().get(i).getGroupName().equals(oldGroup.getGroupName())) {
                    school.getCalendar().getStudents().get(i).setGroup(noGroup);
                }
            }
            globalListView.getItems().clear();
            for (String groupName : school.getCalendar().getGroups().keySet()) {
                if (!groupName.equals("No group")) {
                    globalListView.getItems().add(groupName);
                }
            }
            name.clear();
        });


        buttonAdd.setOnAction(event -> {
            if (!school.getCalendar().getGroups().containsKey(name.getText()) && !name.getText().isEmpty()) {
                school.getCalendar().addGroup(new Group(name.getText()));
                globalListView.getItems().add(name.getText());
                name.clear();
            } else {
                if (school.getCalendar().getGroups().containsKey(name.getText())) {
                    name.clear();
                    name.setPromptText("<Name already in use>");
                }
                if (name.getText().isEmpty()) {
                    name.clear();
                    name.setPromptText("<Enter a name>");
                }
            }
        });
    }

    /**
     * This method is used to create the pane in which all the
     * components of the setting option Subject will be displayed.
     *
     * @return A VBox that will be placed in the center of the BorderPane.
     */
    private Node getSubjectPane() {

        vBoxName.getChildren().add(new Label("Subject name:"));
        name = new TextField();
        name.setMaxWidth(150);
        name.setPromptText("<Enter a name>");
        vBoxName.getChildren().add(name);

        vBoxCenter = new VBox();
        vBoxCenter.getChildren().addAll(vBoxName, globalListView);
        vBoxCenter.setSpacing(10);
        vBoxCenter.setPadding(padding);

        //Clears the table and adds the subjects from calendar to the list at the startAnimationTimer of the page opened.
        globalListView.getItems().clear();

        for (String subject : school.getCalendar().getSubjects().keySet()) {
            globalListView.getItems().add(subject);
        }

        globalListView.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue != null) {
                name.setText(newValue);
            }
        });

        buttonsSubject();
        return vBoxCenter;
    }

    /**
     * Method used to set the function of add, save and delete for settings option Subject.
     */
    private void buttonsSubject() {
        buttonSave.setOnAction(e -> {

            Subject newSubject = new Subject(name.getText());
            String oldSubject = globalListView.getSelectionModel().getSelectedItem();

            for (Integer i : school.getCalendar().getTeachers().keySet()) {
                for (String subject : school.getCalendar().getSubjects().keySet()) {
                    if (school.getCalendar().getSubjects().get(subject).getName().equals(oldSubject)) {
                        if (school.getCalendar().getTeachers().get(i).getSubjects().contains(subject)) {
                            school.getCalendar().getTeachers().get(i).getSubjects().remove(subject);
                            school.getCalendar().getTeachers().get(i).getSubjects().add(newSubject.getName());
                        }
                    }
                }
            }
            school.getCalendar().removeSubject(school.getCalendar().getSubjects().get(oldSubject));
            school.getCalendar().addSubject(newSubject);

            name.clear();

            //Updates list with changed data
            globalListView.getItems().clear();
            for (String subject : school.getCalendar().getSubjects().keySet()) {
                globalListView.getItems().add(subject);
            }
        });

        buttonDelete.setOnAction(e -> {

            String oldSubject = globalListView.getSelectionModel().getSelectedItem();

            for (Integer i : school.getCalendar().getTeachers().keySet()) {
                school.getCalendar().getTeachers().get(i).getSubjects().remove(oldSubject);
            }

            school.getCalendar().removeSubject(school.getCalendar().getSubjects().get(oldSubject));

            name.clear();

            //Updates list with changed data.
            globalListView.getItems().clear();
            for (String subject : school.getCalendar().getSubjects().keySet()) {
                globalListView.getItems().add(subject);
            }
        });

        buttonAdd.setOnAction(event -> {
            if (school.getCalendar().getSubjects().isEmpty() || (!name.getText().isEmpty() && !school.getCalendar().getSubjects().containsKey(name.getText()))) {
                this.school.getCalendar().addSubject(new Subject(name.getText()));
                globalListView.getItems().add(name.getText());
                name.clear();
            } else {
                if (name.getText().isEmpty()) {
                    name.clear();
                    name.setPromptText("Enter a name");
                }
                if (school.getCalendar().getSubjects().containsKey(name.getText())) {
                    name.clear();
                    name.setPromptText("Name already in use");
                }
            }
        });

    }

    /**
     * This method is used to create the pane in which all the
     * components of the setting option Room will be displayed.
     *
     * @return A VBox that will be placed in the center of the BorderPane.
     */
    private Node getRoomPane() {

        globalTableView = new TableView<>();
        globalTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SimplePropertyConverter, String> roomNameTB = new TableColumn<>("Room name");
        roomNameTB.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        roomNameTB.prefWidthProperty().bind(borderPane.widthProperty().divide(3));
        TableColumn<SimplePropertyConverter, String> roomCapacityTB = new TableColumn<>("Room capacity");
        roomCapacityTB.setCellValueFactory(new PropertyValueFactory<>("roomCapacity"));
        roomCapacityTB.prefWidthProperty().bind(borderPane.widthProperty().divide(3));
        TableColumn<SimplePropertyConverter, String> roomTypeTB = new TableColumn<>("Room type");
        roomTypeTB.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        roomTypeTB.prefWidthProperty().bind(borderPane.widthProperty().divide(3));

        globalTableView.getColumns().addAll(roomNameTB, roomCapacityTB, roomTypeTB);

        globalTableView.getItems().clear();

        for (String classroom : school.getCalendar().getClassRooms().keySet()) {
            globalTableView.getItems().add(new SimplePropertyConverter(classroom, school.getCalendar().getClassRooms().get(classroom), "Classroom"));
        }
        for (String staffroom : school.getCalendar().getStaffRooms().keySet()) {
            globalTableView.getItems().add(new SimplePropertyConverter(staffroom, school.getCalendar().getStaffRooms().get(staffroom), "Staffroom"));
        }

        //TextFields
        vBoxName.getChildren().add(new Label("Room name:"));
        name = new TextField();
        name.setPromptText("<Enter a Room name>");
        vBoxName.getChildren().add(name);
        vBoxName.setPadding(new Insets(0, 0, 10, 0));
        gridPane.add(vBoxName, 1, 1);
        gridPane.setTranslateX(-10);

        //room fields text prompt
        globalTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                name.setText(newValue.getRoomName());
            }
        });

        vBoxCenter.getChildren().addAll(gridPane, globalTableView);
        vBoxCenter.setPadding(padding);

        buttonsRoom();

        return vBoxCenter;
    }

    /**
     * Method used to set the function of add, save and delete for settings option Room.
     */
    private void buttonsRoom() {
        buttonSave.setOnAction(event -> {
            if (!name.getText().isEmpty()) {
                SimplePropertyConverter selectedItem = globalTableView.getSelectionModel().getSelectedItem();
                String room = selectedItem.getRoomName();

                if (selectedItem.getRoomType().equals("Classroom")) {
                    ClassRoom classRoom = globalTableView.getSelectionModel().getSelectedItem().getClassRoom();
                    if (!school.getCalendar().getClassRooms().containsKey(room)) {
                        school.getCalendar().getClassRooms().remove(room);
                        school.getCalendar().getClassRooms().put(name.getText(), classRoom);
                    }
                }
                if (selectedItem.getRoomType().equals("Staffroom")) {
                    StaffRoom staffRoom = selectedItem.getStaffRoom();
                    if (school.getCalendar().getStaffRooms().containsKey(room)) {
                        school.getCalendar().getStaffRooms().put(name.getText(), staffRoom);
                    }
                }

                globalTableView.getItems().clear();

                for (String classroom : school.getCalendar().getClassRooms().keySet()) {
                    globalTableView.getItems().add(new SimplePropertyConverter(classroom, school.getCalendar().getClassRooms().get(classroom),
                            "Classroom"));
                }
                for (String staffroom : school.getCalendar().getStaffRooms().keySet()) {
                    globalTableView.getItems().add(new SimplePropertyConverter(staffroom, school.getCalendar().getStaffRooms().get(staffroom)
                            , "Staffroom"));
                }
                name.clear();
            }
        });
    }

    /**
     * Method used to import all the rooms that are used in the tiled map. It imports the data from a JSON file
     * and adds them to the calendar.
     */
    private void getTiledRooms() {

        //loading of classroom names from json file/tiled map
        JsonReader reader = Json.createReader(getClass().getResourceAsStream("/pokemon_school_finito.json"));
        JsonObject object = reader.readObject();
        for (int i = 0; i < object.getJsonArray("layers").size(); i++) {
            JsonObject layer = object.getJsonArray("layers").getJsonObject(i);
            if (layer.getString("type").equals("objectgroup")) {
                for (int j = 0; j < layer.getJsonArray("objects").size(); j++) {
                    JsonObject temp = layer.getJsonArray("objects").getJsonObject(j);
                    if (!school.getCalendar().getClassRooms().containsKey(temp.getString("name"))
                            && !school.getCalendar().getStaffRooms().containsKey(temp.getString("name"))
                            && !temp.getString("name").contains("chair")
                            && !temp.getString("name").contains("teach Down")
                            && !temp.getString("name").contains("Exit")) {
                        if (temp.getString("name").contains("Canteen")) {
                            StaffRoom staffroom = new StaffRoom(temp.getString("name"), 60);
                            school.getCalendar().addStaffRoom(staffroom);
                        } else if (temp.getString("name").contains("Toilets")) {
                            StaffRoom staffroom = new StaffRoom(temp.getString("name"), 4);
                            school.getCalendar().addStaffRoom(staffroom);
                        } else if (temp.getString("name").contains("TeacherRoom")) {
                            StaffRoom staffroom = new StaffRoom(temp.getString("name"), 5);
                            school.getCalendar().addStaffRoom(staffroom);
                        } else {
                            ClassRoom classRoom = new ClassRoom(temp.getString("name"), 9);
                            school.getCalendar().addClassRoom(classRoom);
                        }
                    }
                }
            }
        }
    }

    /**
     * This method is used to create the pane in which all the
     * components of the setting option SaveLoad will be displayed.
     *
     * @return A VBox that will be placed in the center of the BorderPane.
     */
    private Node getSaveLoadPane() {
        fileList = new ComboBox<>();
        buttonSaveFile = new Button("Save");
        buttonLoad = new Button("Load");
        name = new TextField();

        vBoxName.getChildren().add(new Label("Save name:"));
        vBoxName.getChildren().add(name);
        name.setPromptText("<Enter a name>");
        vBoxName.getChildren().add(buttonSaveFile);
        buttonSaveFile.setTranslateY(10);
        gridPane.add(vBoxName, 1, 1);

        vBoxSubjectName.getChildren().add(new Label("Load file:"));

        try {
            File file = new File("./saves");
            fileList.getItems().clear();
            for (File child : Objects.requireNonNull(file.listFiles())) {
                if (!child.getName().equals(".gitkeep")) {
                    fileList.getItems().add(child.getName().substring(0, child.getName().length() - 5));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        fileList.setPromptText("<No file selected>");
        vBoxSubjectName.getChildren().add(fileList);
        HBox loadDeleteButtons = new HBox();
        loadDeleteButtons.getChildren().add(buttonLoad);
        loadDeleteButtons.getChildren().add(buttonDelete);
        loadDeleteButtons.setSpacing(48);
        vBoxSubjectName.getChildren().add(loadDeleteButtons);
        gridPane.add(vBoxSubjectName, 2, 1);

        gridPane.setPadding(new Insets(10, 0, 0, 0));
        buttonsSaveLoad();
        return gridPane;
    }

    /**
     * Method used to set the function of save file, load file and delete file for settings option SaveLoad.
     */
    private void buttonsSaveLoad() {
        buttonSaveFile.setOnAction(event -> {
            if (!name.getText().equals("") && !fileList.getItems().contains(name.getText())) {
                try (FileWriter writer = new FileWriter("./saves/" + name.getText() + ".json")) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(school.getCalendar(), writer);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                fileList.getItems().add(name.getText());
                name.clear();
                school.setCalendar(new Calendar());
            }
        });

        buttonLoad.setOnAction(event -> {
            Gson gson = new Gson();
            try {
                school.setCalendar(gson.fromJson(new FileReader("./saves/" + fileList.getValue() + ".json"), Calendar.class));
                fileList.setValue("Loaded");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        buttonDelete.setOnAction(event -> {
            String nameDeleted = fileList.getSelectionModel().getSelectedItem();
            File file = new File("./saves/" + fileList.getSelectionModel().getSelectedItem() + ".json");
            if (file.delete()) {
                fileList.getItems().remove(nameDeleted);
                fileList.setValue("<" + nameDeleted + " Deleted>");
            } else {
                fileList.setValue("Error");
            }
        });
    }
}