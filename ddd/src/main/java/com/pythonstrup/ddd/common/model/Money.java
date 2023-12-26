package com.pythonstrup.ddd.common.model;

import java.util.Objects;
import lombok.Getter;

@Getter
public class Money {

  private int value;

  public Money(int value) {
    this.value = value;
  }

  public Money add(Money money) {
    return new Money(this.value + money.value);
  }

  public Money multiply(int multiplier) {
    return new Money(value * multiplier);
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Money money = (Money) o;
    return value == money.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
