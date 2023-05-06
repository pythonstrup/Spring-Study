package com.biggy.springmvc.basic.response;

import com.biggy.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@Controller
// @ResponseBody => class 레벨에 붙이면 클래스의 모든 메소드에 적용된다.
// @RestController => @Controller와 @ResponseBody를 모두 달고 있기 때문에 이 어노테이션만 붙여줘도 된다!
public class ResponseBodyController {

  @GetMapping("/response-body-string-v1")
  public void responseBodyV1(HttpServletResponse response) throws IOException {
    response.getWriter().write("OK");
  }

  @GetMapping("/response-body-string-v2")
  public ResponseEntity<String> responseBodyV2() {
    return new ResponseEntity<>("OK", HttpStatus.OK);
  }

  @ResponseBody
  @GetMapping("/response-body-string-v3")
  public String responseBodyV3() throws IOException {
    return "OK";
  }

  @GetMapping("/response-body-json-v1")
  public ResponseEntity<HelloData> responseBodyJsonV1() {
    HelloData helloData = new HelloData();
    helloData.setUsername("UserA");
    helloData.setAge(20);
    return new ResponseEntity<>(helloData, HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @GetMapping("/response-body-json-v2")
  public HelloData responseBodyJsonV2() {
    HelloData helloData = new HelloData();
    helloData.setUsername("UserA");
    helloData.setAge(20);
    return helloData;
  }
}
