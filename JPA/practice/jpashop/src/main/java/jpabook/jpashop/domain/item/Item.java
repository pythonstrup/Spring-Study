package jpabook.jpashop.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

//@BatchSize(size = 100) // 컬렉션이 아닌 경우는 엔티티 위에 적어주면 되지만,, 사실 그냥 페치 조인버리는 게 훨씬 낫다.
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

  @Id @GeneratedValue
  @Column(name = "item_id")
  private Long id;

  private String name;
  private int price;
  private int stockQuantity;

  @ManyToMany(mappedBy = "items")
  private List<Category> categories = new ArrayList<>();

  //===Business Logic===//
  public void addStock(int quantity) {
    this.stockQuantity += quantity;
  }

  public void subtractStock(int quantity) {
    int restStock = this.stockQuantity - quantity;
    if (restStock < 0) {
      throw new NotEnoughStockException("need more stock");
    }
    this.stockQuantity = restStock;
  }
}
