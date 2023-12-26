package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jpa.JpaItemRepositoryV3;
import hello.itemservice.repository.v2.ItemQueryRepositoryV2;
import hello.itemservice.repository.v2.ItemRepositoryV2;
import hello.itemservice.service.ItemServiceV2;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class V2Config {

  private final ItemRepositoryV2 itemRepository;
  private final EntityManager em;

  @Bean
  public ItemServiceV2 itemService() {
    return new ItemServiceV2(itemRepository, itemQueryRepository());
  }

  @Bean
  public ItemQueryRepositoryV2 itemQueryRepository() {
    return new ItemQueryRepositoryV2(em);
  }

  // 테스트 데이터 초기화 때문에 필요...
  @Bean
  public ItemRepository itemRepository() {
    return new JpaItemRepositoryV3(em);
  }
}
