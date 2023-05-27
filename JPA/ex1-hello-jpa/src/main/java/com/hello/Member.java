package com.hello;

import java.util.Date;
import javax.persistence.*;

@Entity
// @Table(name = "MBR")
public class Member {

  @Id
  private Long id;

  @Column(name = "name",
      nullable = false,
      columnDefinition = "varchar(100) default 'EMPTY'")
  private String username;

  private Integer age;

  @Enumerated(EnumType.STRING)
  private RoleType roleType;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModifiedDate;

  // DB에 적용하지 않고 메모리에서만 사용하고 싶을 때
  @Transient
  private int temp;

  @Lob
  private String description;

  public Member() {}

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

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public RoleType getRoleType() {
    return roleType;
  }

  public void setRoleType(RoleType roleType) {
    this.roleType = roleType;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public int getTemp() {
    return temp;
  }

  public void setTemp(int temp) {
    this.temp = temp;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
