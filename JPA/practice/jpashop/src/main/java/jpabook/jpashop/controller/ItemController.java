package jpabook.jpashop.controller;

import java.util.List;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.BookFormDto;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping("/items/new")
  public String create(Model model) {
    model.addAttribute("form", new BookFormDto());
    return "items/createItemForm";
  }

  @PostMapping("/items/new")
  public String create(BookFormDto form) {
    Book book = new Book();
    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());

    itemService.saveItem(book);
    return "redirect:/";
  }

  @GetMapping("/items")
  public String list(Model model) {
    List<Item> items = itemService.findItems();
    model.addAttribute("items", items);
    return "items/itemList";
  }

  @GetMapping("/items/{itemId}/edit")
  public String updateForm(@PathVariable Long itemId, Model model) {
    Book item = (Book) itemService.findOne(itemId);

    // entity로 내보내지 않고 dto로 내보낸다.
    BookFormDto form = new BookFormDto();
    form.setId(item.getId());
    form.setName(item.getName());
    form.setPrice(item.getPrice());
    form.setStockQuantity(item.getStockQuantity());
    form.setAuthor(item.getAuthor());
    form.setIsbn(item.getIsbn());
    model.addAttribute("form", form);

    return "items/updateItemForm";
  }

  @PostMapping("/items/{itemId}/edit")
  public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookFormDto form) {
//    // 준영속 엔티티(실제로 존재하는 식별자 값이 들어있음)
//    // 영속성 컨텍스트에서 관리하지 않는 엔티티 => JPA에서 관리하지 않기 때문에 변경감지가 되지 않는다.
//    // 컨트롤러에서는 어설프게 엔티티를 사용하지 않도록 하자...
//    Book book = new Book();
//    book.setId(form.getId());
//    book.setName(form.getName());
//    book.setPrice(form.getPrice());
//    book.setStockQuantity(form.getStockQuantity());
//    book.setAuthor(form.getAuthor());
//    book.setIsbn(form.getIsbn());
//    // 병합(Merge)으로 처리되는 코드
//    // repository.save에서 em.merge로 처리됨
//    itemService.saveItem(book);

    itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
    return"redirect:/items";
  }
}
