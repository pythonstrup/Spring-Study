package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .formLogin(form -> form
//            .loginPage("/loginPage")
            .loginProcessingUrl("/loginProc")
            .defaultSuccessUrl("/", true)
            .failureUrl("/failed")
            .usernameParameter("userId")
            .passwordParameter("passwd")
            .successHandler((request, response, authentication) -> {
              System.out.println("authentication: " + authentication);
              response.sendRedirect("/home");
            })
            .failureHandler((request, response, exception) -> {
              System.out.println("exception = " + exception);
              response.sendRedirect("/login");
            })
            .permitAll());
    return http.build();
  }

  @Bean
  public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    UserDetails user1 = User.withUsername("user")
        .password("{noop}1111")
        .authorities("ROLE_USER")
        .build();
    return new InMemoryUserDetailsManager(user1);
  }
}
