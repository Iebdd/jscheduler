package project.scheduler.Repositories;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import project.scheduler.Services.BookingService.Status;
import project.scheduler.Tables.Booking;

@Transactional
public interface BookingRepository extends CrudRepository<Booking, UUID> {

    @NativeQuery(value ="SELECT * FROM bookings b WHERE b.start >= ?1 AND b.end <= ?2 AND b.b_course_id = ?3")
    Iterable<Booking> getTimeConflicts(Instant start, Instant end, UUID course_id);

    @NativeQuery(value ="SELECT * FROM bookings b WHERE b.start >= ?1 AND b.end <= ?2 AND b.b_room_id = ?3")
    Iterable<Booking> getRoomConflicts(Instant start, Instant end, UUID room_id);

    @Modifying
    @NativeQuery(value = "UPDATE bookings b SET b.status = ?1 WHERE b.bookings_id = ?2")
    Integer updateStatus(Status status, UUID course_id);

    @Modifying
    @NativeQuery(value = "DELETE FROM bookings b WHERE b.bookings_id = ?1")
    Integer removeById(UUID bookings_id);
}
