package com.hello;

import java.util.Date;
import javax.persistence.*;

@Entity
// @Table(name = "MBR")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // ex) MySQL autoincrement
  private String id;

  @Column(name = "name",
      nullable = false,
      columnDefinition = "varchar(100) default 'EMPTY'")
  private String username;

  public Member() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
