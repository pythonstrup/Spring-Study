package sample.cafekiosk.spring.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findBySellingTypeIn(List<ProductSellingType> sellingTypes);

  List<Product> findByProductNumberIn(List<String> productNumbers);

  @Query(value = "select p.product_number from product p order by p.id desc limit 1", nativeQuery = true)
  String findLatestProduct();
}
