package project.scheduler;

import jakarta.persistence.OneToOne;

public class UserCourse {

  @OneToOne(mappedBy="user_id")
  private User user_id;

  @OneToOne(mappedBy="ucourse_id")
  private Course ucourse_id;

  public User getUserId() {
    return user_id;
  }

  public void setUserId(User user_id) {
    this.user_id = user_id;
  }

  public void setCourseId(Course ucourse_id) {
    this.ucourse_id = ucourse_id;
  }

  public Course getCourseId() {
    return ucourse_id;
  }
}
