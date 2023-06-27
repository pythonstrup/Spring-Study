package study.datajpa.repository.projections;

import org.springframework.beans.factory.annotation.Value;

// 인터페이스 기반 프로젝션
public interface UsernameOnly {

  // open projections
  // 엔티티의 모든 필드를 가져오고, 그것을 조합해서 보여줄 수 있음
  @Value("#{target.username + ' ' + target.age}")
  String getUsername();

  // closed projections
  // 필요한 것만 가져와서 보여줌
//  String getUsername();
}
