package fr.eql.courseplanner.schedule;

import fr.eql.courseplanner.entities.Course;
import fr.eql.courseplanner.entities.Promo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*

- cours entre 9h et 17h ----- OK
- un cours à la fois pour une promo ----- OK

*/

class CourseSchedulerTest {

    private CourseScheduler scheduler;
    private Course course;
    private Promo promo;

    private final int DURATION_IN_MINUTES = 210;

    @BeforeEach
    public void setUp() {
        scheduler = new CourseScheduler();

        promo = new Promo();
        promo.setName("AI108");

        course = new Course();
        course.setStartTime(CourseScheduler.MINIMUM_START_TIME);
        course.setDurationInMinutes(DURATION_IN_MINUTES);
        course.setPromo(promo);
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

    @Test()
    public void addCourse_overlappingAnotherCourseForTheSamePromo_ThrowsException()
    {
        Course course1 = new Course();
        course1.setDurationInMinutes(DURATION_IN_MINUTES);
        course1.setStartTime(CourseScheduler.MINIMUM_START_TIME);
        course1.setPromo(promo);

        scheduler.addCourse(course1);

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
        anotherPromo.setName("Another Promo");

        Course course1 = new Course();
        course1.setDurationInMinutes(DURATION_IN_MINUTES);
        course1.setStartTime(CourseScheduler.MINIMUM_START_TIME);
        course1.setPromo(anotherPromo);

        scheduler.addCourse(course1);

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