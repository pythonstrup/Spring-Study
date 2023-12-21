package com.pythonstrup.ddd.order.command.domain;

import com.pythonstrup.ddd.catalog.command.domain.product.Product;
import lombok.Getter;

@Getter
public class OrderLine {

  private Product product;
  private int price;
  private int quantity;
  private int amounts;

  public OrderLine(Product product, int price, int quantity) {
    this.product = product;
    this.price = price;
    this.quantity = quantity;
    this.amounts = calculateAmounts();
  }

  private int calculateAmounts() {
    return price * quantity;
  }


}
