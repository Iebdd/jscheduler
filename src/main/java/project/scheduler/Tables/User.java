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

/**
 * Class representation of User entries
 */
@Entity()
@Table(name="Users")
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
    name = "inscriptions",
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

  /**
   * Full constructor
   * 
   * @param role        The role to be set
   * @param first_name  The first name to be set
   * @param last_name   The last name to be set
   * @param password    The password to be set
   * @param email       The email to be set
   */
  public User(Integer role, String first_name, String last_name, String password, String email) {
    this.role = role;
    this.firstName = first_name;
    this.lastName = last_name;
    this.password = password;
    this.email = email;
  }

  /**
   * Getter for user id
   * 
   * @return  The instance user id
   */
  @JsonIgnore
  public UUID getUserId() {
    return this.user_id;
  }

  /**
   * Getter for first name
   * 
   * @return  The instance first name
   */
  public String getFirstName() {
    return this.firstName;
  }

  /**
   * Setter for first name
   * 
   * @param firstName The first name to be set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Getter for last name
   * 
   * @return  The instance last name
   */
  public String getLastName() {
    return this.lastName;
  }

  /**
   * Setter for last name
   * 
   * @param lastName  The last name to be set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Setter for role
   * 
   * @param role  The role to be set
   */
  public void setRole(Integer role) {
    this.role = role;
  }

  public Integer getRole() {
    return this.role;
  }

  /**
   * Setter for password
   * 
   * @param password  The password to be set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Getter for password
   * 
   * @return  The instance password
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Setter for email
   * 
   * @param email The email to be set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Getter for email
   * 
   * @return  The instance email
   */
  public String getEmail() {
    return this.email;
  }

  @Override
  public String toString() {
      StringBuilder new_string = new StringBuilder();
      new_string.append(String.format("{\"user_id\": \"%s\", \"email\": \"%s\", \"firstName\": \"%s\", \"lastName\": \"%s\", \"role\": \"%d\"}", 
                                              this.user_id, this.email, this.firstName, this.lastName, this.role));
      return new_string.toString();
  }

}
