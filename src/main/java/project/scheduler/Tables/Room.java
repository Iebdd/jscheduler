package project.scheduler.Tables;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name="Room")
public class Room { 
   
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer room_id;
  private final LocalTime start = LocalTime.of(7, 0, 0, 0);
  private final LocalTime end = LocalTime.of(20, 0, 0, 0);
  private String name;

  @OneToMany(mappedBy = "room")
  @SuppressWarnings("unused")
  private final Set<Bookings> roomCourse = new HashSet<>();

  public Room() {}

  public Room(String name) {
    this.name = name;
  }

  public Integer getId() {
    return room_id;
  }

  public void setId(Integer id) {
    this.room_id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalTime getStart() {
    return start;
  }

  public LocalTime getEnd() {
    return end;
  } 
}
