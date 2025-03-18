package project.scheduler.Tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "UserCourse")
public class UserCourse {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;  // Unique identifier for the join table entry

  @ManyToOne  // Many UserCourses can correspond to one User
  @JoinColumn(name = "user_id", referencedColumnName = "id")  // Foreign key to User
  private User user;

  @ManyToOne  // Many UserCourses can correspond to one Course
  @JoinColumn(name = "ucourse_id", referencedColumnName = "id")  // Foreign key to Course
  private Course course;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }
}
