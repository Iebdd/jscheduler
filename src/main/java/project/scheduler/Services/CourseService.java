package project.scheduler.Services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.CourseRepository;
import project.scheduler.Tables.Course;

@Service
public class CourseService {
    
    @Inject
    private CourseRepository courseRepository;

    public ResponseEntity<Course> create(String name) {
        Course c = new Course(name);
        return ResponseEntity.ok(courseRepository.save(c));
    }

    public Optional<Course> findCourseById(UUID course_id) {
        return courseRepository.findById(course_id);
    }

    public Iterable<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public Course findCourseByName(String name) {
        return courseRepository.findByCourseName(name);
    }
}
