package com.biggy.springmvc.basic.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
public class RequestBodyStringController {

  @PostMapping("/request-body-string-v1")
  public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    ServletInputStream inputStream = request.getInputStream();
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("messageBody = {}", messageBody);

    response.getWriter().write("ok");
  }

  @PostMapping("/request-body-string-v2")
  public void requestBodyStringV2(InputStream inputStream, Writer writer)
      throws IOException {
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    log.info("messageBody = {}", messageBody);
    writer.write("ok");
  }

  @PostMapping("/request-body-string-v3")
  public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity)
      throws IOException {
    String messageBody = httpEntity.getBody();
    log.info("messageBody = {}", messageBody);
    return new HttpEntity<>("ok");
  }

  @PostMapping("/request-body-string-v4")
  public HttpEntity<String> requestBodyStringV4(RequestEntity<String> requestEntity)
      throws IOException {
    URI url = requestEntity.getUrl();
    String messageBody = requestEntity.getBody();
    log.info("urn = {}, messageBody = {}",url, messageBody);
    return new ResponseEntity<>("ok", HttpStatus.CREATED);
  }

  @PostMapping("/request-body-string-v5")
  public HttpEntity<String> requestBodyStringV5(@RequestBody String messageBody)
      throws IOException {
    log.info("messageBody = {}", messageBody);
    return new ResponseEntity<>("ok", HttpStatus.CREATED);
  }
}
