package project.scheduler.Tables;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import project.scheduler.Services.BookingService.Status;

@Entity(name = "Bookings") 
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookings_id;

    @ManyToOne
    @JoinColumn(name = "b_room_id", referencedColumnName = "room_id") // Foreign key reference to Room
    private Room room;

    @ManyToOne
    @JoinColumn(name = "b_course_id", referencedColumnName = "course_id") // Foreign key reference to Course
    private Course course;

    private Instant start;
    private Instant end;
    private Status status;

    public Bookings() {};

    public Bookings(Room room, Course course, Instant start, Instant end, Status status) {
        this.room = room;
        this.course = course;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public UUID getId() {
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

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
