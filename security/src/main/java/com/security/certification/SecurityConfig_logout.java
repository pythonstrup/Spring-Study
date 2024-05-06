package com.security.certification;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig_logout {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/logout/success").permitAll()
            .anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
//        .csrf(csrf -> csrf.disable())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
            .logoutSuccessUrl("/logout/success")
            .logoutSuccessHandler(((request, response, authentication) -> {
              response.sendRedirect("/logout/success");
            }))
            .deleteCookies("JSESSIONID", "remember-me")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .addLogoutHandler(((request, response, authentication) -> {
              HttpSession session = request.getSession();
              session.invalidate();
              SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(null);
              SecurityContextHolder.getContextHolderStrategy().clearContext();
            }))
            .permitAll());
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
