package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

@SpringBootTest
class ItemRepositoryTest {

  @Autowired
  ItemRepository itemRepository;

  @Test
  void save() {

    // 아래와 같이 식별자를 넣어서 저장하면?
    // JPA interface에서 save가 아닌 merge가 호출된다.
    // merge는 select로 찾아낸 후, 없으면 새로운 값을 넣는 형식이기 때문에, 굉장히 비효율적이다.
    // 이 문제를 해결하려면? Persistable 인터페이스를 구현해서 판단 로직을 변경할 수 있다. (inNew 메소드)
    Item item = new Item("A");
    itemRepository.save(item);
  }
}