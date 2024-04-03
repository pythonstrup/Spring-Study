package com.test.thejavatest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StudyRepeatedTest {

  @DisplayName("스터디 만들기")
  @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
  void repeatTest(RepetitionInfo repetitionInfo) {
    System.out.println(
        String.format(
            "%d/%d",
            repetitionInfo.getCurrentRepetition(),
            repetitionInfo.getTotalRepetitions()));
  }

  @DisplayName("스터디 만들기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요."})
  void parameterizedTest(String message) {
    System.out.println(message);
  }
}