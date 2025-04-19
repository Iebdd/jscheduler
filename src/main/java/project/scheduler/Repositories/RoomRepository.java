package project.scheduler.Repositories;

import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import project.scheduler.Tables.Room;

/**
 * Repository representing the Bookings database table
 */
public interface RoomRepository extends CrudRepository<Room, UUID> {

    /**
     * Selects a room based on its name
     * 
     * @param name The name of the room to be searched for
     * 
     * @return A Room object containing the given Room, if present
     */
    @NativeQuery(value = "SELECT * FROM rooms r WHERE r.room_name = ?1")
    Room findByRoomName(String roomName);

    /**
     *  Selects the start time of the given room
     * 
     * @param course_id The id of the room in question
     * 
     * @return  The start time of the room
     */
    @NativeQuery(value = "SELECT r.start FROM rooms r WHERE r.room_id = ?1")
    LocalTime findStartById(UUID course_id);

    /**
     *  Selects the end time of the given room
     * 
     * @param course_id The id of the room in question
     * 
     * @return  The end time of the room
     */
    @NativeQuery(value = "SELECT r.end FROM rooms r WHERE r.room_id = ?1")
    LocalTime findEndById(UUID course_id);

}
