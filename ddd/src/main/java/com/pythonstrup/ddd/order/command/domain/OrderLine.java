package com.pythonstrup.ddd.order.command.domain;

import com.pythonstrup.ddd.catalog.command.domain.product.Product;
import com.pythonstrup.ddd.common.model.Money;
import lombok.Getter;

@Getter
public class OrderLine {

  private Product product;
  private Money price;
  private int quantity;
  private Money amounts;

  public OrderLine(Product product, Money price, int quantity) {
    this.product = product;
    // Money가 불변 객체가 아니라면, price 파라미터가 변경될 때 발생하는 문제를 방지하기 위해 깊은 복사를 해야 한다.
    this.price = new Money(price.getValue());
    this.quantity = quantity;
    this.amounts = calculateAmounts();
  }

  private Money calculateAmounts() {
    return price.multiply(quantity);
  }


}
