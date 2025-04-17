package project.scheduler.Services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.CourseRepository;
import project.scheduler.Tables.Course;

/**
 * The service responsible for courses
 */
@Service
public class CourseService {
    
    @Inject
    private CourseRepository courseRepository;

    /**
     * Adds a new course to the database
     * 
     * @param name  The name of the course to be created
     * 
     * @return  A Course object representing the new entry
     */
    public ResponseEntity<Course> create(String name) {
        Course c = new Course(name);
        return ResponseEntity.ok(courseRepository.save(c));
    }

    /**
     *  Finds a course based on its id
     * 
     * @param course_id The id of the course in question as a UUID object
     * 
     * @return  A Course representation of the entry, if present
     */
    public Optional<Course> findCourseById(UUID course_id) {
        return courseRepository.findById(course_id);
    }

    /**
     * Returns all courses in the database
     * 
     * @return  An Iterable of all courses
     */
    public Iterable<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Finds a course based on its name
     * 
     * @param name  The name of the course in question
     * 
     * @return  A Course representation of the entry, if present
     */
    public Course findCourseByName(String name) {
        return courseRepository.findByCourseName(name);
    }
}
