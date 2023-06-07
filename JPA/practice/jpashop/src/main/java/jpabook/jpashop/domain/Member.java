package jpabook.jpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

  @Id @GeneratedValue
  @Column(name = "member_id")
  private Long id;
  private String username;

  @Embedded
  private Address address;

  // 컬렉션은 필드에서 초기화하자. => Best Practice
  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();
}
