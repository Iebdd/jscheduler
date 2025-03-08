package project.scheduler;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userCourseRepository
// CRUD refers Create, Read, Update, Delete

public interface UserCourseRepository extends CrudRepository<User, Integer> {

}
