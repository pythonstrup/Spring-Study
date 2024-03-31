package sample.cafekiosk.spring.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

//@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

  protected MockMvc mockMvc;
  protected ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp(
//      WebApplicationContext webApplicationContext, // 스프링의 컨텍스트(@SpringBootTest를 해야 사용 가능)
      RestDocumentationContextProvider provider) {
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(initController())
//        .webAppContextSetup(webApplicationContext) // @SpringBootTest를 띄었을 때 사용! 하지만 굳이?
        .apply(documentationConfiguration(provider))
        .build();
  }

  protected abstract Object initController();
}
