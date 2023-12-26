package com.pythonstrup.ddd.order.command.domain;

import com.pythonstrup.ddd.common.model.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShippingInfo {

  private Receiver receiver;
  private Address address;

  public ShippingInfo(Receiver receiver, Address address) {
    this.receiver = receiver;
    this.address = address;
  }
}
