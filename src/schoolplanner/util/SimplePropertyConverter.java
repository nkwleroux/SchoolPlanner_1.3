package schoolplanner.util;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import schoolplanner.calendar.lessons.npc.Student;
import schoolplanner.calendar.lessons.npc.Teacher;
import schoolplanner.calendar.lessons.rooms.ClassRoom;
import schoolplanner.calendar.lessons.rooms.StaffRoom;

/**
 * @author Nicholas Le roux
 */

public class SimplePropertyConverter {

    //students and teachers
    private SimpleStringProperty studentGroup;
    private SimpleStringProperty teacherSubjects;
    private SimpleStringProperty name;
    private SimpleIntegerProperty ID;
    private SimpleStringProperty position;

    //rooms
    private SimpleStringProperty roomName;
    private SimpleStringProperty roomType;
    private ClassRoom classRoom;
    private StaffRoom staffRoom;

    /**
     * A constructor that changes the name, ID and group from the object student into a
     * SimpleProperty so that it can be added to the TableView.
     *
     * @param student To get the name, ID and group from the created student
     */
    public SimplePropertyConverter(Student student){
        name = new SimpleStringProperty(student.getName());
        ID = new SimpleIntegerProperty(student.getStudentNumber());

        try {
            studentGroup = new SimpleStringProperty(student.getGroupName());
            }catch(NullPointerException e){
            studentGroup = new SimpleStringProperty("A2");
        }

        position = new SimpleStringProperty("Student");
    }


    /**
     * A constructor that changes the name, ID and group from the object teacher into a
     * SimpleProperty so that it can be added to the TableView.
     *
     * @param teacher To get the name, ID and group from the created teacher
     */
    public SimplePropertyConverter(Teacher teacher){
        name = new SimpleStringProperty(teacher.getName());
        ID = new SimpleIntegerProperty(teacher.getTeacherCode());

        String subjectList = "";
        if (teacher.getSubjects().size() != 0) {
            for (String key : teacher.getSubjects()) {
                subjectList += key + ", ";
            }
            subjectList = subjectList.substring(0, subjectList.length() - 2);
        }else {
            subjectList = "Doesn't teach a subject";
        }

        teacherSubjects = new SimpleStringProperty(subjectList);

        position = new SimpleStringProperty("Teacher");
    }

    public SimplePropertyConverter(String classRoomName, ClassRoom classRoom, String type) {
        this.roomName = new SimpleStringProperty(classRoomName);
        this.roomType = new SimpleStringProperty(type);
        this.classRoom = classRoom;
    }

    public SimplePropertyConverter(String staffRoomName, StaffRoom staffRoom, String type) {
        this.roomName = new SimpleStringProperty(staffRoomName);
        this.roomType = new SimpleStringProperty(type);
        this.staffRoom = staffRoom;
    }

    /**
     * Getter for the student group.
     *
     * @return student group of the String type.
     */
    public String getStudentGroup() {
        return studentGroup.get();
    }

    /**
     * Getter for the teacher subjects.
     *
     * @return subjects of the teacher of the String type.
     */
    public String getTeacherSubjects() {
        return teacherSubjects.get();
    }

    /**
     * Getter for the name of both students and teachers.
     *
     * @return names of students and teachers of the String type.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Getter for the ID of both students and teachers.
     *
     * @return ID of both the students and the teachers of the Integer type.
     */
    public int getID() {
        return ID.get();
    }

    /**
     * Getter for the role of both students and teachers.
     *
     * @return Role of both the students and the teachers of the String type.
     */
    public String getPosition() {
        return position.get();
    }

    /**
     * Getter for the Room name.
     *
     * @return Name of the Room of the String type.
     */
    public String getRoomName() {
        return roomName.get();
    }

    /**
     * Getter for the Room type.
     *
     * @return Type of the Room of the String type.
     */
    public String getRoomType() {
        return roomType.get();
    }

    /**
     * Getter for the classRoom.
     *
     * @return ClassRoom of the ClassRoom type.
     */
    public ClassRoom getClassRoom() {
        return classRoom;
    }

    /**
     * Getter for the staffRoom.
     *
     * @return StaffRoom of the StaffRoom type.
     */
    public StaffRoom getStaffRoom() {
        return staffRoom;
    }
}