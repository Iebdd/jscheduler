package project.scheduler.Tables;

import java.time.Duration;
import java.time.Instant;

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
    private Integer bookings_id;

    @ManyToOne
    @JoinColumn(name = "b_room_id", referencedColumnName = "room_id") // Foreign key reference to Room
    private Room room;

    @ManyToOne
    @JoinColumn(name = "b_course_id", referencedColumnName = "course_id") // Foreign key reference to Course
    private Course course;

    private Instant start;
    private Instant end;
    private Duration length;

    public void setStart(Instant start) {
        this.start = start;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public void setLength(Duration length) {
        this.length = length;
    }

    public Integer getId() {
        return bookings_id;
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

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public Duration getLength() {
        return length;
    }
}
