package fr.eql.courseplanner.schedule;

import fr.eql.courseplanner.entities.Course;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CourseScheduler {

    static final LocalTime MINIMUM_START_TIME = LocalTime.of(9,0);
    static final LocalTime MAXIMUM_END_TIME = LocalTime.of(17,0);
    private List<Course> courses = new ArrayList<Course>();

    public boolean addCourse(Course courseToAdd) {

        if (courseToAdd.getStartTime().isBefore(MINIMUM_START_TIME))
             return false;

        if(courseToAdd.getEndTime().isAfter(MAXIMUM_END_TIME))
            return false;

        for(Course course : courses) {
            if(course.getPromo().equals(courseToAdd.getPromo())
                && isOverlapping(courseToAdd, course))
                return false;
        }

        courses.add(courseToAdd);
        return true;
    }

    boolean isOverlapping(Course courseX, Course courseY) {
        if((courseX.getEndTime().isBefore(courseY.getStartTime()) || courseX.getEndTime().equals(courseY.getStartTime()))
            || courseX.getStartTime().isAfter(courseY.getEndTime()) ||courseX.getStartTime().equals(courseY.getEndTime()))
            return false;
        return true;
    }

}