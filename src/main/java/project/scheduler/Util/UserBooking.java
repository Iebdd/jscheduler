package project.scheduler.Util;

import java.util.ArrayList;
import java.util.UUID;



/**
 * A class responsible for transmitting booking ids and conflicts
 */
public class UserBooking {
    ArrayList<UUID> room_conflicts;
    ArrayList<UUID> time_conflicts;
    UUID booking_id;


    public UserBooking() {};

    public UserBooking(boolean conflict) {
        this.booking_id = null;
    }

    public UserBooking(UUID booking_id) {
        this.booking_id = booking_id;
        this.room_conflicts = null;
        this.time_conflicts = null;
    }

    public void setRoomConflicts(ArrayList<UUID> room_conflicts) {
        this.room_conflicts = room_conflicts;
    }

    public void setTimeConflicts(ArrayList<UUID> time_conflicts) {
        this.time_conflicts = time_conflicts;
    }

    public ArrayList<UUID> getRoomConflicts() {
        return this.room_conflicts;
    }

    public ArrayList<UUID> getTimeConflicts() {
        return this.time_conflicts;
    }

    public UUID getBooking_id() {
        return this.booking_id;
    }
}
