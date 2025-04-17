package project.scheduler.Services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.RoomRepository;
import project.scheduler.Tables.Room;

/**
 * The service responsible for rooms
 */
@Service
public class RoomService {
    @Inject
    private RoomRepository roomRepository;

    /**
     *  Creates a new room
     * 
     * @param name  The name of the room to be created
     * 
     * @return  A Room object representing the new entry
     */
    public ResponseEntity<Room> create(String name) {
        Room r = new Room(name);
        roomRepository.save(r);
        return ResponseEntity.ok(r);
    }

    /**
     *  Finds a room based on its id
     * 
     * @param room_id   The id of the room in question as a UUID object
     * 
     * @return  A Room object representing the found entry, or null if it wasn't found
     */
    public Optional<Room> findRoomById(UUID room_id) {
        return roomRepository.findById(room_id);
    }

    /**
     * Returns all rooms in the database
     * 
     * @return  An Iterable of all Rooms present
     */
    public ResponseEntity<Iterable<Room>> findAllRooms() {
        return ResponseEntity.ok(roomRepository.findAll());
    }

    /**
     * Finds a room by its name
     * 
     * @param name  The name of the room in question
     * 
     * @return  A Room object representing the found entry, or null if it wasn't found
     */
    public Room findRoomByName(String name) {
        return roomRepository.findByRoomName(name);
    }
}
