package jpql.lecture;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import jpql.domain.Member;

public class Basic {

  private void typeQueryAndQuery(EntityManager em) {
    Member member = new Member();
    member.setUsername("user1");
    member.setAge(20);
    em.persist(member);

    TypedQuery<Member> typedQuery1 = em.createQuery("select m from Member m", Member.class);
    TypedQuery<String> typedQuery2 = em.createQuery("select m.username from Member m", String.class);

    // 반환 타입이 명확하지 않을 때
    Query query = em.createQuery("select m.username, m.age from Member m");
  }

  private void getResult(EntityManager em) {
    Member member = new Member();
    member.setUsername("user1");
    member.setAge(20);
    em.persist(member);

    TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);

    // 결과가 하나 이상이면 리스트를 반환하고, 결과가 없으면 빈리스트를 반환한다.
    List<Member> resultList = query.getResultList();

    for (Member member1 : resultList) {
      System.out.println("member1 = " + member1);
    }

    // singleResult는 주의할 점이 있다.
    // 결과가 정확히 하나여야만 한다.
    // 결과가 없으면 javax.persistence.NoResultException
    // 둘 이상이면 javax.persistence.NonUniqueResultException
    Member singleResult = query.getSingleResult();
    System.out.println("singleResult = " + singleResult);

    // SingleResult가 JPA 표준 API이기 때문에 Spring JPA Data에서도 어쩔 수 없이 사용한다고 한다.
    // 예외처리를 위해 try ~ catch로 감싼다고 함. 그리고 예외 터지면 null이나, Optional을 반환!
  }

  private void jpqlWithParameter(EntityManager em) {
    Member member = new Member();
    member.setUsername("user1");
    member.setAge(20);
    em.persist(member);

//    TypedQuery<Member> query = em.createQuery("select m from Member m where m.username = :username", Member.class);
//    query.setParameter("username", "user1");
//    Member singleResult = query.getSingleResult();

    // 사용하지 않는 것이 좋다. 숫자가 밀려버릴 수도 있기 때문에
//    Member singleResult = em.createQuery("select m from Member m where m.username = :1", Member.class)
//        .setParameter(1, "user1")
//        .getSingleResult();

    Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
        .setParameter("username", "user1")
        .getSingleResult();
    System.out.println("singleResult = " + singleResult);
  }
}
