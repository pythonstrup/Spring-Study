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
}
