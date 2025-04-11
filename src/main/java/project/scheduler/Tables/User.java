package project.scheduler.Tables;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity()
@Table(name="User")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer user_id;

  @Column(nullable = false)
  private Integer role;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique=true)
  private String email;

  
  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(
    name = "Inscription",
    joinColumns = {@JoinColumn(name = "user_id")},
    inverseJoinColumns = {@JoinColumn(name = "course_id")}
  )
  @SuppressWarnings("unused")
  private final Set<Course> course = new HashSet<>();

  @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
  @SuppressWarnings("unused")
  private final List<Token> token = new ArrayList<>();


  // Default constructor
  public User() {}

  public User(Integer role, String name, String password, String email) {
    this.role = role;
    this.name = name;
    this.password = password;
    this.email = email;
  }

  public Integer getId() {
    return user_id;
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

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }
}
