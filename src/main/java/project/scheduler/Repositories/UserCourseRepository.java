package project.scheduler.Repositories;

import org.springframework.data.repository.CrudRepository;

import project.scheduler.Tables.UserCourse;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userCourseRepository
// CRUD refers Create, Read, Update, Delete

public interface UserCourseRepository extends CrudRepository<UserCourse, Integer> {

}
