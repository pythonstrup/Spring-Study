package com.test.thejavatest.junit5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import com.test.thejavatest.Study;
import com.test.thejavatest.StudyStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

class StudyTestAssume {

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  void create_new_study() {
    String testEnv = System.getenv("TEST_ENV");
    System.out.println(testEnv);
    assumeTrue("LOCAL".equalsIgnoreCase(testEnv));
    Study study = new Study(10);
    assertAll(
        () -> assertNotNull(study),
        () -> Assertions.assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값이 DRAFT여야 한다."),
        () -> assertTrue(study.getLimit() > 0, () -> "스터디의 최대 참석 가능 인원은 0보다 커야 한다.")
    );
  }

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  void create_new_study2() {
    String testEnv = System.getenv("TEST_ENV");

    assumingThat("LOCAL".equalsIgnoreCase(testEnv), () -> {
      System.out.println("local");
      Study study = new Study(10);
      assertThat(study.getLimit()).isGreaterThan(0);
    });
    assumingThat("bell".equalsIgnoreCase(testEnv), () -> {
      System.out.println("bell");
      Study study = new Study(10);
      assertThat(study.getLimit()).isGreaterThan(0);
    });
  }

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  @EnabledOnOs({OS.MAC, OS.LINUX})
  void create_new_study3() {
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
  }

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9, JRE.JAVA_10, JRE.JAVA_11, JRE.JAVA_17})
  void create_new_study4() {
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
  }

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  @EnabledOnJre(JRE.OTHER)
  void create_new_study5() {
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
  }

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
  void create_new_study6() {
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
  }
}