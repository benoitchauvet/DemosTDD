package fr.eql.courseplanner.services;

import java.util.List;

import fr.eql.courseplanner.entities.Course;

public interface ICourseService {

  List<Course> findAll();

  void save(Course course);

  List<Course> findAllByPromo(Long idPromo);
  
}
