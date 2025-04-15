package project.scheduler.Tables;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity(name="Courses")
public class Course {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID course_id;

    private String courseName;

    @JsonIgnore
    @ManyToMany(mappedBy = "course")  // Match property in UserCourse
    private final Set<User> user = new HashSet<>(); // Relationship with UserCourse

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private final Set<Booking> roomCourse = new HashSet<>();

/*     @ManyToMany(mappedBy = "course")  // Match property in RoomCourse
    private RoomCourse roomCourse; // Relationship with RoomCourse */

    // Default constructor
    public Course() {}

    public Course(String courseName) {
        this.courseName = courseName;
    }

    // Getters and setters
    public UUID getId() {
        return course_id;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getName() {
        return courseName;
    }
}
