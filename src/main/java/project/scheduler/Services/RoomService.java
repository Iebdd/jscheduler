package project.scheduler.Services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import project.scheduler.Repositories.RoomRepository;
import project.scheduler.Tables.Room;

@Service
public class RoomService {
    @Inject
    private RoomRepository roomRepository;

    public ResponseEntity<Room> create(String name) {
        Room r = new Room(name);
        roomRepository.save(r);
        return ResponseEntity.ok(r);
    }

    public Optional<Room> findRoomById(UUID room_id) {
        return roomRepository.findById(room_id);
    }

    public ResponseEntity<Iterable<Room>> getRooms() {
        return ResponseEntity.ok(roomRepository.findAll());
    }

    public Room findRoomByName(String name) {
        return roomRepository.findByRoomName(name);
    }
}
