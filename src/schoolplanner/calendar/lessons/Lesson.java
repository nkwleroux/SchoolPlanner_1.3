package schoolplanner.calendar.lessons;

import schoolplanner.calendar.lessons.npc.Group;
import schoolplanner.calendar.lessons.npc.Teacher;
import schoolplanner.calendar.lessons.rooms.ClassRoom;
import schoolplanner.calendar.lessons.rooms.Room;
import schoolplanner.calendar.lessons.subject.Subject;
import schoolplanner.util.TimeStamp;

public class Lesson {
    private Teacher teacher;
    private Group group;
    private ClassRoom classRoom;
    private Subject subject;
    private TimeStamp startTime;
    private TimeStamp endTime;

    /**
     * This is the constructor. It sets the teacher, classRoom, subject, groups, startTime and endTime of this object.
     * @param teacher The teacher that teaches this lesson.
     * @param group The group that follows this lesson.
     * @param classRoom The classRoom in which this lesson is being given.
     * @param subject The subject of this lesson.
     * @param startTime The time when the lesson starts.
     * @param endTime The time when the lesson ends.
     */
    public Lesson(Teacher teacher, Group group, ClassRoom classRoom, Subject subject, TimeStamp startTime, TimeStamp endTime) {
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.subject = subject;
        this.group = group;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * This is a method that returns the teacher of the lesson.
     * @return Teacher teacher
     */
    public Teacher getTeacher() {
        return teacher;
    }

    /**
     * This is a way to change the teacher of the lesson.
     * @param teacher the teacher which it will be changed to.
     */
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    /**
     * This is a method that returns the groups of the lesson.
     * @return String group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * This method adds a group to the HashMap of groups who follow this lesson.
     * @param group The group which the user wants to add.
     */
    public void setGroup(Group group) throws IllegalArgumentException{
        this.group = group;
    }

    /**
     * This is a method that returns the teacher of the lesson.
     * @return Room classRoom
     */
    public Room getClassRoom() {
        return classRoom;
    }

    /**
     * This is a method that returns the subject of the lesson.
     * @return Subject subject
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * This is a way to change the subject of the lesson.
     * @param subject the subject which it will be changed to.
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * This is a method that returns the startTime of the lesson.
     * @return TimeStamp startTime
     */
    public TimeStamp getStartTime() {
        return startTime;
    }

    /**
     * This is a method that returns the endTime of the lesson.
     * @return TimeStamp endTime
     */
    public TimeStamp getEndTime() {
        return endTime;
    }
}
