package com.test.thejavatest.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Member {

  @Id
  @GeneratedValue
  private Long id;
  private String email;

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
