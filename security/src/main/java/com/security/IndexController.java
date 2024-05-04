package com.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @GetMapping
  public String index() {
    return "index";
  }

  @GetMapping("/home")
  public String home() {
    return "home";
  }

  @GetMapping("/loginPage")
  public String loginPage() {
    return "loginPage";
  }

  @GetMapping("/anonymous")
  public String anonymous() {
    return "anonymous";
  }

  @GetMapping("/authentication")
  public String authentication(Authentication authentication) {
    if (authentication instanceof AnonymousAuthenticationToken) {
      return "anonymous"; // 익명 사용자로 들어와도 authenticaiton이 null이라 해당 값을 반환받지 못한다.
    } else {
      return "not anonymous";
    }
  }

  @GetMapping("/anonymousContext")
  public String anonymousContext(@CurrentSecurityContext SecurityContext context) {
    return context.getAuthentication().getName();
  }
}
