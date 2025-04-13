package project.scheduler.Tables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity()
@Table(name="User")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JsonProperty(index = 1)
  private UUID user_id;

  @Column(nullable = false)
  private Integer role;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false) 
  private String lastName;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique=true)
  private String email;

  
  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(
    name = "Inscription",
    joinColumns = {@JoinColumn(name = "i_user_id")},
    inverseJoinColumns = {@JoinColumn(name = "i_course_id")}
  )
  @SuppressWarnings("unused")
  private final Set<Course> course = new HashSet<>();

  @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
  @SuppressWarnings("unused")
  private final List<Token> token = new ArrayList<>();


  // Default constructor
  public User() {}

  public User(Integer role, String first_name, String last_name, String password, String email) {
    this.role = role;
    this.firstName = first_name;
    this.lastName = last_name;
    this.password = password;
    this.email = email;
  }

  @JsonIgnore
  public UUID getUserId() {
    return this.user_id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public Integer getRole() {
    return this.role;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return this.password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return this.email;
  }
}
