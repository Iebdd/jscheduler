package project.scheduler.Repositories;

import org.springframework.data.repository.CrudRepository;

import project.scheduler.Tables.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called roomCourseRepository
// CRUD refers Create, Read, Update, Delete

public interface RoomCourseRepository extends CrudRepository<User, Integer> {

}
