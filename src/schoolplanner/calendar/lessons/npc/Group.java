package schoolplanner.calendar.lessons.npc;

import java.util.HashMap;

public class Group {
    private HashMap<Integer, Student> students;
    private String groupName;

    /**
     * Constructor for the Group class, sets the groupName and initializes the list of students.
     * @param groupName The groupName to set to this object.
     */
    public Group(String groupName) {
        this.students = new HashMap<>();
        this.groupName = groupName;
    }

    /**
     * Gets the students assigned to this object.
     * @return HashMap of all students using their studentNumber as a key.
     */
    public HashMap<Integer, Student> getStudents() {
        return students;
    }

    /**
     * Adds a student to this group object by adding it to it's hashMap.\
     * This method will also immediately set the group to the student as well.
     * @param student The student object to add to the HashMap.
     */
    public void addStudent(Student student) throws IllegalArgumentException {
        if (students.containsKey(student.getStudentNumber())) {
            throw new IllegalArgumentException("The given student is already in this group.");
        }
        students.put(student.getStudentNumber(), student);
        student.setGroup(this);
    }

    /**
     * Removed the student from this group object by removing it from the HashMap.
     * @param student The student object to remove from the HashMap.
     */
    public void removeStudent(Student student) throws IllegalArgumentException {
        if (!students.containsKey(student.getStudentNumber())) {
            throw new IllegalArgumentException("The given student is not in this group.");
        }
        students.remove(student.getStudentNumber());
    }

    /**
     * Gets the groupName assigned to this group.
     * @return The groupName assigned to the group.
     */
    public String getGroupName() {

        return groupName;
    }

    /**
     * Sets the groupName given in the parameter.
     * @param groupName The new name of the group.
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
        for (Integer i: students.keySet()) {
            students.get(i).setGroup(this);
        }
    }

    /**
     * Gets the size of the room, used to conflict checking.
     * @return The size of the room.
     */
    public int getSize(){
        return students.size();
    }
}
