package sample.cafekiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;

/**
 * readOnly = true: 읽기전용
 * CRUD에서 CUD 동작 X / only Read
 * JPA: CUD 스냅샷 저장, 변경감지 X (성능 향상)
 *
 * CQRS - Command / Read
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  // 동시성 이슈 => 해결? UUID를 사용하면 될듯?
  @Transactional
  public ProductResponse createProduct(ProductCreateServiceRequest request) {
    String nextProductNumber = createNextProductNumber();
    Product product = request.toEntity(nextProductNumber);
    Product savedProduct = productRepository.save(product);
    return ProductResponse.of(savedProduct);
  }

  public List<ProductResponse> getSellingProducts() {
    return productRepository.findBySellingTypeIn(ProductSellingType.forDisplay())
        .stream()
        .map(ProductResponse::of)
        .collect(Collectors.toList());
  }

  private String createNextProductNumber() {
    String latestProductNumber = productRepository.findLatestProduct();
    if (latestProductNumber == null) {
      return "001";
    }

    Integer latestProductNumberInt = Integer.parseInt(latestProductNumber);
    int nextProductNumberInt = latestProductNumberInt + 1;

    return String.format("%03d", nextProductNumberInt);
  }
}
