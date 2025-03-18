package project.scheduler.Tables;

import jakarta.persistence.Embeddable;

@Embeddable
public class RoomCourseId {

    private Long roomId;
    private Long courseId;

    public RoomCourseId() {}

    public RoomCourseId(Long roomId, Long courseId) {
        super();
        this.roomId = roomId;
        this.courseId = courseId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

}
