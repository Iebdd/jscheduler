package project.scheduler.Tables;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity(name="Course")
public class Course {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String name;

    @SuppressWarnings("unused")
    @ManyToMany(mappedBy = "course")  // Match property in UserCourse
    private final Set<User> user = new HashSet<>(); // Relationship with UserCourse

    @SuppressWarnings("unused")
    @OneToMany(mappedBy = "course")
    private final Set<Bookings> roomCourse = new HashSet<>();

/*     @ManyToMany(mappedBy = "course")  // Match property in RoomCourse
    private RoomCourse roomCourse; // Relationship with RoomCourse */

    // Default constructor
    public Course() {}

    public Course(String name) {
        this.name = name;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
