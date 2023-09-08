package schoolplanner.util.filter;

import schoolplanner.calendar.lessons.Lesson;

import java.io.Serializable;
import java.util.ArrayList;

public class TeacherFilter implements FilterableObject, Serializable {
    TeacherFilter() {
    }

    /**
     * Filter function used to specifically filter based on the Teacher used in the lessons.
     * @param lessons The full list of all the lessons to filter.
     * @param criteria The criteria/Room name to use inside the filter.
     * @return The filtered list based on the used criteria.
     */
    @Override
    public ArrayList<Lesson> filter(ArrayList<Lesson> lessons, String criteria) {
        ArrayList<Lesson> filteredList = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (Integer.toString(lesson.getTeacher().getTeacherCode()).equals(criteria)) {
                filteredList.add(lesson);
            }
        }
        if (filteredList.isEmpty()) {
            throw new IllegalArgumentException("No Teachers in this lessons match the given criteria");
        }
        return filteredList;
    }
}
