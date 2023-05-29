package hello;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
// 아래 옵션은 @DiscriminatorColumn 어노테이션 없이 자동으로 DTYPE을 넣어준다.
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 조인 전략보다 성능이 좋은 편이다.
// item 테이블이 존재하지 않고, 모든테이블에 컬럼 넣어줌
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 추천하지 않는 전략
public abstract class Item {

  @Id @GeneratedValue
  private Long id;

  private String name;
  private int price;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }
}
