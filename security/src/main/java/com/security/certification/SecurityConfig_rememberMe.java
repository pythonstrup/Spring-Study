package com.security.certification;

import com.security.certification.handler.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig_rememberMe {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
        .rememberMe(rememberMe -> rememberMe
//            .alwaysRemember(true) // default => flase
            .tokenValiditySeconds(3600)
            .userDetailsService(userDetailsService())
            .rememberMeParameter("remember")
            .rememberMeCookieName("remember")
            .key("security"));
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user1 = User.withUsername("user")
        .password("{noop}1111")
        .authorities("ROLE_USER")
        .build();
    return new InMemoryUserDetailsManager(user1);
  }
}
