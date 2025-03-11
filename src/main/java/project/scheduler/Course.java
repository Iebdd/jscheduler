package project.scheduler;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity(name="Course")
public class Course {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToOne(mappedBy = "course")  // Match property in UserCourse
    private UserCourse userCourse; // Relationship with UserCourse

    @OneToOne(mappedBy = "course")  // Match property in RoomCourse
    private RoomCourse roomCourse; // Relationship with RoomCourse

    // Default constructor
    public Course() {}

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
