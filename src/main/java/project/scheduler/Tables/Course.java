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

/**
 * Class representation of Course entries
 */
@Entity(name="Courses")
public class Course {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID course_id;

    private String courseName;

    @SuppressWarnings("unused")
    @JsonIgnore
    @ManyToMany(mappedBy = "course")
    private final Set<User> user = new HashSet<>();

    @SuppressWarnings("unused")
    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private final Set<Booking> roomCourse = new HashSet<>();


    // Default constructor
    public Course() {}

    /**
     * Full Constructor
     * 
     * @param courseName The name of the course
     */
    public Course(String courseName) {
        this.courseName = courseName;
    }


    /**
     * Getter for course id
     * 
     * @return  The instance course id
     */
    public UUID getId() {
        return course_id;
    }

    /**
     * Setter for course name
     * 
     * @param courseName    The new course name
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     *  Getter for course name
     * 
     * @return The instance course name
     */
    public String getCourseName() {
        return courseName;
    }
}
