package project.scheduler.Tables;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Embeddable class representing the composite id of isncription
 */
@Embeddable
public class InscriptionsId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "i_user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "i_course_id", referencedColumnName = "course_id")
    private Course course;

    /**
     * Default constructor
     */
    public InscriptionsId() {};

    /**
     * Full constructor
     * 
     * @param user      The user referenced
     * @param course    The course referenced
     */
    public InscriptionsId(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    /**
     * Getter for course id
     * 
     * @return  The course id referenced
     */
    public Course getCourseId() {
        return this.course;
    }

    /**
     * Getter for user id
     * 
     * @return  The user id referenced
     */
    public User getUserId() {
        return this.user;
    }
    
}
