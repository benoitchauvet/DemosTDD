package fr.eql.courseplanner.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
    }

    @Test
    public void setDuration_endDateIsValid()
    {
        course.setStartTime(LocalTime.of(9,0));
        course.setDurationInMinutes(60);

        LocalTime expectedEndTime = LocalTime.of(10,0);

        assertEquals(course.getEndTime(), expectedEndTime);
    }

    @Test
    public void setDuration_negativeDuration_Fails()
    {
        course.setStartTime(LocalTime.of(9,0));

        assertThrows(IllegalArgumentException.class, () -> {
            course.setDurationInMinutes(-1);
        });
    }

    @Test
    public void setDuration_zeroDuration_Fails()
    {
        course.setStartTime(LocalTime.of(9,0));

        assertThrows(IllegalArgumentException.class, () -> {
            course.setDurationInMinutes(0);
        });
    }

    @Test
    public void setDuration_LowerThanMinimumDuration_Fails()
    {
        course.setStartTime(LocalTime.of(9,0));

        assertThrows(IllegalArgumentException.class, () -> {
            course.setDurationInMinutes(Course.MINIMUM_DURATION_IN_MINUTES - 1);
        });
    }

}