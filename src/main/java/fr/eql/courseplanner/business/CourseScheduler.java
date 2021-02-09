package fr.eql.courseplanner.business;

import fr.eql.courseplanner.entities.Course;
import fr.eql.courseplanner.services.ICourseService;
import fr.eql.courseplanner.services.IPromoService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Component
public class CourseScheduler {

  
    @Autowired
    private ICourseService courseService;
    
    static final LocalTime MINIMUM_START_TIME = LocalTime.of(9,0);
    static final LocalTime MAXIMUM_END_TIME = LocalTime.of(17,0);

    public static final String OVERLAPPING_FOR_SAME_PROMO_ERROR_MESSAGE = "Course overlapping with another course for this promo";
    public static final String START_TIME_BEFORE_MINIMUM_ERROR_MESSAGE = "Start time before minimum";
    public static final String END_TIME_EXCEEDS_MAXIMUM_ERROR_MESSAGE = "End time exceeds maximum";

    public CourseScheduler()
    {
      
    }
    
    public void addCourse(Course courseToAdd) {

        verifyCourseTimeRange(courseToAdd);
        
        verifyOverlappingForSamePromo(courseToAdd);

        courseService.save(courseToAdd);
    }

    private void verifyOverlappingForSamePromo(Course courseToAdd) throws CourseNotCreatedException {
      Long promoId = courseToAdd.getPromo().getId();
      
      for(Course course : courseService.findAllByPromo(promoId)) {

        if(isOverlapping(courseToAdd, course))
              throw new CourseNotCreatedException(OVERLAPPING_FOR_SAME_PROMO_ERROR_MESSAGE, courseToAdd);
      }
    }

    private void verifyCourseTimeRange(Course courseToAdd) throws CourseNotCreatedException {
      if (courseToAdd.getStartTime().isBefore(MINIMUM_START_TIME))
           throw new CourseNotCreatedException(START_TIME_BEFORE_MINIMUM_ERROR_MESSAGE, courseToAdd);

      if(courseToAdd.getEndTime().isAfter(MAXIMUM_END_TIME))
          throw new CourseNotCreatedException(END_TIME_EXCEEDS_MAXIMUM_ERROR_MESSAGE, courseToAdd);
    }

    boolean isOverlapping(Course courseX, Course courseY) {
        if((courseX.getEndTime().isBefore(courseY.getStartTime()) || courseX.getEndTime().equals(courseY.getStartTime()))
            || courseX.getStartTime().isAfter(courseY.getEndTime()) ||courseX.getStartTime().equals(courseY.getEndTime()))
            return false;
        return true;
    }

    public class CourseNotCreatedException extends RuntimeException {

      private static final long serialVersionUID = -2968722127548014763L;
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