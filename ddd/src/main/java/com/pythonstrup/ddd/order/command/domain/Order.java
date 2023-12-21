package com.pythonstrup.ddd.order.command.domain;

import com.pythonstrup.ddd.common.model.Money;
import java.util.List;

public class Order {

  private OrderState state;
  private ShippingInfo shippingInfo;
  private List<OrderLine> orderLineList;
  private Money totalAmounts;

  public Order(ShippingInfo shippingInfo, List<OrderLine> orderLineList) {
    setOrderLineList(orderLineList);
    setShippingInfo(shippingInfo);
  }

  public void changeShippingInfo(ShippingInfo newShippingInfo) {
    verifyNotYetShipped();
    setShippingInfo(newShippingInfo);
  }

  public void cancel() {
    verifyNotYetShipped();
    this.state = OrderState.CANCELED;
  }

  private void verifyNotYetShipped() {
    if (state != OrderState.PAYMENT_WAITING && state != OrderState.PREPARING) {
      throw new IllegalStateException("already shipped");
    }
  }

  private void setShippingInfo(ShippingInfo shippingInfo) {
    if (shippingInfo == null) {
      throw new IllegalArgumentException("no ShippingInfo");
    }
    this.shippingInfo = shippingInfo;
  }

  private void setOrderLineList(List<OrderLine> orderLineList) {
    verifyAtLeastOneOrMoreOrderLineList(orderLineList);
    this.orderLineList = orderLineList;
    calculateTotalAmounts();
  }

  private void verifyAtLeastOneOrMoreOrderLineList(List<OrderLine> orderLineList) {
    if (orderLineList == null || orderLineList.isEmpty()) {
      throw new IllegalArgumentException("no OrderLine");
    }
  }

  private void calculateTotalAmounts() {
    int sum = orderLineList.stream()
        .mapToInt(x -> x.getAmounts())
        .sum();
    this.totalAmounts = new Money(sum);
  }
}
