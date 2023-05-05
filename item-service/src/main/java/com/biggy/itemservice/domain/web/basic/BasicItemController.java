package com.biggy.itemservice.domain.web.basic;

import com.biggy.itemservice.domain.item.Item;
import com.biggy.itemservice.domain.item.ItemRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

  private final ItemRepository itemRepository;

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "basic/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/item";
  }

  @GetMapping("/add")
  public String addForm() {
    return "basic/addForm";
  }

  @PostMapping("/v1/add")
  public String addItemV1(@RequestParam String itemName,
                      @RequestParam Integer price,
                      @RequestParam Integer quantity,
                      Model model) {

    Item item = new Item();
    item.setItemName(itemName);
    item.setPrice(price);
    item.setQuantity(quantity);

    itemRepository.save(item);

    model.addAttribute("item", item);

    return "basic/item";
  }

  @PostMapping("/v2/add")
  public String addItemV2(@ModelAttribute("item") Item item, Model model) {

    itemRepository.save(item);

//    model.addAttribute("item", item); // ModelAttribute를 통해 자동 추가되므로 생략가능

    return "basic/item";
  }

  // 이름도 생략하고 model도 생략할 수 있다!!!
  @PostMapping("/add")
  public String addItemV3(@ModelAttribute Item item) {

    itemRepository.save(item);

    return "basic/item";
  }

  /**
   * 테스트용 데이터
   */
  @PostConstruct
  public void init() {
    itemRepository.save(new Item("itemA", 10000, 10));
    itemRepository.save(new Item("itemB", 20000, 20));
  }
}
