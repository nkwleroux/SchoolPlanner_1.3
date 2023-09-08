package schoolplanner.calendar.lessons.subject;

import schoolplanner.calendar.lessons.npc.Teacher;

import java.util.HashMap;

public class Subject {
    private String name;
    private HashMap<Integer, Teacher> teachers;

    /**
     * This is the constructor. It sets the name of this object.
     * @param name This is the name of the subject. (Example: "Math")
     */
    public Subject(String name){
        this.name = name;
        teachers = new HashMap<>();
    }

    /**
     * This method adds a Teacher to the HashMap Teachers.
     * We made it so that when you add a subject to a lesson you can only add a Teacher who can teach that subject.
     * @param teacher This is the Object teacher which will be added to the HashMap(teachers).
     */
    public void addTeacher(Teacher teacher) throws IllegalArgumentException{
        if (teachers.containsKey(teacher.getTeacherCode())) {
            throw new IllegalArgumentException("The given teacher already teaches this subject.");
        }
        this.teachers.put(teacher.getTeacherCode(), teacher);
    }

    /**
     * This method removes a teacher from the HashMap.
     * @param teacher This is the object teacher which will be removed from the HashMap(teachers).
     */
    public void removeTeacher(Teacher teacher) throws IllegalArgumentException{
        if (!teachers.containsKey(teacher.getTeacherCode())) {
            throw new IllegalArgumentException("The given teacher does not teach this subject.");
        }
        this.teachers.remove(teacher.getTeacherCode());
        teacher.removeSubject(this);
    }

    /**
     * This method return's the HashMap with all the teachers in it that can teach this subject.
     * @return HashMap<teacherCode(Integer), Teacher>
     */
    public HashMap<Integer, Teacher> getTeachers() {
        return teachers;
    }

    /**
     * This is a method that returns the name of the subject.
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * This is a way to change the name of the subject.
     * @param name the name which it will be changed to.
     */
    public void setName(String name) {
        this.name = name;
    }
}
