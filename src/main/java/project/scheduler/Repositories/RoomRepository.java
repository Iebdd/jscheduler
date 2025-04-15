package project.scheduler.Repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import project.scheduler.Tables.Room;

public interface RoomRepository extends CrudRepository<Room, UUID> {

    Room findByRoomName(String name);
}
