package schoolplanner.calendar;

import schoolplanner.calendar.lessons.Lesson;
import schoolplanner.calendar.lessons.subject.Subject;
import schoolplanner.util.TimeStamp;
import schoolplanner.calendar.lessons.npc.Group;
import schoolplanner.calendar.lessons.npc.Teacher;
import schoolplanner.calendar.lessons.rooms.ClassRoom;

import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {

    private ArrayList<Lesson> lessons;

    private HashMap<String, Group> groups;

    public Schedule() {
        this.lessons = new ArrayList<>();
        this.groups = new HashMap<>();
    }

    public Schedule(ArrayList<Lesson> lessons) {
        this.lessons = new ArrayList<>();
        this.groups = new HashMap<>();
        this.lessons = lessons;
    }

    /**
     * this method add's a lesson to the schedule, this lesson has a teacher, classRoom, subject, group, startTime and endTime.
     * @param teacher teacher to add to the lesson.
     * @param classRoom classroom to add to the lesson.
     * @param subject subject to add to the lesson.
     * @param group group to add to the lesson.
     * @param startTime startTime to add to the lesson.
     * @param endTime endTime to add to the lesson.
     * @throws IllegalArgumentException Exception if parameters have overlap with another lesson.
     */
    void addLesson(Teacher teacher, ClassRoom classRoom, Subject subject, Group group, TimeStamp startTime, TimeStamp endTime) throws IllegalArgumentException {
        if (!lessons.isEmpty()) {
            for (Lesson lesson : lessons) {
                if (((!startTime.isLaterThan(lesson.getEndTime())) && (startTime.isLaterThan(lesson.getStartTime()))) || ((!endTime.isLaterThan(lesson.getEndTime())) && (endTime.isLaterThan(lesson.getStartTime())))) {
                    if (teacher.getTeacherCode() == lesson.getTeacher().getTeacherCode()) {
                        throw new IllegalArgumentException(teacher.getName() + " is already occupied");
                    }
                    if (classRoom.getName().equals(lesson.getClassRoom().getName())) {
                        throw new IllegalArgumentException("Room: " + classRoom.getName() + " is already occupied");
                    }
                    if (lesson.getGroup().getGroupName().equals(group.getGroupName())) {
                        throw new IllegalArgumentException("Group: " + group.getGroupName() + " is already occupied");
                    }
                }
            }
        }
        if (classRoom.getCapacity() < group.getSize()) {
            throw new IllegalArgumentException("Classroom: " + classRoom.getName() + " is too small");
        }
        this.lessons.add(new Lesson(teacher, group, classRoom, subject, startTime, endTime));
        if (!this.groups.containsKey(group.getGroupName())) {
            this.groups.put(group.getGroupName(), group);
        }
    }

    /**
     * this method returns the last lesson of a group.
     * @param group Group to check for the last lesson.
     * @return Last lesson of the group.
     * @throws RuntimeException Exception of the schedule has no lessons.
     */
    public Lesson getLastLesson(String group) throws RuntimeException {
        if (lessons.isEmpty()) {
            throw new RuntimeException("The current schedule does not contain any lessons.");
        }
        Lesson leader = null;
        ArrayList<Lesson> groupLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getGroup().getGroupName().equals(group)) {
                groupLessons.add(lesson);
            }
        }
        for (Lesson lesson : groupLessons) {
            if (leader == null || lesson.getStartTime().isLaterThan(leader.getStartTime())) {
                leader = lesson;
            }
        }
        return leader;
    }

    /**
     * this method returns the last lesson of a specific teacher.
     * @param teacher Teacher to check for last lesson.
     * @return Last lesson for the teacher.
     */
    public Lesson getLastLessonTeacher(String teacher) throws RuntimeException {
        lessonCheck();
        Lesson leader = null;
        ArrayList<Lesson> groupLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getTeacher().getName().equals(teacher)) {
                groupLessons.add(lesson);
            }
        }
        for (Lesson lesson : groupLessons) {
            if (leader == null || lesson.getStartTime().isLaterThan(leader.getStartTime())) {
                leader = lesson;
            }
        }
        return leader;
    }

    /**
     * this method returns the first lesson of a group.
     * @param group Group to check for the last Lesson.
     * @return Last lesson of the Group.
     */
    public Lesson getFirstLesson(String group) {
        lessonCheck();
        Lesson leader = null;
        ArrayList<Lesson> groupLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getGroup().getGroupName().equals(group)) {
                groupLessons.add(lesson);
            }
        }
        for (Lesson lesson : groupLessons) {
            if (leader == null || !lesson.getStartTime().isLaterThan(leader.getStartTime())) {
                leader = lesson;
            }
        }
        return leader;
    }

    /**
     * this method returns a hashMap of all the groups with its names.
     * @return HashMap of all groups.
     */
    public HashMap<String, Group> getGroups() {
        return this.groups;
    }

    /**
     * this method returns a ArrayList of all the Lessons.
     * @return ArrayList of all lessons.
     */
    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    /**
     * Checks if the lesson is empty.
     * @throws RuntimeException Error if the lesson is empty.
     */
    private void lessonCheck() throws RuntimeException {
        if (lessons.isEmpty()) {
            throw new RuntimeException("The current schedule does not contain any lessons.");
        }
    }
}
