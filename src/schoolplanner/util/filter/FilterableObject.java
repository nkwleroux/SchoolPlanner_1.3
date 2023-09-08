package schoolplanner.util.filter;

import schoolplanner.calendar.lessons.Lesson;

import java.util.ArrayList;

/**
 * Filterable object describes all possible types to filter, each has it's own dedicated class.
 */
public interface FilterableObject {
    ArrayList<Lesson> filter(ArrayList<Lesson> lessons, String criteria);
}
