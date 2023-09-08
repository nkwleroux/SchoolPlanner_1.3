package schoolplanner.gui.startscreen;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import schoolplanner.School;
import schoolplanner.gui.settings.Setting;
import schoolplanner.gui.scheduleGUI.ScheduleViewer;
import schoolplanner.gui.contacts.ContactsList;
import schoolplanner.simulator.Simulator;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

/**
 * @author Nicholas Le Roux
 */

public class StartScreenC {

    @FXML
    private Label labelTitle;

    @FXML
    private Button buttonStartSim;

    @FXML
    private Button buttonSchedulePicker;

    @FXML
    private Button buttonSettings;

    @FXML
    private Button buttonContacts;

    private School school;

    public void setSchool(School school) {
        this.school = school;
    }


    /**
     * Method to set scene to ContactsList.
     */
    @FXML
    void onActionContacts(ActionEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ContactsList contacts = new ContactsList(primaryStage, school);
        primaryStage.setTitle("Contacts list");
        contacts.start();
    }

    /**
     * Method to set scene to ScheduleViewer.
     */
    @FXML
    void onActionSchedulePicker(ActionEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ScheduleViewer userInterface = new ScheduleViewer(primaryStage, school);
        primaryStage.setTitle("Schedule Viewer");
        userInterface.start();
    }

    /**
     * Method to set scene to Settings.
     */
    @FXML
    void onActionSettings(ActionEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Setting setting = new Setting(primaryStage, school);
        primaryStage.setTitle("Settings");
        setting.start();
    }

    /**
     * Method to set scene to Simulation.
     */
    @FXML
    void onActionStartSim(ActionEvent event) {

        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();

        Button backButton = new Button("Back");

        BorderPane startupPane = new BorderPane();

        Scene startupScene = new Scene(startupPane);

        ArrayList<String> schedules = new ArrayList<>();

        for(Date date: this.school.getCalendar().getSchedules().keySet()){
            schedules.add(date.toString());
        }

        Label title = new Label("Enter which day to simulate:");

        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(schedules));

        HBox HBox = new HBox(title, comboBox, backButton);

        startupPane.setCenter(HBox);

        primaryStage.setScene(startupScene);

        backButton.setOnAction((event2 -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(StartScreenC.class.getResource("startscreen.fxml"));
                Parent root = loader.load();
                StartScreenC startScreenC = loader.getController();
                startScreenC.setSchool(this.school);
                final Scene startScreen = new Scene(root);
                primaryStage.setMaximized(false);
                primaryStage.setTitle("School calendar Application");
                primaryStage.setScene(startScreen);
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        comboBox.setOnAction((event1) -> {

            Simulator simulator = new Simulator(this.school.getCalendar().getSchedules().get(Date.valueOf(comboBox.getValue().toString())),
                    backButton, primaryStage);

            primaryStage.setMaximized(true);

            backButton.setOnAction((event2 -> {
                try {
                    simulator.quit();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(StartScreenC.class.getResource("startscreen.fxml"));
                    Parent root = loader.load();
                    StartScreenC startScreenC = loader.getController();
                    startScreenC.setSchool(this.school);
                    final Scene startScreen = new Scene(root);
                    primaryStage.setMaximized(false);
                    primaryStage.setTitle("School calendar Application");
                    primaryStage.setScene(startScreen);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        });
    }
}


