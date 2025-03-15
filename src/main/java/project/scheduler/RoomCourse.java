package project.scheduler;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity(name = "RoomCourse") 
public class RoomCourse {

    @EmbeddedId
    private RoomCourseId id = new RoomCourseId(); // Unique ID for RoomCourse

    @ManyToOne
    //@JoinColumn(name = "room_id", referencedColumnName = "id") // Foreign key reference to Room
    @MapsId("roomId")
    private Room room;

    @ManyToOne
    //@JoinColumn(name = "course_id", referencedColumnName = "id") // Foreign key reference to Course
    @MapsId("courseId")
    private Course course;

    // Getters and Setters
    public RoomCourseId getId() {
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
