package project.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String salt;

  @Column(nullable = false, unique=true)
  private String email;

  
  @ManyToMany //(mappedBy = "user")
  @SuppressWarnings("unused")
  private final Set<Course> course = new HashSet<>();

  @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
  @SuppressWarnings("unused")
  private final List<Token> token = new ArrayList<>();


  // Default constructor
  public User() {}

  public User(Integer role, String name, String password, String salt, String email) {
    this.role = role;
    this.name = name;
    this.password = password;
    this.salt = salt;
    this.email = email;
  }

  // Getters and setters
  public Integer getId() {
    return id;
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

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getSalt() {
    return salt;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }
}
