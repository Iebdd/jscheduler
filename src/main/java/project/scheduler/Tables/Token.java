package project.scheduler.Tables;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Tokens")
public class Token {
   
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name="id")
  private Integer id;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  private Instant expiry;
  
  @SuppressWarnings("unused")
  @ManyToOne
  @JoinColumn(nullable = false, name="user_id")
  private User user;

  public Token() {}

  public Token(User id, String token, Instant expiry) {
    this.user = id;
    this.token = token;
    this.expiry = expiry;
  }

  public Token(User id, String token) {
    this.user = id;
    this.token = token;
    this.expiry = Instant.now().plusSeconds(604800); // Sets the expiry one week from now
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setExpiry(Instant expiry) {
    this.expiry = expiry;
  } 

  public Instant getExpiry() {
    return expiry;
  }
}
