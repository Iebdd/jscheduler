package project.scheduler.Tables;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class InscriptionId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "i_user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "i_course_id", referencedColumnName = "course_id")
    private Course course;

    public InscriptionId() {};

    public InscriptionId(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    public Course getCourseId() {
        return this.course;
    }

    public User getUserId() {
        return this.user;
    }
    
}
