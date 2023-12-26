package com.pythonstrup.ddd.order.command.domain;

import com.pythonstrup.ddd.common.model.Money;
import java.util.List;

public class Order {

  private OrderNo number;
  private OrderState state;
  private ShippingInfo shippingInfo;
  private Orderer orderer;
  private List<OrderLine> orderLineList;
  private Money totalAmounts;

  public Order(Orderer orderer, ShippingInfo shippingInfo, List<OrderLine> orderLineList, OrderState state) {
    setOrderer(orderer);
    setOrderLineList(orderLineList);
    setShippingInfo(shippingInfo);
    this.state = state;
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
        .mapToInt(x -> x.getAmounts().getValue())
        .sum();
    this.totalAmounts = new Money(sum);
  }

  private void setOrderer(Orderer orderer) {
    if (orderer == null) throw new IllegalArgumentException("no orderer");
    this.orderer = orderer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return number.equals(order.number);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((number == null)? 0: number.hashCode());
    return result;
  }
}
