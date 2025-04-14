package project.scheduler.Repositories;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import project.scheduler.Tables.Bookings;

public interface BookingsRepository extends CrudRepository<Bookings, UUID> {

    @NativeQuery(value = "SELECT * FROM bookings b WHERE (b.start >= ?1 AND b.end <= ?2) AND (b.b_room_id = ?3 OR b.b_course_id = ?3)")
    Iterable<Bookings> getConflicts(Instant start, Instant end, UUID room_id, UUID course_id);

    @NativeQuery(value ="SELECT * FROM bookings b WHERE b.start >= ?1 AND b.end <= ?2 AND b.b_course_id = ?3")
    Iterable<Bookings> getCourseConflicts(Instant start, Instant end, UUID course_id);
}
