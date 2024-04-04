package com.test.thejavatest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = OrderAnnotation.class)
class StudyTestOrder {

  int value = 1;

  @Test
  @Order(1)
  @DisplayName("인스턴스1")
  void instance1() {
    System.out.println(value++);
    System.out.println(this);
  }

  @Test
  @Order(3)
  @DisplayName("인스턴스2")
  void instance2() {
    System.out.println(value++);
    System.out.println(this);
  }

  @Test
  @Order(2)
  @DisplayName("인스턴스3")
  void instance3() {
    System.out.println(value++);
    System.out.println(this);
  }

  @Test
  @Order(1)
  @DisplayName("인스턴스4")
  void instance4() {
    System.out.println(value++);
    System.out.println(this);
  }
}