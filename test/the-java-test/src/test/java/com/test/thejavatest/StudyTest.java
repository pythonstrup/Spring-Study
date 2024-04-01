package com.test.thejavatest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

// 메소드 이름 표기 전략을 설정할 수 있음 => 그러나 그냥 @DisplayName을 사용하는 것을 권장
// @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

  @BeforeAll
  static void beforeAll() {
    System.out.println("\nbeforeAll\n");
  }

  @AfterAll
  static void afterAll() {
    System.out.println("\nafterAll\n");
  }

  @BeforeEach
  void setUp() {
    System.out.println("\nbeforeEach");
  }

  @AfterEach
  void tearDown() {
    System.out.println("afterEach\n");
  }

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  void create_new_study() {
    Study study = new Study();
    assertNotNull(study);
    System.out.println("create1");
  }

  @Test
  void create2() {
    Study study = new Study();
    assertNotNull(study);
    System.out.println("create2");
  }

  // @Disabled
  // 깨진 테스트에 사용하는 것은 좋지 않다. 깨진 테스트는 바로 고치는 것이 좋다.
  // 더 이상 관리되지 않는 테스트에서 사용
  @Test
  @Disabled
  void create3() {
    Study study = new Study();
    assertNotNull(study);
    System.out.println("create3");
  }
}