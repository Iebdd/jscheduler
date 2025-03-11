package project.scheduler;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity()
@Table(name="users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(nullable = false)  // Ensure 'role' is not null in the database
  private Integer role;

  @Column(nullable = false)  // Ensure 'name' is not null in the database
  private String name;

  @OneToOne(mappedBy = "user")
  private UserCourse userCourse;

  // Default constructor
  public User() {}

  // Getters and setters
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
