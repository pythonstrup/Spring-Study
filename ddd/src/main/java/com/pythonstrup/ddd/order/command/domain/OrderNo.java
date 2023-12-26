package com.pythonstrup.ddd.order.command.domain;

import jakarta.persistence.Column;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderNo {

  @Column(name = "order_number")
  private String number;

  public OrderNo(String number) {
    this.number = number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderNo orderNo = (OrderNo) o;
    return Objects.equals(number, orderNo.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number);
  }
}
