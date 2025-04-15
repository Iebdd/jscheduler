package project.scheduler.Tables;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name="Rooms")
public class Room { 
   
  @Id
  @GeneratedValue(strategy=GenerationType.UUID)
  private UUID room_id;
  private final LocalTime start = LocalTime.of(7, 0, 0, 0);
  private final LocalTime end = LocalTime.of(20, 0, 0, 0);
  private String roomName;

  @OneToMany(mappedBy = "room")
  @SuppressWarnings("unused")
  private final Set<Booking> roomCourse = new HashSet<>();

  public Room() {}

  public Room(String roomName) {
    this.roomName = roomName;
  }

  public UUID getId() {
    return this.room_id;
  }

  public String getRoomName() {
    return this.roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public LocalTime getStart() {
    return start;
  }

  public LocalTime getEnd() {
    return end;
  } 
}
