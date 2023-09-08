package schoolplanner.calendar;

import javafx.scene.control.Alert;
import schoolplanner.calendar.lessons.rooms.StaffRoom;
import schoolplanner.calendar.lessons.subject.Subject;
import schoolplanner.util.AlertHandler;
import schoolplanner.calendar.lessons.npc.Group;
import schoolplanner.calendar.lessons.npc.Student;
import schoolplanner.calendar.lessons.npc.Teacher;
import schoolplanner.calendar.lessons.rooms.ClassRoom;
import schoolplanner.util.TimeStamp;
import schoolplanner.util.filter.FilterController;

import java.sql.Date;
import java.util.HashMap;

public class Calendar {
    private HashMap<Integer, Student> students;
    private HashMap<Integer, Teacher> teachers;
    private HashMap<String, Subject> subjects;
    private HashMap<String, Group> groups;
    private HashMap<String, ClassRoom> classRooms;
    private HashMap<String, StaffRoom> staffRooms;
    private HashMap<Date, Schedule> schedules;
    private FilterController filterController;

    public Calendar() {
        this.subjects = new HashMap<>();
        this.students = new HashMap<>();
        this.groups = new HashMap<>();
        this.groups.put("No group", new Group("No group"));
        this.classRooms = new HashMap<>();
        this.staffRooms = new HashMap<>();
        this.schedules = new HashMap<>();
        this.teachers = new HashMap<>();
        this.filterController = new FilterController();
    }

