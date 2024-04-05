package com.test.thejavatest.domain;

import com.test.thejavatest.study.StudyStatus;

public class Study {

  private StudyStatus status = StudyStatus.DRAFT;

  private int limit;

  private String name;

  public Study() {}

  public Study(int limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException("limit은 0보다 커야 한다.");
    }
    this.limit = limit;
  }

  public Study(int limit, String name) {
    this.limit = limit;
    this.name = name;
  }

  public StudyStatus getStatus() {
    return status;
  }

  public int getLimit() {
    return limit;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Study{" +
        "status=" + status +
        ", limit=" + limit +
        ", name='" + name + '\'' +
        '}';
  }

  public void setOwner(Member member) {

  }
}