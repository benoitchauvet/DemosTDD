package fr.eql.courseplanner.business;

import fr.eql.courseplanner.business.CourseScheduler;
import fr.eql.courseplanner.entities.Course;
import fr.eql.courseplanner.entities.Promo;
import fr.eql.courseplanner.services.ICourseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

/*

- cours entre 9h et 17h ----- OK
- un cours à la fois pour une promo ----- OK

*/

@RunWith(SpringRunner.class)
class CourseSchedulerTest {

    @InjectMocks
    private CourseScheduler scheduler;
    
    @Mock
    ICourseService courseServiceMock;
  
    
    private Course course;
    private Promo promo;

    private final int DURATION_IN_MINUTES = 210;
   
   
    @BeforeEach
    public void setUp() {
      
        MockitoAnnotations.initMocks(this);
      
        promo = new Promo();
        promo.setId(1l);
        promo.setName("AI108");

        course = new Course();
        course.setStartTime(CourseScheduler.MINIMUM_START_TIME);
        course.setDurationInMinutes(DURATION_IN_MINUTES);
        course.setPromo(promo);
        
        //when(courseServiceMock.findAllByPromo(Mockito.any(Long.class))).thenReturn(new ArrayList<Course>());
        //doNothing().when(courseServiceMock).save(Mockito.any(Course.class));

    }

    @Test
    public void overlapping_courseXFinishingDuringCourseY_isTrue()
    {
        Course x = new Course();
        x.setStartTime(CourseScheduler.MINIMUM_START_TIME);
        x.setDurationInMinutes(DURATION_IN_MINUTES);

        Course y = new Course();
        y.setStartTime(CourseScheduler.MINIMUM_START_TIME.plusHours(1));
        y.setDurationInMinutes(DURATION_IN_MINUTES);

        assertTrue(scheduler.isOverlapping(x,y));
        assertTrue(scheduler.isOverlapping(y,x));
    }

    @Test()
    public void overlapping_courseXFinishingBeforeCourseY_isFalse()
    {
        Course x = new Course();
        x.setStartTime(CourseScheduler.MINIMUM_START_TIME);
        x.setDurationInMinutes(DURATION_IN_MINUTES);

        Course y = new Course();
        y.setStartTime(CourseScheduler.MINIMUM_START_TIME.plusMinutes(DURATION_IN_MINUTES + 1));
        y.setDurationInMinutes(DURATION_IN_MINUTES);

        assertFalse(scheduler.isOverlapping(x,y));
        assertFalse(scheduler.isOverlapping(y,x));
    }

    @Test
    public void overlapping_courseXFinishingWhenCourseYStarts_isFalse()
    {
        Course x = new Course();
        x.setStartTime(CourseScheduler.MINIMUM_START_TIME);
        x.setDurationInMinutes(DURATION_IN_MINUTES);

        Course y = new Course();
        y.setStartTime(CourseScheduler.MINIMUM_START_TIME.plusMinutes(DURATION_IN_MINUTES));
        y.setDurationInMinutes(DURATION_IN_MINUTES);

        assertFalse(scheduler.isOverlapping(x,y));
        assertFalse(scheduler.isOverlapping(y,x));
    }

    @Test
    public void overlapping_courseXIncludedInCourseY_isTrue()
    {
        Course x = new Course();
        x.setStartTime(CourseScheduler.MINIMUM_START_TIME.plusHours(1));
        x.setDurationInMinutes(60);

        Course y = new Course();
        y.setStartTime(CourseScheduler.MINIMUM_START_TIME);
        y.setDurationInMinutes(240);

        assertTrue(scheduler.isOverlapping(x,y));
        assertTrue(scheduler.isOverlapping(y,x));
    }

    @Test
    public void overlapping_courseXSameTimesAsCourseY_isTrue()
    {
        Course x = new Course();
        x.setStartTime(CourseScheduler.MINIMUM_START_TIME);
        x.setDurationInMinutes(60);

        Course y = new Course();
        y.setStartTime(CourseScheduler.MINIMUM_START_TIME);
        y.setDurationInMinutes(60);

        assertTrue(scheduler.isOverlapping(x,y));
    }

    private void addAnotherCourse(Promo p)
    {
      Course anotherCourse = new Course();
      anotherCourse.setDurationInMinutes(DURATION_IN_MINUTES);
      anotherCourse.setStartTime(CourseScheduler.MINIMUM_START_TIME);
      anotherCourse.setPromo(p);
      
      scheduler.addCourse(anotherCourse);

      List<Course> coursesData1 = new ArrayList<Course>();
      coursesData1.add(anotherCourse);
      
      when(courseServiceMock.findAllByPromo(p.getId())).thenReturn(coursesData1);
    }
    
    @Test()
    public void addCourse_overlappingAnotherCourseForTheSamePromo_ThrowsException()
    {
        addAnotherCourse(promo);
        
        Exception exc = assertThrows(CourseScheduler.CourseNotCreatedException.class, () -> {
            scheduler.addCourse(course);
        });

        String expectedMessage = CourseScheduler.OVERLAPPING_FOR_SAME_PROMO_ERROR_MESSAGE;

        assertTrue(exc.getMessage().equals(expectedMessage));
    }

    @Test()
    public void addCourse_overlappingAnotherCourseForAnotherPromo_Succeeds()
    {
        Promo anotherPromo = new Promo();
        anotherPromo.setId(2l);
        anotherPromo.setName("AI109");

        addAnotherCourse(anotherPromo);        
        
        assertAll(() -> {
            scheduler.addCourse(course);
        });
    }

    @Test
    public void addCourse_startingBeforeMinimumStartTime_ThrowsException() {

        course.setStartTime(CourseScheduler.MINIMUM_START_TIME.minusHours(1));

        Exception exc = assertThrows(CourseScheduler.CourseNotCreatedException.class, () -> {
            scheduler.addCourse(course);
        });

        String expectedMessage = CourseScheduler.START_TIME_BEFORE_MINIMUM_ERROR_MESSAGE;

        assertTrue(exc.getMessage().equals(expectedMessage));
    }

    @Test
    public void addCourse_startingExactlyAtMinimumStartTime_succeeds() {

        course.setStartTime(CourseScheduler.MINIMUM_START_TIME);

        assertAll( () -> { scheduler.addCourse(course); });
    }

    @Test
    public void addCourse_startingAfterMinimumStartTime_succeeds() {

        course.setStartTime(CourseScheduler.MINIMUM_START_TIME.plusHours(1));

        assertAll( () -> { scheduler.addCourse(course); });
    }

    @Test
    public void addCourse_finishingBeforeMaximumEndTime_succeeds() {

        course.setStartTime(CourseScheduler.MAXIMUM_END_TIME.minusMinutes(DURATION_IN_MINUTES + 30));

        assertAll( () -> { scheduler.addCourse(course); });
    }

    @Test
    public void addCourse_finishingExactlyAtMaximumEndTime_succeeds() {

        course.setStartTime(CourseScheduler.MAXIMUM_END_TIME.minusMinutes(DURATION_IN_MINUTES));

        assertAll( () -> { scheduler.addCourse(course); });
    }

    @Test
    public void addCourse_finishingAfterMaximumEndTime_ThrowsException() {

        course.setStartTime(CourseScheduler.MAXIMUM_END_TIME.minusHours(1));

        Exception exc = assertThrows(CourseScheduler.CourseNotCreatedException.class, () -> {
            scheduler.addCourse(course);
        });

        String expectedMessage = CourseScheduler.END_TIME_EXCEEDS_MAXIMUM_ERROR_MESSAGE;

        assertTrue(exc.getMessage().equals(expectedMessage));
    }


}