package hello.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Product {

  @Id @GeneratedValue
  private Long id;

  private String name;

  // 편리해보이지만 실무에서는 사용하지 않는다.
  // 연결테이블이 단순히 연결만 하고 끝날 가능성은 거의 없다. 다른 데이터가 들어올 수 있기 때문에..
  // 중간 테이블을 엔티티로 승격하고, @OneToMany와 @ManyToOne 등을 통해 관계를 만들어주는 것이 훨씬 좋다.
//  @ManyToMany(mappedBy = "products")
//  private List<Member> members = new ArrayList<>();

  @OneToMany(mappedBy = "product")
  private List<MemberProduct> memberProducts  = new ArrayList<>();

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

  public List<MemberProduct> getMemberProducts() {
    return memberProducts;
  }

  public void setMemberProducts(List<MemberProduct> memberProducts) {
    this.memberProducts = memberProducts;
  }
}
