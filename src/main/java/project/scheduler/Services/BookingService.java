package project.scheduler.Services;

import java.time.Instant;
import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.BookingsRepository;
import project.scheduler.Tables.Bookings;
import project.scheduler.Tables.Course;
import project.scheduler.Tables.Room;
import project.scheduler.Util.UserBooking;

@Service
public class BookingService {

    @Inject
    private BookingsRepository bookingsRepository;

    public enum Status {
        Set(0),
        Planned(1);
    
        private final int num;
    
        Status(int num) {
            this.num = num;
        }
    
        public int getNum() {
            return this.num;
        }
    }

    public ResponseEntity<UserBooking> setBooking(Course course, Room room, Instant start, Instant end, boolean isAdmin) {
        Iterable<Bookings> conflict_bookings = bookingsRepository.getConflicts(start, end, course.getId(), room.getId());
        if(IterableUtils.size(conflict_bookings) != 0) {
            UUID[] conflicts = new UUID[IterableUtils.size(conflict_bookings)];
            Iterator<Bookings> cb_it = conflict_bookings.iterator();
            for(int index = 0; index < conflicts.length; index++) {
                conflicts[index] = cb_it.next().getId();
            }
            return new ResponseEntity<>(new UserBooking(conflicts), HttpStatus.CONFLICT);
        }
        Status status = (isAdmin) ? Status.Set : Status.Planned;
        UUID new_id = bookingsRepository.save(new Bookings(room, course, start, end, status)).getId();
        return ResponseEntity.ok(new UserBooking(new_id));
    }
}
