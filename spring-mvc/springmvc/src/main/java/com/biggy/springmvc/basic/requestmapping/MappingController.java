package com.biggy.springmvc.basic.requestmapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MappingController {

  // 주소를 여러 개 지정할 수도 있다.
  @RequestMapping(value = {"/hello-basic", "/hello-go"}, method = RequestMethod.GET)
  public String helloBasic() {
    log.info("helloBasic");
    return "ok";
  }

  // 축약 표현
  @GetMapping(value = "/mapping-get-v2")
  public String mappingGetV2() {
    log.info("mapping-get-v2");
    return "ok";
  }

  // PathVariable의 변수명이 동일하면 변수명 지정을 생략해도 된다.
  // `@PathVariable String userId`
  @GetMapping("/mapping/{userId}")
  public String mappingPath(@PathVariable("userId") String data) {

    log.info("mappingPath userId={}", data);
    return "ok";
  }

  // 다중 매핑
  @GetMapping("/mapping/users/{userId}/orders/{orderId}")
  public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
    log.info("mappingPath userId={}, orderId={}", userId, orderId);
    return "ok";
  }

  // 특정 파라미터 조건 매핑 => 거의 사용하지 않는다.
  // 해당 쿼리스트링이 있어야 한다.
  @GetMapping(value = "/mapping-param", params = "mode=debug")
  public String mappingParam() {
    log.info("mappingParam");
    return "ok";
  }

  // 특정 헤더로 추가 매핑
  // 해당 헤더의 값이 있어야 한다.
  @GetMapping(value = "/mapping-header", headers = "mode=debug")
  public String mappingHeader() {
    log.info("mappingHeader");
    return "ok";
  }

  /**
   * Content-Type 헤더 기반 추가 매핑 Media Type
   * consumes="application/json"
   * consumes="!application/json"
   */
//  @PostMapping(value = "/mapping-consume", consumes = "application/json")
  @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
  public String mappingConsumes() {
    log.info("mappingConsumes");
    return "ok";
  }

  /**
   * 미디어 타입 조건 매핑
   * Accept 헤더 기반 Media Type
   * produces = "text/html"
   * produces = "text/*"
   */
//  @PostMapping(value = "/mapping-produce", produces = "text/html")
  @PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
  public String mappingProduces() {
    log.info("mappingProduces");
    return "ok";
  }
}
