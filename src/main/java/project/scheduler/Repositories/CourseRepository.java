package project.scheduler.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import project.scheduler.Tables.Course;

public interface CourseRepository extends CrudRepository<Course, UUID> {

    @NativeQuery(value = "SELECT * FROM course")
    Course[] getCourses();
}
