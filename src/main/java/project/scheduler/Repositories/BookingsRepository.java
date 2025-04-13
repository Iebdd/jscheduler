package project.scheduler.Repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import project.scheduler.Tables.Bookings;

// This will be AUTO IMPLEMENTED by Spring into a Bean called roomCourseRepository
// CRUD refers Create, Read, Update, Delete

public interface BookingsRepository extends CrudRepository<Bookings, UUID> {

}
