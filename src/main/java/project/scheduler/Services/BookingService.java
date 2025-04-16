package project.scheduler.Services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.BookingRepository;
import project.scheduler.Tables.Booking;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Room;
import project.scheduler.Util.UserBooking;

/**
 * The service responsible for bookings (Courses taking place in a room at a specific time)
 */
@Service
public class BookingService {

    @Inject
    private BookingRepository bookingRepository;

    /**
     * An enum representing the possible stati of a booking
     */
    public enum Status {
        Set(0),
        Planned(1),
        Preference(2);
    
        private final int num;
    
        Status(int num) {
            this.num = num;
        }
    
        public int getNum() {
            return this.num;
        }
    }

    /**
     * Sets a booking and confirms possible conflicts
     * 
     * @param course    
     * @param room
     * @param start
     * @param end
     * @param status
     * 
     * @return
     */
    public ResponseEntity<UserBooking> setBooking(Course course, Room room, Instant start, Instant end, Status status) {
        Iterable<Booking> rconflicts_it = bookingRepository.getRoomConflicts(start, end, room.getId());
        Iterable<Booking> tconflicts_it = bookingRepository.getTimeConflicts(start, end , course.getId());
        int room_num = IterableUtils.size(rconflicts_it);
        int time_num = IterableUtils.size(tconflicts_it);
        if (room_num != 0 || time_num != 0) {
            UserBooking c_booking = new UserBooking(true);
            ArrayList<UUID> r_conflicts;
            ArrayList<UUID> t_conflicts;
            if(room_num != 0) {
                r_conflicts = new ArrayList<>();
                for(Booking booking : rconflicts_it) {
                    r_conflicts.add(booking.getId());
                }
                c_booking.setRoomConflicts(r_conflicts);
            }
            if(time_num != 0) {
                t_conflicts = new ArrayList<>();
                for (Booking booking : tconflicts_it) {
                    t_conflicts.add(booking.getId());
                }
                c_booking.setTimeConflicts(t_conflicts);
            }
            return new ResponseEntity<>(c_booking, HttpStatus.CONFLICT);
        }

        UUID new_id = bookingRepository.save(new Booking(room, course, start, end, status)).getId();
        return ResponseEntity.ok(new UserBooking(new_id));
    }

    public ResponseEntity<Integer> updateBookingStatus(Status status, UUID course_id) {
        return ResponseEntity.ok(bookingRepository.updateStatus(status, course_id));
    }

    public Booking findBookingById(UUID booking_id) {
        return bookingRepository.findById(booking_id).orElse(null);
    }

    public Integer removeBookingById(UUID booking_id) {
        return bookingRepository.removeById(booking_id);
    }

    public ResponseEntity<Iterable<Booking>> findAllBookingsByUser(UUID user_id) {
        return ResponseEntity.ok(bookingRepository.findAllByUserId(user_id));
    }
}
