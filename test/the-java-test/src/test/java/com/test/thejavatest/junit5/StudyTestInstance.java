package com.test.thejavatest.junit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

// 기본 테스트 인스턴스 생성 전략은 메소드마다 인스턴스를 생성하는 전략이다.
@TestInstance(Lifecycle.PER_CLASS) // 하지만 이와 같이 설정해주면 클래스마다 인스턴스를 생성하게 된다.
class StudyTestInstance {

  int value = 1;

  @Test
  @DisplayName("인스턴스1")
  void instance1() {
    System.out.println(value++);
    System.out.println(this);
  }

  @Test
  @DisplayName("인스턴스2")
  void instance2() {
    System.out.println(value++);
    System.out.println(this);
  }
}