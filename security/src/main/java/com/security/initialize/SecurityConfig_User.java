package com.security.initialize;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig_User {

//  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .formLogin(Customizer.withDefaults());
    return http.build();
  }

  // application.yaml 에서도 설정할 수 있음
  // application.yaml 과 중복되면 코드로 작성된 설정이 더 우선된다.
  @Bean
  public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    UserDetails user1 = User.withUsername("user1")
        .password("{noop}1111")
        .authorities("ROLE_USER")
        .build();
    UserDetails user2 = User.withUsername("user2")
        .password("{noop}1111")
        .authorities("ROLE_USER")
        .build();
    UserDetails user3 = User.withUsername("user3")
        .password("{noop}1111")
        .authorities("ROLE_USER")
        .build();
    return new InMemoryUserDetailsManager(user1,user2,user3);
  }
}
