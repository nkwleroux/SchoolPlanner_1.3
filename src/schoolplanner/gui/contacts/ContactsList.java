package schoolplanner.gui.contacts;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import schoolplanner.School;
import schoolplanner.gui.startscreen.StartScreenC;
import schoolplanner.calendar.lessons.subject.Subject;
import schoolplanner.calendar.lessons.npc.Group;
import schoolplanner.calendar.lessons.npc.Student;
import schoolplanner.calendar.lessons.npc.Teacher;
import schoolplanner.util.SimplePropertyConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Nicholas Le Roux
 */

public class ContactsList {

    private final int STARTSCREENWIDTH = 625;
    private final int STARTSCREENHEIGHT = 428;
    private TableView<SimplePropertyConverter> tableView;
    private CustomTextField textFieldSortName;
    private CustomTextField textFieldSortID;
    private CustomTextField textFieldSortRole;
    private CustomTextField textFieldSortSubject;
    private CustomTextField textFieldSortGroup;
    private ToolBar toolBarTop;
    private ToolBar toolBarBot;
    private Button buttonBack;
    private Stage stage;
    private ComboBox<String> comboBoxFilter;
    private ArrayList<TableColumn> tableColumns;
    private School school;
    private String sortType;

    //Default list, is replaced if the calendar object list is not empty.
    private ObservableList<SimplePropertyConverter> tableList = FXCollections.observableArrayList();

    public ContactsList(Stage window, School school) {
        stage = window;
        tableView = new TableView<>();
        textFieldSortName = new CustomTextField();
        textFieldSortID = new CustomTextField();
        textFieldSortRole = new CustomTextField();
        textFieldSortSubject = new CustomTextField();
        textFieldSortGroup = new CustomTextField();
        toolBarTop = new ToolBar();
        toolBarBot = new ToolBar();
        buttonBack = new Button("Back");
        comboBoxFilter = new ComboBox<>();
        tableColumns = new ArrayList<>();
        this.school = school;
        sortType = "All";
    }

    /**
     * Start function that can be called in another class. Used to startAnimationTimer the GUI.
     */
    public void start() {

        if (setTableList().isEmpty()) {
            tableList = setStandardTableList();
        } else {
            tableList = setTableList();
        }

        //Sets the columns in the TableView.
        setTableView(getDefaultView());

        //Sets the stage.
        setStage();

        //Checks if the button 'Back' has been pressed and changes the scene to the startAnimationTimer screen.
        setButtonBack();

        //Changes TableView according to chosen option in ComboBox
        setComboBoxFilter();

        //Changes the data in the TableView according to the information typed in the TextField.
        setTextFieldSortName();
        setTextFieldSortID();
        setTextFieldSortRole();
        setTextFieldSortGroup();
        setTextFieldSortSubject();
        setTextFieldSortSubject();

    }

