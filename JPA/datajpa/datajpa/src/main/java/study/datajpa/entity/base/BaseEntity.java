package study.datajpa.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// JpaBaseEntity와 비교
// 아래 설정들을 사용하려면 Application(main) 클래스에서 @EnableJpaAuditing 어노테이션을 추가해줘야한다.
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {

  @CreatedBy
  @Column(updatable = false)
  private String createBy;

  @LastModifiedBy
  private String lastModifiedBy;
}
