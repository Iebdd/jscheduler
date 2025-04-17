package project.scheduler.Tables;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Class representation of Token entries
 */
@Entity
@Table(name="Tokens")
public class Token {
   
  @Id
  @GeneratedValue(strategy=GenerationType.UUID)
  @Column(name="token_id")
  private UUID token_id;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  private Instant expiry;
  
  @SuppressWarnings("unused")
  @ManyToOne
  @JoinColumn(nullable = false, name="t_user_id")
  private User user;

  /**
   * Default constructor
   */
  public Token() {}

  /**
   * Full constructor
   * 
   * @param id      The user referenced
   * @param token   The token to be used
   * @param expiry  The expiry of the token
   */
  public Token(User id, String token, Instant expiry) {
    this.user = id;
    this.token = token;
    this.expiry = expiry;
  }

  /**
   * Constructor with a predetermined expiry date (1 week)
   * 
   * @param id    The user referenced
   * @param token The token to be used
   */
  public Token(User id, String token) {
    this.user = id;
    this.token = token;
    this.expiry = Instant.now().plusSeconds(604800); // Sets the expiry one week from now
  }

  /**
   * Getter for token id
   * 
   * @return  The instance id
   */
  public UUID getId() {
    return token_id;
  }

  /**
   * Getter for token
   * 
   * @return  The instance token
   */
  public String getToken() {
    return token;
  }

  /**
   * Setter for the expiry
   * 
   * @param expiry  The expiry to be set
   */
  public void setExpiry(Instant expiry) {
    this.expiry = expiry;
  } 

  /**
   * Setter for the expiry
   * 
   * @return  The instanc expiry
   */
  public Instant getExpiry() {
    return expiry;
  }
}
