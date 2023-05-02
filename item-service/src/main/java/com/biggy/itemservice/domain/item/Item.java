package com.biggy.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

// @Data => 굉장히 위험하기 때문에 조심하자. 어떻게 동작할 지 예상할 수 없기 때문에....
@Getter @Setter
public class Item {

  private Long id;
  private String itemName;
  private Integer price;
  private Integer quantity;

  public Item() {}

  public Item(String itemName, Integer price, Integer quantity) {
    this.itemName = itemName;
    this.price = price;
    this.quantity = quantity;
  }
}
