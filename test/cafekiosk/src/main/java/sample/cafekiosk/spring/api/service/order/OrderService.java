package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final StockRepository stockRepository;

  /**
   * 재고 감소 -> 동시성 고민
   * optimistic lock / pessimistic lock / ...
   */
  public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredAt) {
    List<String> productNumbers = request.productNumbers();
    List<Product> products = findProductsBy(productNumbers);

    deductStockQuantities(productNumbers, products);

    Order order = Order.create(products, registeredAt);
    Order savedOrder = orderRepository.save(order);
    return OrderResponse.of(savedOrder);
  }

  private List<Product> findProductsBy(List<String> productNumbers) {
    List<Product> products = productRepository.findByProductNumberIn(productNumbers);
    Map<String, Product> productMap = products.stream()
        .collect(Collectors.toMap(product -> product.getProductNumber(), p -> p));
    List<Product> duplicateProducts = productNumbers.stream()
        .map(productMap::get)
        .collect(Collectors.toList());
    return duplicateProducts;
  }

  private void deductStockQuantities(List<String> productNumbers, List<Product> products) {
    List<String> stockProductNumbers = extractStockProductNumbers(products);

    Map<String, Stock> stockMap = createStockMapBy(productNumbers);

    Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

    for (String stockProductNumber: new HashSet<>(stockProductNumbers)) {
      Stock stock = stockMap.get(stockProductNumber);
      int quantity = productCountingMap.get(stockProductNumber).intValue();
      if (stock.isQuantityLessThan(quantity)) {
        throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
      }
      stock.deductQuantity(quantity);
    }
  }

  private Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
    return stockProductNumbers.stream()
        .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
  }

  private Map<String, Stock> createStockMapBy(List<String> productNumbers) {
    List<Stock> stocks = stockRepository.findByProductNumberIn(productNumbers);
    Map<String, Stock> stockMap = stocks.stream()
        .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
    return stockMap;
  }

  private List<String> extractStockProductNumbers(List<Product> products) {
    return products.stream()
        .filter(product -> ProductType.containsStockType(product.getType()))
        .map(Product::getProductNumber)
        .collect(Collectors.toList());
  }
}
