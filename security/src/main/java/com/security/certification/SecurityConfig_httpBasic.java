package com.security.certification;

import com.security.certification.handler.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig_httpBasic {

//  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
//        .httpBasic(Customizer.withDefaults());
        .httpBasic(basic -> basic
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
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
