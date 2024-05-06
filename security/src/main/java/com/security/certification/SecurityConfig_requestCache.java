package com.security.certification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig_requestCache {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
    requestCache.setMatchingRequestParameterName("customParam=y");
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/logout/success").permitAll()
            .anyRequest().authenticated())
        .formLogin(form -> form
            .successHandler(((request, response, authentication) -> {
              SavedRequest savedRequest = requestCache.getRequest(request, response);
              String redirectUrl = savedRequest.getRedirectUrl();
              response.sendRedirect(redirectUrl);
            })))
        .requestCache(cache -> cache
            .requestCache(requestCache));
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
