package project.scheduler;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name="User") // This tells Hibernate to make a table out of this class
public class User {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @OneToOne()
  @PrimaryKeyJoinColumn(name="user_id")
  private Integer id;

  private Integer role;

  private String name;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public Integer getRole() {
    return role;
  }
}
