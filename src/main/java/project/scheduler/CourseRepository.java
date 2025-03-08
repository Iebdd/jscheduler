package project.scheduler;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called courseRepository
// CRUD refers Create, Read, Update, Delete

public interface CourseRepository extends CrudRepository<User, Integer> {

}