    /**
     * Function to filter a lessons using given parameters.
     * @param date The date of the lessons to filter.
     * @param type The type of data to filter in the lessons.
     * @param criteria The criteria with which to filter the type of data.
     * @return The filtered lessons, or null if there was an error.
     */
    public Schedule getFilteredSchedule(Date date, String type, String criteria) {
        Schedule filteredSchedule = new Schedule();
        if (!schedules.containsKey(date)) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Filter Exception",
                    "An error occurred while filtering the Schedule",
                    "There exists no lessons on the given date.");
        }
        try {
            filteredSchedule = filterController.getFilteredSchedule(schedules.get(date),type,criteria);
        } catch (Exception e){
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Filter Exception",
                    "An error occurred while filtering the Schedule",
                    e.toString());
        }
        return filteredSchedule;
    }

    /**
     * This method makes a new Schedule and adds it to the HashMap with all the lessons's.
     * It also has a error check to see if the date doesn't already have a lessons.
     * We added this check to make sure there wont be 2 different lessons's on the same date.
     * @param date the wanted date for the new lessons.
     */
    public void addSchedule(Date date) {
        if (this.schedules.containsKey(date)) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Schedule Exception",
                    "An error occurred while creating a new Schedule",
                    "There is already a lessons for the given date: " + date.toString());
        }
        this.schedules.put(date, new Schedule());
    }

    /**
     * This method removes a Schedule through the given date in the parameters.
     * It also has a error check to see if the date doesn't have a lessons.
     * We added this check to make sure that the user knows that there is no lessons for that given date.
     * @param date the date for which lessons that the user wants to remove.
     */
    public void removeSchedule(Date date) {
        if (!this.schedules.containsKey(date)) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Schedule Exception",
                    "An error occurred while removing a Schedule",
                    "There is no lessons for the given date: " + date.toString());
        }
        this.schedules.remove(date);
    }

    /**
     * This method returns a HashMap with all the Subject's in it.
     * The Key of this HashMap is the name of the Subject in String.
     * @return subjects in the calendar.
     */
    public HashMap<String, Subject> getSubjects() {
        return subjects;
    }

    /**
     * This adds a new Subject to the HashMap full of subject's.
     * This method also has a error check. It checks if the HashMap already contains this subject you want to add.
     * @param subject The subject which wants to be added to the HashMap.
     */
    public void addSubject(Subject subject) {
        if (this.subjects.containsKey(subject.getName())) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Subject Exception",
                    "An error occurred while creating a new Subject",
                    "The given subject already exist.");
        }
        this.subjects.put(subject.getName(), subject);
    }

    /**
     * The method removeSubject removes a subject from the HashMap subjects.
     * This method also has a error check. It checks if the HashMap contains this subject.
     * If it does not contain this subject it cant be removed and it shows a alert
     * @param subject the subject the user wants to remove.
     */
    public void removeSubject(Subject subject) {
        if (!this.subjects.containsKey(subject.getName())) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Subject Exception",
                    "An error occurred while removing a Subject",
                    "The given Subject does not exist.");
        }
        this.subjects.remove(subject.getName());
    }

    /**
     * This method return a HashMap which contains all the Students.
     * The key of this HashMap is the StudentNumber of the Student.
     * @return students in the calendar.
     */
    public HashMap<Integer, Student> getStudents() {
        return students;
    }

    /**
     * This method adds a student to the HashMap.
     * This method also has a error check. It checks if the HashMap already contains this student you want to add.
     * @param student the student the user wants to add.
     */
    public void addStudent(Student student) {
        if (this.students.containsKey(student.getStudentNumber())) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Student Exception",
                    "An error occurred while creating a new Student",
                    "The given Student already exist.");
        }
        this.students.put(student.getStudentNumber(), student);
    }

    /**
     * This method removes a student from the HashMap.
     * This method also has a error check. It checks if the HashMap contains this student you want to remove.
     * @param student the student the user wants to remove.
     */
    public void removeStudent(Student student) {
        if (!this.students.containsKey(student.getStudentNumber())) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Student Exception",
                    "An error occurred while removing a Student",
                    "The given Student does not exist.");
        }
        for (String group: groups.keySet()) {
            if (groups.get(group).getStudents().containsKey(student.getStudentNumber())) {
                groups.get(group).removeStudent(student);
            }
        }
        this.students.remove(student.getStudentNumber());
    }

    /**
     * This method return a HashMap with teacher's.
     * The Key of this HashMap is a TeacherCode.
     * @return teachers in the calendar.
     */
    public HashMap<Integer, Teacher> getTeachers() {
        return teachers;
    }

    /**
     * This method adds a teacher to the HashMap.
     * This method also has a error check. It checks if the HashMap already contains this teacher you want to add.
     * @param teacher the teacher the user wants to add.
     */
    public void addTeacher(Teacher teacher) {
        if (this.teachers.containsKey(teacher.getTeacherCode())) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Teacher Exception",
                    "An error occurred while creating a new Teacher",
                    "The given teacher already exist.");
        }
        this.teachers.put(teacher.getTeacherCode(), teacher);
    }

    /**
     * This method removes a teacher from the HashMap.
     * This method also has a error check. It checks if the HashMap contains this teacher you want to remove.
     * @param teacher the teacher the user wants to remove.
     */
    public void removeTeacher(Teacher teacher) {
        if (!this.teachers.containsKey(teacher.getTeacherCode())) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Teacher Exception",
                    "An error occurred while removing a Teacher",
                    "The given Teacher does not exist.");
        }
        this.teachers.remove(teacher.getTeacherCode());
    }

    /**
     * This method return a HashMap with group's.
     * The Key of this HashMap is a groupName.
     * @return groups in the calendar.
     */
    public HashMap<String, Group> getGroups() {
        return groups;
    }

    /**
     * This method adds a group to the HashMap.
     * This method also has a error check. It checks if the HashMap already contains this group you want to add.
     * @param group the group the user wants to add.
     */
    public void addGroup(Group group) {
        if (this.groups.containsKey(group.getGroupName())) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "Group Exception",
                    "An error occurred while creating a new Group",
                    "The given Group already exist.");
        }
        this.groups.put(group.getGroupName(), group);
    }

    /**
     * This method return a HashMap with classRoom's.
     * The Key of this HashMap is a classRoomName.
     * @return classrooms in the calendar.
     */
    public HashMap<String, ClassRoom> getClassRooms() {
        return classRooms;
    }

    /**
     * This method adds a classRoom to the HashMap.
     * This method also has a error check. It checks if the HashMap already contains this classRoom you want to add.
     * @param classRoom the classRoom the user wants to add.
     */
    public void addClassRoom(ClassRoom classRoom) {
        if (this.classRooms.containsKey(classRoom.getName())) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "ClassRoom Exception",
                    "An error occurred while creating a new ClassRoom",
                    "The given ClassRoom already exist.");
        }
        this.classRooms.put(classRoom.getName(), classRoom);
    }

    public HashMap<String, StaffRoom> getStaffRooms() {
        return staffRooms;
    }

    public void addStaffRoom(StaffRoom staffRoom) throws IllegalArgumentException{
        if (this.staffRooms.containsKey(staffRoom.getName())) {
            AlertHandler.show(Alert.AlertType.ERROR,
                    "StaffRoom Exception",
                    "An error occurred while creating a new StaffRoom",
                    "The given StaffRoom already exist.");
        }
        this.staffRooms.put(staffRoom.getName(), staffRoom);
    }

    /**
     * This method adds a new Lesson to the lessons.
     * This method also has a error check to make sure groups,teachers or classrooms wont be used conflicting times.
     * @param date The date on which the lesson takes place.
     * @param teacher The teacher who gives this lesson.
     * @param classRoom The classRoom in which the lesson is taking place.
     * @param subject The subject which is being taught here.
     * @param group The group who takes this lesson.
     * @param startTime The startTime of the lesson.
     * @param endTime The endTime of the lesson.
     */
    public void addLesson(Date date, int teacher, String classRoom, String subject, String group, TimeStamp startTime, TimeStamp endTime) throws IllegalArgumentException {
        try {
            dataCheck(teacher, classRoom, subject, group);
            schedules.get(date).addLesson(teachers.get(teacher), classRooms.get(classRoom), subjects.get(subject), this.groups.get(group), startTime, endTime);
        } catch (IllegalArgumentException ex) {
            AlertHandler.show(Alert.AlertType.ERROR, "Lesson Exception", "An error occurred while creating a new lesson", ex.toString());
            throw ex;
        }
    }

    /**
     * This method is used to check if the teacher, classRoom, subject and group actually exist.
     * When they do not exist they throw a Exception.
     * @param teacher The teacher which is being checked.
     * @param classRoom The classRoom which is being checked.
     * @param subject The subject which is being checked.
     * @param group The group which is being checked.
     * @throws IllegalArgumentException is the Exception which is thrown when the object do not exist.
     */
    private void dataCheck(int teacher, String classRoom, String subject, String group) throws IllegalArgumentException {
        boolean check = true;
        if(!this.teachers.containsKey(teacher)){
            throw new IllegalArgumentException("No teacher with the teacherCode: " + teacher + " currently exists!");
        }
        if (!this.classRooms.containsKey(classRoom)) {
            throw new IllegalArgumentException("Classroom: " + classRoom + " does not exist!");
        }
        if (!this.subjects.containsKey(subject)) {
            throw new IllegalArgumentException("Subject: " + subject + " does not exist!");
        }
        if (!this.groups.containsKey(group)) {
            throw new IllegalArgumentException("Group: " + group + " does not exist!");
        }
        for (String key: this.teachers.get(teacher).getSubjects()) {
            if(subjects.get(key).getName().equals(subject)) {
                check = false;
            }
        }
        if(check){
            throw new IllegalArgumentException(this.teachers.get(teacher).getName() + " is not allowed to teach " + subject + "!");
        }
    }

    /**
     * this method returns a hashMap with schedule's and its names.
     * @return schedules in the map.
     */
    public HashMap<Date, Schedule> getSchedules() {
        return schedules;
    }
}
