package com.test.thejavatest.junit5;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.test.thejavatest.Study;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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

  @DisplayName("스터디 만들기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요."})
//  @EmptySource // 비어 있는 문자열을 넣어줌
//  @NullSource // null을 넣어줌
  @NullAndEmptySource // 둘 다 넣어줌
  void parameterizedTestNullAndEmptySource(String message) {
    System.out.println(message);
  }

  @DisplayName("스터디 만들기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @ValueSource(ints = {10, 20, 40})
  void parameterizedTestValueSourceInteger(@ConvertWith(StudyConverter.class) Study study) {
    System.out.println(study.getLimit());
  }

  static class StudyConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType)
        throws ArgumentConversionException {
      assertEquals(Study.class, targetType, "Can only convert to Study");
      return new Study(Integer.parseInt(source.toString()));
    }
  }

  @DisplayName("스터디 만들기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @CsvSource({"10, 'java'", "20, spring"})
  void parameterizedTestCsv(Integer limit, String name) {
    System.out.println(new Study(limit, name));
  }

  @DisplayName("스터디 만들기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @CsvSource({"10, 'java'", "20, spring"})
  void parameterizedTestCsvWithArgumentsAccessor(ArgumentsAccessor argumentsAccessor) {
    Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(0));
    System.out.println(study);
  }

  @DisplayName("스터디 만들기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @CsvSource({"10, 'java'", "20, spring"})
  void parameterizedTestCsvWithAggregator(@AggregateWith(StudyAggregator.class) Study study) {
    System.out.println(study);
  }

  static class StudyAggregator implements ArgumentsAggregator {

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context)
        throws ArgumentsAggregationException {
      return new Study(accessor.getInteger(0), accessor.getString(0));
    }
  }
}