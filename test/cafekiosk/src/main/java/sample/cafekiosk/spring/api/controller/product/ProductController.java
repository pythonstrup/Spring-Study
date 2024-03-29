package sample.cafekiosk.spring.api.controller.product;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

  public final ProductService productService;

  @PostMapping("/new")
  public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
    return ApiResponse.of(productService.createProduct(request.toService()));
  }

  @GetMapping("/selling")
  public ApiResponse<List<ProductResponse>> getSellingProducts() {
    return ApiResponse.of(productService.getSellingProducts());
  }
}
