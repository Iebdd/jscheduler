package project.scheduler.Repositories;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import project.scheduler.Services.BookingService.Status;
import project.scheduler.Tables.Booking;

/**
 * Repository representing the Bookings database table
 */
@Transactional
public interface BookingRepository extends CrudRepository<Booking, UUID> {

    /**
     * Selects all Bookings which take place at the same time as the given one
     * 
     * @param start       The planned start time of the booking as an Instant object
     * @param end         The planned end time of the booking as an Instant object
     * @param course_id   The id of the course in question as a UUID object
     * 
     * @return  An Iterable of all conflicting bookings
     */
    @NativeQuery(value ="SELECT * FROM bookings b WHERE b.start >= ?1 AND b.end <= ?2 AND b.b_course_id = ?3")
    Iterable<Booking> getTimeConflicts(Instant start, Instant end, UUID course_id);

    /**
     * Selects all Bookings which take place in the same room as the current one
     * 
     * @param start       The planned start time of the booking as an Instant object
     * @param end         The planned end time of the booking as an Instant object
     * @param room_id     The id of the room in question as a UUID object
     * 
     * @return  An Iterable of all conflicting bookings
     */
    @NativeQuery(value ="SELECT * FROM bookings b WHERE b.start >= ?1 AND b.end <= ?2 AND b.b_room_id = ?3")
    Iterable<Booking> getRoomConflicts(Instant start, Instant end, UUID room_id);

    /**
     * Updates the status of the given booking
     * 
     * @param status      The new status of the booking
     * @param course_id   The course to be changed
     * 
     * @return  Tthe amount of changed entries. 1 for the booking having been changed. 0 for remaining unchanged
     */
    @Modifying
    @NativeQuery(value = "UPDATE bookings b SET b.status = ?1 WHERE b.bookings_id = ?2")
    Integer updateStatus(Status status, UUID course_id);

    /**
     * Removes a booking based on the id given
     * 
     * @param bookings_id   Id of the booking to be removed as a UUID object
     * 
     * @return  The amount of deleted entries. 1 for the booking having been deleted. 0 for remaining unchanged
     */
    @Modifying
    @NativeQuery(value = "DELETE FROM bookings b WHERE b.bookings_id = ?1")
    Integer removeById(UUID bookings_id);

    /**
     * Selects all bookings based on a given user id
     * 
     * @param user_id   The user id to be searched for
     * 
     * @return  An Iterable containing all the courses the user is inscribed in
     */
    @NativeQuery(value = "SELECT * FROM (SELECT * FROM inscriptions i WHERE i.i_user_id = ?1) i, bookings b WHERE i.i_course_id = b.b_course_id") 
    Iterable<Booking> findAllByUserId(UUID user_id);

    /**
     *  Selects alls courses which take place in the given room
     * 
     * @param room_id   The id of the room in question
     * 
     * @return  An Iterable of all courses taking place in this room
     */
    @NativeQuery(value = "SELECT * FROM bookings b WHERE b.b_room_id = ?1")
    Iterable<Booking> findAllByRoomId(UUID room_id);

    /**
     *  Selects all courses happening in a given room on a given day
     * 
     * @param room_id   The id of the room in question
     * @param start     The earliest time on the given day
     * @param end       The latest time on the given day
     * 
     * @return  An Iterable containing all rooms happening in the given room on the given day
     */
    @NativeQuery(value = "SELECT room_id FROM bookings b WHERE b.b_room_id = ?1 AND b.start >= ?2 AND b.end <= ?3")
    Iterable<Booking> findAllByRoomIdAndDate(UUID room_id, LocalDateTime start, LocalDateTime end);


}
