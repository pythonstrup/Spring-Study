package hello.lecture;

import hello.entity.Member;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ObjectOrientedQuery {

  // 문자가 아닌 자바코드로 JPQL을 작성할 수 있다는 장점이 있지만,
  // SQL스럽지 않다, 너무 복잡하고 실용성이 없다는 단점이 존재..
  // 실무에서 많이 쓰지는 않는다고 함. 코드 리팩토링이 너무 어렵다고 함!
  // QueryDSL 사용 권장!!!
  private void criteria(EntityManager em) {
    // Criteria
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Member> query = cb.createQuery(Member.class);

    Root<Member> m =query.from(Member.class);

    CriteriaQuery<Member> cq = query.select(m);

    String username = "park";
    if (username != null) {
      cq = cq.where(cb.equal(m.get("username"), "park"));
    }

    List<Member> resultList = em.createQuery(cq).getResultList();
  }

  // QueryDSL 또한, 문자가 아닌 자바코드로 JPQL을 작성할 수 있다는 장점
  // JPQL의 빌더 역할, 단순하고 쉽다. 동적쿼리 작성도 편리하다.
  // 컴파일 시점에 문법 오류를 발견할 수 있다.
  // 실무에서 사용 강추!!
  private void queryDsl(EntityManager em) {}

  // JPA가 제공한는 SQL 직접 작성 기능!
  // JPQL로 해결할 수 없는 특정 데이터베이스에 의존적인 기능이다. (SQL마다 방언이 다르기 때문에)
  private void nativeSQL(EntityManager em) {
    Member member1 = new Member();
    member1.setUsername("user1");
    em.persist(member1);

    List<Member> resultList = em.createNativeQuery(
            "select MEMBER_ID, city, street, zipcode, USERNAME from MEMBER", Member.class)
        .getResultList();

    for (Member member : resultList) {
      System.out.println("member = " + member);
    }
  }
}
