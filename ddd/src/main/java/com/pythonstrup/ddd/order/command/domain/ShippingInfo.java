package com.pythonstrup.ddd.order.command.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShippingInfo {

  private String receiverName;
  private String receiverPhoneNumber;
  private String shippingAddress1;
  private String shippingAddress2;
  private String shippingZipcode;

  public ShippingInfo(String receiverName, String receiverPhoneNumber, String shippingAddress1,
      String shippingAddress2, String shippingZipcode) {
    this.receiverName = receiverName;
    this.receiverPhoneNumber = receiverPhoneNumber;
    this.shippingAddress1 = shippingAddress1;
    this.shippingAddress2 = shippingAddress2;
    this.shippingZipcode = shippingZipcode;
  }
}