    /**
     * Sets the stage of the screen.
     */
    private void setStage() {

        //Layout manager used in the stage.
        BorderPane borderPane = new BorderPane();

        //TextField name
        textFieldSortName.setAlignment(Pos.CENTER);
        textFieldSortName.setPromptText("Name");
        textFieldSortName.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        TextFields.bindAutoCompletion(textFieldSortName, search("Names"));
        textFieldSortName.setRight(new FontAwesomeIconView(FontAwesomeIcon.SEARCH));

        //TextField ID
        textFieldSortID.setAlignment(Pos.CENTER);
        textFieldSortID.setPromptText("ID number");
        textFieldSortID.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        textFieldSortID.setRight(new FontAwesomeIconView(FontAwesomeIcon.SEARCH));

        //TextField subject
        textFieldSortSubject.setAlignment(Pos.CENTER);
        textFieldSortSubject.setPromptText("Subject");
        textFieldSortSubject.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        TextFields.bindAutoCompletion(textFieldSortSubject, search("Subjects"));
        textFieldSortSubject.setRight(new FontAwesomeIconView(FontAwesomeIcon.SEARCH));

        //TextField role
        textFieldSortRole.setAlignment(Pos.CENTER);
        textFieldSortRole.setPromptText("Role");
        textFieldSortRole.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        TextFields.bindAutoCompletion(textFieldSortRole, search("Roles"));
        textFieldSortRole.setRight(new FontAwesomeIconView(FontAwesomeIcon.SEARCH));

        //TextField group
        textFieldSortGroup.setAlignment(Pos.CENTER);
        textFieldSortGroup.setPromptText("Group");
        textFieldSortGroup.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        TextFields.bindAutoCompletion(textFieldSortGroup, search("Groups"));
        textFieldSortGroup.setRight(new FontAwesomeIconView(FontAwesomeIcon.SEARCH));

        //ComboBox
        comboBoxFilter.getItems().addAll("All", "Students", "Teachers");
        comboBoxFilter.getSelectionModel().selectFirst();

        //Toolbar top
        toolBarTop.getItems().addAll(buttonBack, comboBoxFilter,
                textFieldSortID, textFieldSortName);
        toolBarTop.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        //Toolbar bottom
        toolBarBot.getItems().addAll(textFieldSortRole);

        //BorderPane
        borderPane.setTop(toolBarTop);
        borderPane.setCenter(tableView);
        borderPane.setBottom(toolBarBot);

        //Makes new scene
        Scene scene = new Scene(borderPane, 1000, 501);

        //Sets minimum dimensions
        borderPane.setMinSize(480, 130);
        stage.setMinWidth(500);
        stage.setMinHeight(168);

        //Sets scene and displays it on the stage.
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Reads the objects in calendar and adds it to the TableView.
     *
     * @return A list that will be shown in the TableView
     */
    private ObservableList<SimplePropertyConverter> setTableList() {
        ObservableList<SimplePropertyConverter> list = FXCollections.observableArrayList();

        if (!school.getCalendar().getStudents().isEmpty()) {
            for (Integer i : school.getCalendar().getStudents().keySet()) {
                list.add(new SimplePropertyConverter(school.getCalendar().getStudents().get(i)));
            }
        }
        if (!school.getCalendar().getTeachers().isEmpty()) {
            for (Integer i : school.getCalendar().getTeachers().keySet()) {
                list.add(new SimplePropertyConverter(school.getCalendar().getTeachers().get(i)));
            }
        }
        return list;
    }

    /**
     * A search function.
     * TextField search term auto populates the search bar with words that include the term.
     *
     * @param sortType The type of search term used to search through the data on the TableView.
     * @return The filtered list of the sort type.
     */
    private List<String> search(String sortType) {
        List<String> list = new ArrayList<>();
        switch (sortType) {
            case "Names":
                for (SimplePropertyConverter name : tableList) {
                    list.add(name.getName());
                }
                break;
            case "Roles":
                list.add("Student");
                list.add("Teacher");
                break;
            case "Subjects":
                if (school.getCalendar().getSubjects().isEmpty()) {
                    list.add("Tutor");
                    list.add("Senior");
                } else {
                    for (String i : school.getCalendar().getSubjects().keySet()) {
                        list.add(school.getCalendar().getSubjects().get(i).getName());
                    }
                }
                break;
            case "Groups":
                if (school.getCalendar().getGroups().isEmpty()) {
                    list.add("A2");
                } else {
                    for (String i : school.getCalendar().getGroups().keySet()) {
                        list.add(school.getCalendar().getGroups().get(i).getGroupName());
                    }
                }
                break;
            default:
                break;
        }
        return list;
    }

    /**
     * Sorts the list shown in the table according to the chosen role the user sorted them in.
     *
     * @param position The position of the person in the school such as student or teacher.
     * @return The filtered list. Can return a list of only students or teachers.
     */
    private ObservableList<SimplePropertyConverter> sortedList(String position) {
        ObservableList<SimplePropertyConverter> list = FXCollections.observableArrayList();

        for (SimplePropertyConverter index : tableList) {
            if (index.getPosition().equals(position.substring(0, position.length() - 1))) {
                list.add(index);
            }
        }
        return list;
    }

//====================================================================================================================//

    //Sets the TableColumns.

    /**
     * Adds the tableColumns to the TableView
     *
     * @param tableColumns The list of TableColumns from the methods get...DefaultView.
     */
    private void setTableView(ArrayList<TableColumn> tableColumns) {
        tableView.getColumns().clear();
        for (TableColumn<SimplePropertyConverter, String> columnIndex : tableColumns) {
            tableView.getColumns().add(columnIndex);
        }
    }

    /**
     * Sets the initial data type for the TableView.
     *
     * @return A list of columns for both students and teachers.
     */
    private ArrayList<TableColumn> getDefaultView() {
        tableColumns.clear();
        TableColumn<SimplePropertyConverter, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<SimplePropertyConverter, Integer> ID = new TableColumn<>("ID");
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<SimplePropertyConverter, String> group_subject = new TableColumn<>("Role");
        group_subject.setCellValueFactory(new PropertyValueFactory<>("position"));
        Collections.addAll(tableColumns, name, ID, group_subject);

        for (int i = 0; i < tableColumns.size(); i++) {
            if (i == tableColumns.size() - 1) {
                tableColumns.get(i).prefWidthProperty().bind(tableView.widthProperty().divide(
                        tableColumns.size() - 1).subtract(82));
            } else if (!tableColumns.get(i).getText().equals("ID")) {
                tableColumns.get(i).prefWidthProperty().bind(tableView.widthProperty().divide(
                        tableColumns.size() - 1));
            } else {
                tableColumns.get(i).setMinWidth(80);
            }
        }

        tableView.setItems(tableList);

        return tableColumns;
    }

    /**
     * Sets the initial data for the student TableView.
     *
     * @return A list of TableColumns for students.
     */
    private ArrayList<TableColumn> getStudentDefaultView() {
        tableColumns.clear();
        TableColumn<SimplePropertyConverter, String> name = new TableColumn<>("Student Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<SimplePropertyConverter, Integer> ID = new TableColumn<>("Student ID");
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<SimplePropertyConverter, String> group = new TableColumn<>("Group");
        group.setCellValueFactory(new PropertyValueFactory<>("studentGroup"));
        Collections.addAll(tableColumns, name, ID, group);

        for (int i = 0; i < tableColumns.size(); i++) {
            if (i == tableColumns.size() - 1) {
                tableColumns.get(i).prefWidthProperty().bind(tableView.widthProperty().divide(
                        tableColumns.size() - 1).subtract(82));
            } else if (!tableColumns.get(i).getText().equals("Student ID")) {
                tableColumns.get(i).prefWidthProperty().bind(tableView.widthProperty().divide(
                        tableColumns.size() - 1));
            }
        }

        return tableColumns;
    }

    /**
     * Sets the initial data for the teacher TableView.
     *
     * @return A list of TableColumns for teachers.
     */
    private ArrayList<TableColumn> getTeacherDefaultView() {
        tableColumns.clear();
        TableColumn<SimplePropertyConverter, String> name = new TableColumn<>("Teacher Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<SimplePropertyConverter, Integer> ID = new TableColumn<>("Teacher ID");
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<SimplePropertyConverter, String> subject = new TableColumn<>("Subject(s)");
        subject.setCellValueFactory(new PropertyValueFactory<>("teacherSubjects"));
        Collections.addAll(tableColumns, name, ID, subject);

        for (int i = 0; i < tableColumns.size(); i++) {
            if (i == tableColumns.size() - 1) {
                tableColumns.get(i).prefWidthProperty().bind(tableView.widthProperty().divide(
                        tableColumns.size() - 1).subtract(82));
            } else if (!tableColumns.get(i).getText().equals("Teacher ID")) {
                tableColumns.get(i).prefWidthProperty().bind(tableView.widthProperty().divide(
                        tableColumns.size() - 1));
            }
        }

        return tableColumns;
    }

//====================================================================================================================//

    //Default data for the TableView(Not important but can be used).

    /**
     * Sets the default list if there is no objects added such as a student or teacher to calendar.
     *
     * @return A list that will be shown on the TableView.
     */
    private ObservableList<SimplePropertyConverter> setStandardTableList() {
        ObservableList<SimplePropertyConverter> list = FXCollections.observableArrayList();
        Group group = new Group("A2");

        SimplePropertyConverter a = setDefaultStudent(new Student(("Nicholas Le Roux"), 100), group);
        SimplePropertyConverter b = setDefaultStudent(new Student("Mick van der Werf", 101), group);
        SimplePropertyConverter c = setDefaultStudent(new Student("Wouter Leijs", 102), group);
        SimplePropertyConverter d = setDefaultStudent(new Student("Martijn van Zelst", 103), group);
        SimplePropertyConverter e = setDefaultStudent(new Student("Stijn Lemm", 104), group);
        SimplePropertyConverter f = setDefaultStudent(new Student("Tom Spekman", 105), group);
        SimplePropertyConverter Andries = new SimplePropertyConverter(new Teacher("Andries van Dongen", 1,
                new Subject("Tutor")));
        SimplePropertyConverter Johan = new SimplePropertyConverter(new Teacher("Johan Talboom", 2,
                new Subject("Senior")));
        list.addAll(a, b, c, d, e, f, Andries, Johan);

        return list;
    }

    /**
     * Converts a student to the type needed for the TableView.
     *
     * @param student The given student with the name and student number.
     * @param group   The given group that is associated with the student.
     * @return The type needed for the object to be placed in the TableView.
     */
    private SimplePropertyConverter setDefaultStudent(Student student, Group group) {
        student.setGroup(group);
        return new SimplePropertyConverter(student);
    }

//====================================================================================================================//

    //UI inputs (Buttons, TextField, TableColumns...etc.

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
     * ComboBox 'Filter'
     * Switch case which allows the user to choose between different ways of sorting
     * Choices are: All, Students, Teachers
     */
    @SuppressWarnings("Unchecked Call")
    private void setComboBoxFilter() {
        comboBoxFilter.valueProperty().addListener((observable, oldValue, newValue) -> {

            sortType = observable.getValue();
            switch (sortType) {
                case "All":
                    setTableView(getDefaultView());
                    toolBarBot.getItems().removeAll(toolBarBot.getItems());
                    toolBarBot.getItems().addAll(textFieldSortRole);
                    break;

                case "Students":
                    setTableView(getStudentDefaultView());
                    tableView.setItems(sortedList(sortType));
                    toolBarBot.getItems().removeAll(toolBarBot.getItems());
                    toolBarBot.getItems().addAll(textFieldSortGroup);
                    break;

                case "Teachers":
                    setTableView(getTeacherDefaultView());
                    tableView.setItems(sortedList(sortType));
                    toolBarBot.getItems().removeAll(toolBarBot.getItems());
                    toolBarBot.getItems().addAll(textFieldSortSubject);
                    break;

                default:
                    break;
            }
        });
    }

    /**
     * Sorts the list by the chosen ComboBox and search term.
     */
    private void sortInputType() {
        if (sortType.equals("Students")) {
            tableView.setItems(sortedList(sortType));
        } else if (sortType.equals("Teachers")) {
            tableView.setItems(sortedList(sortType));
        } else {
            tableView.setItems(tableList);
        }
    }

    /**
     * Filters list according to input in TextField.
     */
    private void setTextFieldSortName() {
        textFieldSortName.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<SimplePropertyConverter> list = FXCollections.observableArrayList();
            for (SimplePropertyConverter s : tableList) {
                if (s.getName().equalsIgnoreCase(newValue) ||
                        s.getName().contains(newValue.toLowerCase()) ||
                        s.getName().contains(newValue.toUpperCase())) {
                    list.add(s);
                    tableView.setItems(list);
                } else if (newValue.isEmpty()) {
                    sortInputType();
                }
            }
        });
    }

    /**
     * Filters list according to input in TextField.
     */
    private void setTextFieldSortID() {
        textFieldSortID.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<SimplePropertyConverter> list = FXCollections.observableArrayList();
            for (SimplePropertyConverter s : tableList) {
                if (newValue.isEmpty()) {
                    sortInputType();
                } else if (s.getID() == Integer.valueOf(newValue)) {
                    list.add(s);
                    tableView.setItems(list);
                }
            }
        });
    }

    /**
     * Filters list according to input in TextField.
     */
    private void setTextFieldSortRole() {
        textFieldSortRole.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<SimplePropertyConverter> list = FXCollections.observableArrayList();
            for (SimplePropertyConverter s : tableList) {
                if (s.getPosition().equalsIgnoreCase(newValue) ||
                        s.getPosition().contains(newValue.toLowerCase()) ||
                        s.getPosition().contains(newValue.toUpperCase())) {
                    list.add(s);
                    tableView.setItems(list);
                } else if (newValue.isEmpty()) {
                    sortInputType();
                }
            }

        });
    }

    /**
     * Filters list according to input in TextField.
     */
    private void setTextFieldSortGroup() {
        textFieldSortGroup.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<SimplePropertyConverter> list = FXCollections.observableArrayList();
                for (int i = 0; i < tableList.size(); i++) {
                    if (tableList.get(i).getStudentGroup().equalsIgnoreCase(newValue) ||
                            tableList.get(i).getStudentGroup().contains(newValue.toLowerCase()) ||
                            tableList.get(i).getStudentGroup().contains(newValue.toUpperCase())) {
                        list.add(tableList.get(i));
                        tableView.setItems(list);
                    } else if (newValue.isEmpty()) {
                        sortInputType();
                    }
                }
            }
        });
    }

    /**
     * Filters list according to input in TextField.
     */
    private void setTextFieldSortSubject() {
        textFieldSortSubject.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<SimplePropertyConverter> list = FXCollections.observableArrayList();
                for (int i = 0; i < tableList.size(); i++) {

                    if (tableList.get(i).getTeacherSubjects().equalsIgnoreCase(newValue) ||
                            tableList.get(i).getTeacherSubjects().contains(newValue)) {
                        list.add(tableList.get(i));
                        tableView.setItems(list);
                    } else if (newValue.isEmpty()) {
                        sortInputType();
                    }
                }
            }
        });
    }
}
