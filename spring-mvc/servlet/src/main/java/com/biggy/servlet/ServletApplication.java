package com.biggy.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ServletComponentScan // 서블릿 자동 등록
@SpringBootApplication
public class ServletApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServletApplication.class, args);
  }

  // 스프링 Boot가 자동으로 해주는 것
//  @Bean
//  InternalResourceViewResolver internalResourceViewResolver() {
//    return new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
//  }
}
