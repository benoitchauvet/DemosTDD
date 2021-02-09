package fr.eql.courseplanner.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.eql.courseplanner.entities.Course;
import fr.eql.courseplanner.entities.Promo;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

}
