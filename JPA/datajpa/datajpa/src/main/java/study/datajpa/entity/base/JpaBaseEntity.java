package study.datajpa.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public class JpaBaseEntity {

  @Column(updatable = false) // 변경 불가
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  @PrePersist // 최초 등록 시
  public void prePersist() {
    LocalDateTime now = LocalDateTime.now();
    createdDate = now;
    updatedDate = now;
  }

  @PreUpdate // 업데이트 시
  public void preUpdate() {
    updatedDate = LocalDateTime.now();
  }
}
