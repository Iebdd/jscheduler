package project.scheduler;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumns;

@Entity(name="Course")
public class Course {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @OneToOne()
    @PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name="ucourse_id", referencedColumnName="ucourse_id"),
        @PrimaryKeyJoinColumn(name="rcourse_id", referencedColumnName="rcourse_id")})
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
      }
    
      public void setId(Integer id) {
        this.id = id;
      }
    
      public void setRole(String name) {
        this.name = name;
      }
    
      public String getName() {
        return name;
      }
    
}
