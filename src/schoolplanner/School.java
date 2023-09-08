package schoolplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import schoolplanner.calendar.Calendar;
import schoolplanner.gui.startscreen.StartScreenC;
import schoolplanner.calendar.lessons.subject.Subject;
import schoolplanner.calendar.lessons.npc.Group;
import schoolplanner.calendar.lessons.npc.Student;
import schoolplanner.calendar.lessons.npc.Teacher;
import schoolplanner.calendar.lessons.rooms.ClassRoom;
import schoolplanner.util.TimeStamp;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;

public class School extends Application {
private Calendar calendar = new Calendar();

    public School() {
    }

    // Do not delete, used to test simulation.
    private void testData(){

        HashMap<String,Subject> subjectHashMap = new HashMap<>();

        Date date = Date.valueOf(LocalDate.now());
        ClassRoom classRoom = new ClassRoom("FightingRoom 1", 8);
        ClassRoom classRoom1 = new ClassRoom("ClassRoom 4", 8);
        Group a = new Group("A");
        Group b = new Group("B");
        TimeStamp startTime = new TimeStamp(8, 50);
        TimeStamp endTime = new TimeStamp(9, 20);

        for (int i = 16; i < 32; i++) {
            Student student = new Student("Student " + i,i);
            calendar.addStudent(student);
            if(i < 24) {
                a.addStudent(student);
            } else{
                b.addStudent(student);
            }
        }

        calendar.addSchedule(date);
        calendar.addClassRoom(classRoom);
        calendar.addClassRoom(classRoom1);
        calendar.addGroup(a);
        calendar.addGroup(b);

        ClassRoom classRoom2 = new ClassRoom("FightingRoom 2", 200);
        calendar.addClassRoom(classRoom2);

        Subject fire = new Subject("Fire training");
        Subject water = new Subject("Water training");

        calendar.addSubject(fire);
        calendar.addSubject(water);
        calendar.addTeacher(new Teacher("Prof. Oak", 5, fire));
        calendar.addTeacher(new Teacher("Prof. Sycamore", 6, water));
        calendar.addClassRoom(new ClassRoom("FightingRoom 3", 8));
        calendar.addClassRoom(new ClassRoom("ClassRoom 6" , 8));

       calendar.addLesson(date, 5, classRoom2.getName() , "Fire training", "B", new TimeStamp(9, 40), new TimeStamp(10, 20));
        calendar.addLesson(date, 6, classRoom1.getName() , "Water training", "A", new TimeStamp(9, 40), new TimeStamp(10, 20));
        calendar.addLesson(date, 5, "ClassRoom 6" , "Fire training", "B", new TimeStamp(10, 35), new TimeStamp(11, 40));
        calendar.addLesson(date, 6, "FightingRoom 3" , "Water training", "A", new TimeStamp(10, 35), new TimeStamp(11, 40));
        calendar.addLesson(date, 5, classRoom.getName() , "Fire training", "B", new TimeStamp(12, 50), new TimeStamp(13, 40));
        calendar.addLesson(date, 6, classRoom2.getName() , "Water training", "A", new TimeStamp(12, 50), new TimeStamp(13, 40));
        calendar.addLesson(date, 5, classRoom1.getName() , "Fire training", "B", new TimeStamp(14, 0), new TimeStamp(15, 40));
        calendar.addLesson(date, 6, "FightingRoom 3" , "Water training", "A", new TimeStamp(14, 0), new TimeStamp(15, 40));

    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartScreenC.class.getResource("startscreen.fxml"));
        Parent root = loader.load();
        StartScreenC startScreenC = loader.getController();
        startScreenC.setSchool(this);
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(480);
        Scene scene = new Scene(root);
        primaryStage.setTitle("School calendar Application");
        primaryStage.setScene(scene);
        primaryStage.show();

        testData();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
