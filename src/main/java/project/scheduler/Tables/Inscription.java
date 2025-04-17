package project.scheduler.Tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Class representation of Inscription entries
 */
@Entity()
@Table(name = "Inscriptions")
public class Inscription {

  @Id
  @Column(unique = true)
  private InscriptionsId inscriptions_id;  // Unique identifier for the join table entry

  /**
   * Full constructor for Inscription
   * 
   * @param user    The user referenced
   * @param course  The course referenced
   */
  public Inscription(User user, Course course) {
    this.inscriptions_id = new InscriptionsId(user, course);
  }

  /**
   * Default constructor
   */
  public Inscription() {}

  /**
   * Getter for inscription id
   * 
   * @return  The composite inscription id
   */
  public InscriptionsId getId() {
    return inscriptions_id;
  }

}
