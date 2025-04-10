package project.scheduler.Tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "UserCourse")
public class UserCourse {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UserCourseId usercourseid;  // Unique identifier for the join table entry

  public UserCourse(User user, Course course) {
    this.usercourseid = new UserCourseId(user, course);
  }

  public UserCourse() {}

  public UserCourseId getId() {
    return usercourseid;
  }

  public void setId(User user_id, Course course_id) {
    this.usercourseid = new UserCourseId(user_id, course_id);
  }

}
