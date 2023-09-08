package schoolplanner.util.filter;

import schoolplanner.calendar.Schedule;

import java.io.Serializable;

public class FilterController implements Serializable {
    private FilterableObject filterableObject;

    public FilterController() {
    }

    /**
     *
     * @param schedule The lessons which to filter.
     * @param type The type of data to filter
     * @param criteria The criteria with which to filter the data.
     * @return A new filtered lessons based on the parameters.
     * @throws IllegalArgumentException Exception thrown if the lessons does not contain any lessons.
     */
    public Schedule getFilteredSchedule(Schedule schedule, String type, String criteria) throws IllegalArgumentException {
        this.setFilterableObject(type);
        if (schedule.getLessons().isEmpty()) {
            throw new IllegalArgumentException("The given lessons contains no lessons.");
        }
        return new Schedule(this.filterableObject.filter(schedule.getLessons(), criteria));
    }

    /**
     * Helper function to set the correct filter active inside the controller
     * @param type The type of data to check, used to identify which filter to use.
     * @throws IllegalArgumentException The exception is thrown if no filter with the given type was found.
     */
    private void setFilterableObject(String type) throws IllegalArgumentException {
        switch (type) {
            case "Subject" :
                this.filterableObject = new SubjectFilter();
                break;
            case "ClassRoom" :
                this.filterableObject = new ClassRoomFilter();
                break;
            case "Group" :
                this.filterableObject = new GroupFilter();
                break;
            case "Teacher" :
                this.filterableObject = new TeacherFilter();
                break;
                default: throw new IllegalArgumentException("The given object type can not be used as a filter.");
        }
    }
}