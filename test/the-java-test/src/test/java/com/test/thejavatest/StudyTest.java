package com.test.thejavatest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StudyTest {

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  void create_new_study() {
    Study study = new Study(10);
    assertAll(
        () -> assertNotNull(study),
        () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값이 DRAFT여야 한다."),
        () -> assertTrue(study.getLimit() > 0, () -> "스터디의 최대 참석 가능 인원은 0보다 커야 한다.")
    );
  }

  @Test
  @DisplayName("시간 안에 스터디 만들기 \uD83D\uDE31")
  void create_timeout() {
    assertTimeout(Duration.ofSeconds(10), () -> new Study(10));
    assertTimeoutPreemptively(Duration.ofSeconds(10), () -> new Study(10));
  }

  @Test
  @DisplayName("스터디 만들기 에러 \uD83D\uDE31")
  void create_error() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Study(-10));
    String message = exception.getMessage();
    assertEquals("limit은 0보다 커야 한다.", message);
  }
}