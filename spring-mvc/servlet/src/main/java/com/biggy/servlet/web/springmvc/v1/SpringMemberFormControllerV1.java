package com.biggy.servlet.web.springmvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

// @Controller 대신에 @Component와 @RequestMapping 2개의 어노테이션을 적어서 돌려도 돌아가진다.
// 또는 컴포넌트 스캔 없이 직접 스프링 빈에 등록해 @RequestMapping만 붙여도 동작한다.
@Controller
public class SpringMemberFormControllerV1 {

  @RequestMapping("/springmvc/v1/members/new-form")
  public ModelAndView process() {
    return new ModelAndView("new-form");
  }
}
