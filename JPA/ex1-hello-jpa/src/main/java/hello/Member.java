package hello;

import java.util.Date;
import javax.persistence.*;

@Entity
// @Table(name = "MBR")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // ex) MySQL autoincrement
  private Long id;

  @Column(name = "name",
      nullable = false,
      columnDefinition = "varchar(100) default 'EMPTY'")
  private String username;

  public Member() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
