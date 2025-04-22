package project.scheduler.Tables;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import project.scheduler.Services.BookingService.Status;

/**
 * Class representation of Bookings entries
 */
@Entity(name = "Bookings") 
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookings_id;

    @ManyToOne
    @JoinColumn(name = "b_room_id", referencedColumnName = "room_id") // Foreign key reference to Room
    private Room room;

    @ManyToOne
    @JoinColumn(name = "b_course_id", referencedColumnName = "course_id") // Foreign key reference to Course
    private Course course;

    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;

    /**
     * Default constructor
     */
    public Booking() {};

    /**
     * Full constructor
     * 
     * @param room      The room to reference
     * @param course    The course to reference
     * @param start     The start time as a LocalDateTime
     * @param end       The end time as a LocalDateTime
     * @param status    The status to be saved as as a Status enum
     */
    public Booking(Room room, Course course, LocalDateTime start, LocalDateTime end, Status status) {
        this.room = room;
        this.course = course;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    /**
     * Setter for start
     * 
     * @param start The start to save
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Setter for end
     * 
     * @param end The end to save
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Getter for booking_id
     * 
     * @return  The current id
     */
    public UUID getBookings_id() {
        return bookings_id;
    }

    /**
     * Getter for room
     * 
     * @return  The room referenced
     */
    public Room getRoom() {
        return room;
    }
    
    /**
     * Setter for room
     * 
     * @param room The room to be referenced
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Getter for course
     * 
     * @return  The current course referenced
     */
    public Course getCourse() {
        return course;
    }
    
    /**
     * Setter for course
     * 
     * @param course The course to be referenced
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Getter for start
     * 
     * @return  The current start
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Getter for end
     * 
     * @return  The current end
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Getter for status
     * 
     * @return  The current status
     */
    public Status getStatus() {
        return this.status;
    }
    
    /**
     * Setter for status
     * 
     * @param status The status to save
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder new_string = new StringBuilder();
        new_string.append(String.format("{\"bookings_id\": \"%s\", \"room_id\": \"%s\", \"course_id\": \"%s\", \"start\": \"%s\", \"end\": \"%s\", \"status\": \"%s\"}", 
                                            this.bookings_id, this.room.getId(), this.course.getId(), this.start, this.end, this.status));
        return new_string.toString();
    }
}
