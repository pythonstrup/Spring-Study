package com.test.thejavatest.domain;

import com.test.thejavatest.study.StudyStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Study {

  @Id
  @GeneratedValue
  private Long id;
  private StudyStatus status = StudyStatus.DRAFT;
  private int peopleLimit;
  private Long ownerId;

  private String name;

  public Study() {}

  public Study(int peopleLimit) {
    if (peopleLimit <= 0) {
      throw new IllegalArgumentException("limit은 0보다 커야 한다.");
    }
    this.peopleLimit = peopleLimit;
  }

  public Study(int peopleLimit, String name) {
    this.peopleLimit = peopleLimit;
    this.name = name;
  }

  public StudyStatus getStatus() {
    return status;
  }

  public int getPeopleLimit() {
    return peopleLimit;
  }

  public String getName() {
    return name;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  @Override
  public String toString() {
    return "Study{" +
        "status=" + status +
        ", limit=" + peopleLimit +
        ", name='" + name + '\'' +
        '}';
  }

  public void setOwner(Member member) {
    this.ownerId = member.getId();
  }
}
