package fr.eql.courseplanner.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table(name="course")
public class Course {

    public Course() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="start_time")
    @NotNull
    private LocalTime startTime;

    @Column(name="duration")
    @NotNull
    private int durationInMinutes = MINIMUM_DURATION_IN_MINUTES;

    @ManyToOne(fetch = FetchType.LAZY)
    private Promo promo;

    public final static int MINIMUM_DURATION_IN_MINUTES = 30;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {

        if (durationInMinutes < MINIMUM_DURATION_IN_MINUTES)
            throw new IllegalArgumentException();

        this.durationInMinutes = durationInMinutes;
    }

    public LocalTime getEndTime() {
        return startTime.plusMinutes(getDurationInMinutes());
    }

    public Promo getPromo() {
        return this.promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

}
