package schoolplanner.calendar.lessons.npc;

import schoolplanner.calendar.lessons.subject.Subject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Teacher {
    private String name;
    private int teacherCode;
    private Set<String> subjects;

    /**
     * This is 1 of the 2 constructors for the Teacher class.
     * It sets the name and teacherCode and also the HashMap of subjects this teacher can teach.
     * @param name the name of the teacher.
     * @param teacherCode the teacherCode of the teacher.
     * @param subject the subjects the teacher can teach.
     */
    public Teacher(String name, int teacherCode, HashMap<String, Subject> subject){
        this.name = name;
        this.teacherCode = teacherCode;
        this.subjects = new HashSet<>();
        this.subjects.addAll(subject.keySet());
    }

    /**
     * This is the other 1 of the 2 constructors for the Teacher class.
     * It sets the name and teacherCode and it also adds a subject to the HashMap of subjects the teacher can teach.
     * @param name the name of the teacher.
     * @param teacherCode the teacherCode of the teacher.
     * @param subject the subject the teacher can teach.
     */
    public Teacher(String name, int teacherCode, Subject subject){
        this.name = name;
        this.teacherCode = teacherCode;
        this.subjects = new HashSet<>();
        this.subjects.add(subject.getName());
    }

    /**
     * This method removes a subject from the HashMap of subjects the teacher van teach.
     * @param subject the subject which the user wants to remove.
     */
    public void removeSubject(Subject subject) throws IllegalArgumentException{
        if (!subjects.contains(subject.getName())) {
            throw new IllegalArgumentException("The given subject is not taught by this teacher.");
        }
        subjects.remove(subject.getName());
    }

    /**
     * This is a method that returns the subjects the teacher can teach.
     * @return HashMap of subjects that the teacher can teach.
     */
    public Set<String> getSubjects() {
        return subjects;
    }

    /**
     * This is a method that returns the name of the teacher.
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * This is a way to change the name of the teacher.
     * @param name the name which it will be changed to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This is a method that returns the teacherCode of the teacher.
     * @return Integer teacherCode
     */
    public int getTeacherCode() {
        return teacherCode;
    }
}
