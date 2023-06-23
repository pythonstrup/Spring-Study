package study.datajpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DatajpaApplication {

  public static void main(String[] args) {
    SpringApplication.run(DatajpaApplication.class, args);
  }

  // 실제로는 Spring Security에서 꺼내와서 넣어줘야한다.
  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> Optional.of(UUID.randomUUID().toString());
  }
}
