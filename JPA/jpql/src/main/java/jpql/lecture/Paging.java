package jpql.lecture;

import java.util.List;
import javax.persistence.EntityManager;
import jpql.domain.Member;

public class Paging {

  private void paging(EntityManager em) {
    for (int i = 0; i < 100; i++) {
      Member member = new Member();
      member.setUsername("user" + i);
      member.setAge(i);
      em.persist(member);
    }

    em.flush();
    em.clear();

    List<Member> resultList = em.createQuery("select m from Member m order by m.age desc ",
            Member.class)
        .setFirstResult(0)
        .setMaxResults(10)
        .getResultList();
    for (Member m : resultList) {
      System.out.println("m = " + m);
    }
  }
}
