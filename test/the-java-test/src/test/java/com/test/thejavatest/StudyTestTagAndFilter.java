package com.test.thejavatest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class StudyTestTagAndFilter {

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  @Tag("fast")
  void create_new_study() {
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
  }

  @Test
  @DisplayName("스터디 만들기 \uD83D\uDE31")
  @Tag("slow")
  void create_new_study_again() {
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
  }
}