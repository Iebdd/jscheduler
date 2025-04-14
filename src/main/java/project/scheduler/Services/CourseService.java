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
        courseRepository.save(c);
        return ResponseEntity.ok(c);
    }

    public Optional<Course> findCourseById(UUID course_id) {
        return courseRepository.findById(course_id);
    }
}
