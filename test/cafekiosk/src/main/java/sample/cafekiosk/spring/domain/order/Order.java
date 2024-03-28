package sample.cafekiosk.spring.domain.order;

import static jakarta.persistence.CascadeType.ALL;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.Product;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  private int totalPrice;

  private LocalDateTime registeredAt;

  @OneToMany(mappedBy = "order", cascade = ALL)
  private List<OrderProduct> orderProducts = new ArrayList<>();

  @Builder
  public Order(List<Product> products, OrderStatus orderStatus, LocalDateTime registeredAt) {
    this.orderStatus = orderStatus;
    this.totalPrice = calculateTotalPrice(products);
    this.registeredAt = registeredAt;
    this.orderProducts = products.stream()
        .map(product -> new OrderProduct(this, product))
        .collect(Collectors.toList());
  }

  private int calculateTotalPrice(List<Product> products) {
    return products.stream().mapToInt(Product::getPrice).sum();
  }

  public static Order create(List<Product> products, LocalDateTime registeredAt) {
    return new Order(products, OrderStatus.INIT, registeredAt);
  }
}
