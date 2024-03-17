package sample.cafekiosk.unit.beverage;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AmericanoTest {

  @Test
  void getName() {
    Americano americano = new Americano();

//    assertEquals(americano.getName(), "아메리카노"); // JUnit
    Assertions.assertThat(americano.getName()).isEqualTo("아메리카노"); // assertJ
  }

  @Test
  void getPrice() {
    Americano americano = new Americano();

    Assertions.assertThat(americano.getPrice()).isEqualTo(4000);
  }
}