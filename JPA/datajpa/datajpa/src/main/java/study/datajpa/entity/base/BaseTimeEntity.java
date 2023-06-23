package study.datajpa.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// 아래와 같이 time만 따로 나눠서 BaseEntity에서 상속받는 방식으로 구성하는 것도 좋다.
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime lastModifiedDate;

}
