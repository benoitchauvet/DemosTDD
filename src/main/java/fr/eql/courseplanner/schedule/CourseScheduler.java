package fr.eql.courseplanner.schedule;

import fr.eql.courseplanner.entities.Course;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CourseScheduler {

    static final LocalTime MINIMUM_START_TIME = LocalTime.of(9,0);
    static final LocalTime MAXIMUM_END_TIME = LocalTime.of(17,0);

    public static final String OVERLAPPING_FOR_SAME_PROMO_ERROR_MESSAGE = "Course overlapping with another course for this promo";
    public static final String START_TIME_BEFORE_MINIMUM_ERROR_MESSAGE = "Start time before minimum";
    public static final String END_TIME_EXCEEDS_MAXIMUM_ERROR_MESSAGE = "End time exceeds maximum";

    private List<Course> courses = new ArrayList<Course>();

    public void addCourse(Course courseToAdd) {

        if (courseToAdd.getStartTime().isBefore(MINIMUM_START_TIME))
             throw new CourseNotCreatedException(START_TIME_BEFORE_MINIMUM_ERROR_MESSAGE, courseToAdd);

        if(courseToAdd.getEndTime().isAfter(MAXIMUM_END_TIME))
            throw new CourseNotCreatedException(END_TIME_EXCEEDS_MAXIMUM_ERROR_MESSAGE, courseToAdd);

        for(Course course : courses) {
            if(course.getPromo().equals(courseToAdd.getPromo())
                && isOverlapping(courseToAdd, course))
                throw new CourseNotCreatedException(OVERLAPPING_FOR_SAME_PROMO_ERROR_MESSAGE, courseToAdd);
        }

        courses.add(courseToAdd);
    }

    boolean isOverlapping(Course courseX, Course courseY) {
        if((courseX.getEndTime().isBefore(courseY.getStartTime()) || courseX.getEndTime().equals(courseY.getStartTime()))
            || courseX.getStartTime().isAfter(courseY.getEndTime()) ||courseX.getStartTime().equals(courseY.getEndTime()))
            return false;
        return true;
    }

    public class CourseNotCreatedException extends RuntimeException {
        private final Course course;

        public CourseNotCreatedException(String message, Course courseToAdd) {
            super(message);
            this.course = courseToAdd;
        }

        public Course getCourse()
        {
            return course;
        }
    }
}