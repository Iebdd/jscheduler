package project.scheduler.Tables;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "Bookings") 
public class Bookings {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id") // Foreign key reference to Room
    private Room room;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id") // Foreign key reference to Course
    private Course course;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
