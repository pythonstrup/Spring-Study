package memory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "memory", havingValue = "on")
public class MemoryAutoConfig {

  @Bean
  public MemoryController memoryController() {
    return new MemoryController(memoryFinder());
  }

  @Bean
  public MemoryFinder memoryFinder() {
    return new MemoryFinder();
  }
}
