package fr.eql.courseplanner.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eql.courseplanner.entities.Course;
import fr.eql.courseplanner.repositories.CourseRepository;

@Service
public class CourseService implements ICourseService{

  @Autowired
  private CourseRepository repository;
  
  @Override
  public List<Course> findAll() {
     return (List<Course>)repository.findAll();
  }
  
  @Override
  public List<Course> findAllByPromo(Long idPromo) {
    // TODO : lambda pour filtrer sur idPromo ? 
    return (List<Course>)repository.findAll();
  }

  @Override
  public void save(Course course) {
    repository.save(course);
    
  }

}
