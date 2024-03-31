package sample.cafekiosk.spring.api.controller.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.ControllerTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

class ProductControllerTest extends ControllerTestSupport {

  @Test
  @DisplayName("신규 상품을 등록한다.")
  public void createProduct() throws Exception {
    // given
    ProductCreateRequest request = new ProductCreateRequest(HANDMADE, SELLING, "아메리카노", 4000);

    // when

    // then
    mockMvc.perform(post("/api/v1/products/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("200"))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("OK"));
  }

  @Test
  @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
  public void createProductWithoutType() throws Exception {
    // given
    ProductCreateRequest request = new ProductCreateRequest(null, SELLING, "아메리카노", 4000);

    // when

    // then
    mockMvc.perform(post("/api/v1/products/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("신규 상품을 등록할 때 판매상태는 필수값이다.")
  public void createProductWithoutSellingType() throws Exception {
    // given
    ProductCreateRequest request = new ProductCreateRequest(HANDMADE, null, "아메리카노", 4000);

    // when

    // then
    mockMvc.perform(post("/api/v1/products/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("신규 상품을 등록할 때 이름은 필수값이다.")
  public void createProductWithoutName() throws Exception {
    // given
    ProductCreateRequest request = new ProductCreateRequest(HANDMADE, SELLING, null, 4000);

    // when

    // then
    mockMvc.perform(post("/api/v1/products/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("신규 상품을 등록할 때 가격은 양수값이다.")
  public void createProductWithZeroPrice() throws Exception {
    // given
    ProductCreateRequest request = new ProductCreateRequest(HANDMADE, SELLING, "아메리카노", 0);

    // when

    // then
    mockMvc.perform(post("/api/v1/products/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("판매 상품을 조회한다.")
  public void getSellingProducts() throws Exception {
    // given
    List<ProductResponse> result = List.of();

    // when
    Mockito.when(productService.getSellingProducts()).thenReturn(result);

    // then
    mockMvc.perform(get("/api/v1/products/selling"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("200"))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("OK"))
        .andExpect(jsonPath("$.data").isArray());
  }
}