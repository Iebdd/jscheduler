package project.scheduler.Tables;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Class representation of Room entries
 */
@Entity(name="Rooms")
public class Room { 
   
  @Id
  @GeneratedValue(strategy=GenerationType.UUID)
  private UUID room_id;
  private final LocalTime start = LocalTime.of(7, 0, 0, 0);
  private final LocalTime end = LocalTime.of(20, 0, 0, 0);
  private String room_name;

  @SuppressWarnings("unused")
  @JsonIgnore
  @OneToMany(mappedBy = "room")
  private final Set<Booking> roomCourse = new HashSet<>();

  /**
   * Default constructor
   */
  public Room() {}

  /**
   * Full constructor
   * 
   * @param roomName  The instance room name
   */
  public Room(String roomName) {
    this.room_name = roomName;
  }

  /**
   * Getter for the room id
   * 
   * @return  The instance room id
   */
  public UUID getId() {
    return this.room_id;
  }

  /**
   * Getter for room name
   * 
   * @return  The instance room name
   */
  public String getRoomName() {
    return this.room_name;
  }

  /**
   * Setter for room name
   * 
   * @param roomName  The room name to be set
   */
  public void setRoomName(String roomName) {
    this.room_name = roomName;
  }

  /**
   * Getter for start (The earliest courses can take place)
   * 
   * @return  The start time
   */
  public LocalTime getStart() {
    return start;
  }

  /**
   * Getter for end (The latest courses can take place)
   * 
   * @return  The end time
   */
  public LocalTime getEnd() {
    return end;
  } 
}
