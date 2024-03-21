package sample.cafekiosk.spring.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findBySellingTypeIn(List<ProductSellingType> sellingTypes);

  List<Product> findByProductNumberIn(List<String> productNumbers);
}
