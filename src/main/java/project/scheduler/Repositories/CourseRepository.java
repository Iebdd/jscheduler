package project.scheduler.Repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import project.scheduler.Tables.Course;
/**
 * Repository representing the Bookings database table
 */
public interface CourseRepository extends CrudRepository<Course, UUID> {

    /**
     * JPA Query to find a course by its name
     * 
     * @param name  The name of the course in question
     * 
     * @return  A Course object of the relevant Course, if present
     */
    Course findByCourseName(String name);
}
