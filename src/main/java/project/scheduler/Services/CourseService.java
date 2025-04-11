package project.scheduler.Services;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.CourseRepository;
import project.scheduler.Tables.Course;

@Service
public class CourseService {
    
    @Inject
    private CourseRepository courseRepository;

    public ResponseEntity<String> create(String name) {
        Course c = new Course(name);
        int id = courseRepository.save(c).getId();
        return ResponseEntity.ok(String.format("Added Course '%s' with id: %d", name, id));
    }

    public Optional<Course> findCourseById(int course_id) {
        return courseRepository.findById(course_id);
    }
}
