package sample.cafekiosk.spring.docs.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import sample.cafekiosk.spring.api.controller.product.ProductController;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.docs.RestDocsSupport;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

public class ProductControllerDocsTest extends RestDocsSupport {

  @Mock
  final ProductService productService = mock(ProductService.class);

  @Override
  protected Object initController() {
    return new ProductController(productService);
  }

  @Test
  @DisplayName("신규 상품을 등록하는 API")
  void createProduct() throws Exception {
    // given
    ProductCreateRequest request = new ProductCreateRequest(HANDMADE, SELLING, "아메리카노", 4000);

    // when
    given(productService.createProduct(any(ProductCreateServiceRequest.class)))
        .willReturn(new ProductResponse(1L, "001", HANDMADE, SELLING, "아메리카노", 4000));

    // then
    mockMvc.perform(post("/api/v1/products/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("200"))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("OK"))
        .andDo(document("product-create",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("type").type(JsonFieldType.STRING)
                    .description("상품 타입"),
                fieldWithPath("sellingType").type(JsonFieldType.STRING)
                    .optional()
                    .description("상품 판매상태"),
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("상품 이름"),
                fieldWithPath("price").type(JsonFieldType.NUMBER)
                    .description("상품 가격")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER)
                    .description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING)
                    .description("상태"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("메시지"),
                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                    .description("상품 ID"),
                fieldWithPath("data.productNumber").type(JsonFieldType.STRING)
                    .description("상품 번호"),
                fieldWithPath("data.type").type(JsonFieldType.STRING)
                    .description("상품 타입"),
                fieldWithPath("data.sellingType").type(JsonFieldType.STRING)
                    .description("상품 판매상태"),
                fieldWithPath("data.name").type(JsonFieldType.STRING)
                    .description("상품 이름"),
                fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                    .description("상품 가격")
            )
        ));
  }
}
