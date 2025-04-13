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

    public ResponseEntity<String> create(String name) {
        Room r = new Room(name);
        UUID id = roomRepository.save(r).getId();
        return ResponseEntity.ok(String.format("Added Room '%s' with id: %s", name, id));
    }

    public Optional<Room> findRoomById(UUID room_id) {
        return roomRepository.findById(room_id);
    }

    public ResponseEntity<Room[]> getRooms() {
        return ResponseEntity.ok(roomRepository.getAllRooms());
    }
}
