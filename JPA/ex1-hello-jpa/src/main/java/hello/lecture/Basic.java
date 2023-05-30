package hello.lecture;

import hello.entity.Member;
import java.util.List;
import javax.persistence.EntityManager;

public class Basic {

  private void basic(EntityManager em) {
    // save
      Member member = new Member();
      member.setId(3L);
      member.setUsername("park park");
      em.persist(member);

     // em.persist를 호출하지 않아도 자동으로 변경된다.(update문 실행됨)
     // 더티 체킹!! 영속성 컨텍스트!!
      Member findMember = em.find(Member.class, 1L);
      findMember.setUsername("new Kim");

    // JPQL
    String jpql = "select m from Member as m where m.username like '%park%'";
    List<Member> resultList = em.createQuery(jpql, Member.class)
        .setFirstResult(0) // offset - pagination
        .setMaxResults(2)  // limit - pagination
        .getResultList();
    for (Member m : resultList) {
      System.out.println("member.name = " + m.getUsername());
    }
  }
}
