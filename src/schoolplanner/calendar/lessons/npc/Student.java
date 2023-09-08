package schoolplanner.calendar.lessons.npc;

public class Student {
    private String name;
    private String groupName;
    private int studentNumber;

    /**
     * This is the constructor. It sets the name and studentNumber of this student.
     * @param name The name of this student.
     * @param studentNumber The studentNumber of this student.
     */
    public Student(String name, int studentNumber){
        this.name = name;
        this.studentNumber = studentNumber;
    }

    /**
     * This is a method that returns the name of the student.
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * This is a way to change the name of the student.
     * @param name the name which it will be changed to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This is a method that returns the studentNumber of the student.
     * @return Integer studentNumber
     */
    public int getStudentNumber() {
        return studentNumber;
    }

    /**
     * This is a method that returns the group of the student.
     * @return Group group
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * This is a way to change the group of the student.
     * @param group the group which it will be changed to.
     */
    public void setGroup(Group group) {
        this.groupName = group.getGroupName();
    }
}
