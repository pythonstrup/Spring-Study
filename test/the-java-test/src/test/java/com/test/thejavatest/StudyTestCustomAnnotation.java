package com.test.thejavatest;

import static org.assertj.core.api.Assertions.assertThat;

import com.test.thejavatest.annotation.FastTest;
import com.test.thejavatest.annotation.SlowTest;
import org.junit.jupiter.api.DisplayName;

class StudyTestCustomAnnotation {

  @FastTest
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  void create_new_study() {
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
  }

  @SlowTest
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  void create_new_study_again() {
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
  }
}