package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

  // 식별자 생성 전략이 @GenerateValue면 save() 호출 시점에 식별자가 없으므로 새로운 엔티티로 인식해 정상 동작한다.
  // 그러나 JPA 식별자 생성 전략이 @Id만 사용해 직접 할당한다면 이미 식별자 값이 정해져있는 상태로 save()를 호출한다.
  // 이때 merge가 호출되는데, merge는 해당 식별자를 가진 객체가 존재하는지 select로 확인하고 없으면 새로운 엔티티로 인지하기 때문에 비효율적이다.
  // 따라서 이런 경우, Persistable을 사용해 새로운 엔티티 확인 여부를 직접 구현하는 것이 효과적이다.
  // @CreatedDate를 사용하면 새로운 엔티티 여부를 편리하게 확인할 수 있으니 참고하자!!
  @Id
  private String id;

  // Persistable 인터페이스를 구현할 때, isNew 메소드를 구현하는 난관을 createdDate로 해결할 수 있다.
  // 그냥 createdDate가 null이면 새로운 객체라고 판단해버리면 되는 문제이다.
  @CreatedDate
  private LocalDateTime createdDate;

  public Item(String id) {
    this.id = id;
  }

  @Override
  public boolean isNew() {
    return createdDate == null;
  }
}
