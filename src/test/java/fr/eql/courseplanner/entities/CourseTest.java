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


}