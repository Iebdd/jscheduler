package project.scheduler.Util;

import java.util.UUID;

public class UserBooking {
    UUID[] room_conflicts;
    UUID[] course_conflict;
    UUID booking_id;


    public UserBooking() {};

    public UserBooking(UUID[] conflicts) {
        this.conflict = true;
        this.booking_id = null;
        this.conflicts = conflicts;

    }

    public UserBooking(UUID booking_id) {
        this.conflict = false;
        this.booking_id = booking_id;
        this.conflicts = null;
    }

    public boolean isConflict() {
        return conflict;
    }

    public void setConflict(boolean conflict) {
        this.conflict = conflict;
    }

    public UUID[] getConflicts() {
        return conflicts;
    }

    public void setConflicts(UUID[] conflicts) {
        this.conflicts = conflicts;
    }

    public UUID getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(UUID booking_id) {
        this.booking_id = booking_id;
    }
}
