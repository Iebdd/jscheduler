package project.scheduler.Tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity()
@Table(name = "Inscription")
public class Inscription {

  @Id
  @Column(unique = true)
  private InscriptionId inscription_id;  // Unique identifier for the join table entry

  public Inscription(User user, Course course) {
    this.inscription_id = new InscriptionId(user, course);
  }

  public Inscription() {}

  public InscriptionId getId() {
    return inscription_id;
  }

  public void setId(User user_id, Course course_id) {
    this.inscription_id = new InscriptionId(user_id, course_id);
  }

}
