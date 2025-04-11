package project.scheduler.Services;

import java.util.Optional;

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
        int id = roomRepository.save(r).getId();
        return ResponseEntity.ok(String.format("Added Room '%s' with id: %d", name, id));
    }

    public Optional<Room> findRoomById(int room_id) {
        return roomRepository.findById(room_id);
    }
}
