package com.test.thejavatest.junit5;

import static org.assertj.core.api.Assertions.assertThat;

import com.test.thejavatest.domain.Study;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class StudyTestTagAndFilter {

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  @Tag("fast")
  void create_new_study() {
    Study study = new Study(10);
    assertThat(study.getPeopleLimit()).isGreaterThan(0);
  }

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  @Tag("slow")
  void create_new_study_again() {
    Study study = new Study(10);
    assertThat(study.getPeopleLimit()).isGreaterThan(0);
  }
}