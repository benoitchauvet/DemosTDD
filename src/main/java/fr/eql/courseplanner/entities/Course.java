package fr.eql.courseplanner.entities;

import java.time.LocalTime;

public class Course {

    private LocalTime startTime;
    private int durationInMinutes;
    private Promo promo;

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return startTime.plusMinutes(getDurationInMinutes());
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public Promo getPromo() {
        return this.promo;
    }
}
