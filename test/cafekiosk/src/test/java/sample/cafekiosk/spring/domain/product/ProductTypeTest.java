package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ProductTypeTest {

  @Test
  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  public void containsStockType() {
    // given
    ProductType givenType = ProductType.HANDMADE;

    // when
    boolean result = ProductType.containsStockType(givenType);

    // then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  public void containsStockType2() {
    // given
    ProductType givenType = ProductType.BAKERY;

    // when
    boolean result = ProductType.containsStockType(givenType);

    // then
    assertThat(result).isTrue();
  }

  // @ParameterizedTest를 사용하지 않는 경우 테스트가 굉장히 길어진다...
  @Test
  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  public void containsStockType3() {
    // given
    ProductType givenType1 = ProductType.HANDMADE;
    ProductType givenType2 = ProductType.BOTTLE;
    ProductType givenType3 = ProductType.BAKERY;

    // when
    boolean result1 = ProductType.containsStockType(givenType1);
    boolean result2 = ProductType.containsStockType(givenType2);
    boolean result3 = ProductType.containsStockType(givenType3);

    // then
    assertThat(result1).isFalse();
    assertThat(result2).isTrue();
    assertThat(result3).isTrue();
  }

  /**
   * @ParameterizedTest 예시들
   * 1. CsvSource 활용 (@CsvSource)
   * 2. Stream<Arguments>을 반환하는 메소드 활용 (@MethodSource)
   *
   * 파라미터를 전달하는 어노테이션은
   * @ValueSource,
   * @NullSource,
   * @EmptySource,
   * @NullAndEmptySource,
   * @EnumSource
   * @CsvSource
   * @MethodSource
   * 등이 있다.
   */

  @ParameterizedTest
  @CsvSource({"HANDMADE,false", "BOTTLE,true", "BAKERY,true"})
  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다. @CsvSource")
  public void containsStockType4(ProductType productType, boolean expected) {
    // when
    boolean result = ProductType.containsStockType(productType);

    // then
    assertThat(result).isEqualTo(expected);
  }

  private static Stream<Arguments> provideProductTypesForCheckingStockType() {
    return Stream.of(
        Arguments.arguments(ProductType.HANDMADE, false),
        Arguments.arguments(ProductType.BOTTLE, true),
        Arguments.arguments(ProductType.BAKERY, true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideProductTypesForCheckingStockType")
  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다. @MethodSource")
  public void containsStockType5(ProductType productType, boolean expected) {
    // when
    boolean result = ProductType.containsStockType(productType);

    // then
    assertThat(result).isEqualTo(expected);
  }
}