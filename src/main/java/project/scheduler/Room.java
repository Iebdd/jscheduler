package project.scheduler;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name="Room")
public class Room {
   
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @OneToOne
  @PrimaryKeyJoinColumn(name="room_id")
  private Integer id;
  private final LocalTime start = LocalTime.of(7, 0, 0, 0);
  private final LocalTime end = LocalTime.of(20, 0, 0, 0);
  private String name;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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
