package project.scheduler.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

import project.scheduler.Tables.Room;

// This will be AUTO IMPLEMENTED by Spring into a Bean called roomRepository
// CRUD refers Create, Read, Update, Delete

public interface RoomRepository extends CrudRepository<Room, UUID> {

    @NativeQuery(value = "SELECT * FROM room")
    Room[] getAllRooms();

}
