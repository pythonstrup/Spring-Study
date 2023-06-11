package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

  private final EntityManager em;

  public Long save(Item item) {
    if (item.getId() == null) {
      em.persist(item);
    } else {
      // Merge를 사용하면 모든 속성이 변경된다는 것 때문에 위험하다.
      // 요소가 하나라도 없으면 null로 업데이트되어 버린다...
      // 그래서 되도록 사용하지 않는 것이 좋다.
      em.merge(item);
    }
    return item.getId();
  }

  public Item find(Long id) {
    return em.find(Item.class, id);
  }

  public List<Item> findAll() {
    return em.createQuery("select i from Item i", Item.class)
        .getResultList();
  }
}
