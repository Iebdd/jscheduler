package project.scheduler.Repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import project.scheduler.Tables.Room;

/**
 * Repository representing the Bookings database table
 */
public interface RoomRepository extends CrudRepository<Room, UUID> {

    /**
     * JPA Query finding a Room based on its name
     * 
     * @param name The name of the room to be searched for
     * 
     * @return A Room object containing the given Room, if present
     */
    Room findByRoomName(String name);
}
