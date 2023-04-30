package com.biggy.springmvc.basic.request;

import com.biggy.springmvc.basic.HelloData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestBodyJsonController {

  private ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping("/request-body-json-v1")
  public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    ServletInputStream inputStream = request.getInputStream();
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("messgeBody = {}", messageBody);
    HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
    log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

    response.getWriter().write("OK");
  }

  @ResponseBody
  @PostMapping("/request-body-json-v2")
  public String requestBodyJsonV2(@RequestBody String messageBody)
      throws IOException {
    log.info("messgeBody = {}", messageBody);
    HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
    log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

    return "OK";
  }

  @ResponseBody
  @PostMapping("/request-body-json-v3")
  public String requestBodyJsonV3(@RequestBody HelloData helloData)
      throws IOException {
    log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

    return "OK";
  }

  @ResponseBody
  @PostMapping("/request-body-json-v4")
  public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity)
      throws IOException {
    HelloData helloData = httpEntity.getBody();
    log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

    return "OK";
  }

  @ResponseBody
  @PostMapping("/request-body-json-v5")
  public HelloData requestBodyJsonV5(@RequestBody HelloData helloData)
      throws IOException {
    log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

    return helloData;
  }
}
