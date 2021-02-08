package fr.eql.courseplanner.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "promo")
public class Promo {

    public Promo() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
