package com.biggy.springmvc.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestParamController {

  @RequestMapping("/request-param-v1")
  public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));
    log.info("username={}, age={}", username, age);

    response.getWriter().write("ok");
  }

  @ResponseBody
  @RequestMapping(value = "request-param-v2")
  public String RequestParamV2(
      @RequestParam("username") String memberName,
      @RequestParam("age") int memberAge
  ) {
    log.info("username={}, age={}", memberName, memberAge);
    return "ok";
  }

  @ResponseBody
  @RequestMapping(value = "request-param-v3")
  public String RequestParamV3(
      @RequestParam String username,
      @RequestParam int age
  ) {
    log.info("username={}, age={}", username, age);
    return "ok";
  }

  @ResponseBody
  @RequestMapping(value = "request-param-v4")
  public String RequestParamV4(String username, int age) {
    log.info("username={}, age={}", username, age);
    return "ok";
  }

  // required는 default가 false이다.
  @ResponseBody
  @RequestMapping(value = "request-param-required")
  public String RequestParamRequired(
      @RequestParam(required = true) String username,
      @RequestParam(required = false) Integer age
  ) {
    log.info("username={}, age={}", username, age);
    return "ok";
  }

  // default값이 있으면 쿼리스트링이 있다는 판정이므로 required 옵션이 의미가 사라진다.
  // 또한 빈 문자열이 들어왔을 때 default 값이 들어온다.
  @ResponseBody
  @RequestMapping(value = "request-param-default")
  public String RequestParamDefault(
      @RequestParam(defaultValue = "guest") String username,
      @RequestParam(defaultValue = "-1") int age
  ) {
    log.info("username={}, age={}", username, age);
    return "ok";
  }

  @ResponseBody
  @RequestMapping(value = "request-param-map")
  public String RequestParamMap(@RequestParam Map<String, Object> paramMap) {
    log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
    return "ok";
  }
}
