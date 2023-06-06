package jpql.lecture;

import java.util.List;
import javax.persistence.EntityManager;
import jpql.domain.Member;

public class NamedQuery {

  // 미리 정의해서 이름을 부여해두고 사용하는 JPQL
  // 정적 쿼리
  // 어노테이션이나 XML에 정의
  // 애플리케이션 로딩 시점에 초기화 후 재사용
  // 중요!!! 애플리케이션 로딩 시점에 쿼리를 검증 => 정적 쿼리라 미리 파싱을 하기 때문에 오류를 잡기 좋다.
  private void namedQuery(EntityManager em) {
    List<Member> result = em.createNamedQuery("Member.findByUsername", Member.class)
        .setParameter("username", "user1")
        .getResultList();
    for (Member member : result) {
      System.out.println("member = " + member);
    }
  }

  // 사실 @NamedQuery는 Spring Data JPA에서는 사용할 일이 없다.
  // 이름 없는 NamedQuery
}
