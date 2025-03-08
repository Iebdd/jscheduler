package project.scheduler;

import jakarta.persistence.OneToOne;

public class RoomCourse {

  @OneToOne(mappedBy="room_id")
  private User room_id;

  @OneToOne(mappedBy="rcourse_id")
  private Course course_id;

  public User getUserId() {
    return room_id;
  }

  public void setUserId(User user_id) {
    this.room_id = user_id;
  }

  public void setCourseId(Course course_id) {
    this.course_id = course_id;
  }

  public Course getCourseId() {
    return course_id;
  }
}
