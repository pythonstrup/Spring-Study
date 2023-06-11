package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

  private final ItemRepository itemRepository;

  @Transactional
  public void saveItem(Item item) {
    itemRepository.save(item);
  }

  // 변경감지로 업데이트
  // 변경감지를 사용하면 원하는 속성만 선택해서 변경할 수 있다는 장점이 있다. (병합은 약간 위험)
  // 따라서 웬만하면 병합이 아니라 변경감지를 사용하는 것이 좋다.
  @Transactional
  public void updateItem(Long itemId, String name, int price, int stockQuantity) {
    Item findItem = itemRepository.find(itemId);
    findItem.setName(name);
    findItem.setPrice(price);
    findItem.setStockQuantity(stockQuantity);
  }

  public List<Item> findItems() {
    return itemRepository.findAll();
  }

  public Item findOne(Long itemId) {
    return itemRepository.find(itemId);
  }
}
