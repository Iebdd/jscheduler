package project.scheduler.Tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity()
@Table(name = "Inscriptions")
public class Inscription {

  @Id
  @Column(unique = true)
  private InscriptionsId inscriptions_id;  // Unique identifier for the join table entry

  public Inscription(User user, Course course) {
    this.inscriptions_id = new InscriptionsId(user, course);
  }

  public Inscription() {}

  public InscriptionsId getId() {
    return inscriptions_id;
  }

  public void setId(User user_id, Course course_id) {
    this.inscriptions_id = new InscriptionsId(user_id, course_id);
  }

}
