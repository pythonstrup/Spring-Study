package jpql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import jpql.domain.Member;
import jpql.domain.Team;
import jpql.dto.MemberDto;

public class Main {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Member member = new Member();
      member.setUsername("user1");
      member.setAge(28);
      em.persist(member);

      em.flush();
      em.clear();

      List<MemberDto> resultList = em.createQuery("select new jpql.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
          .getResultList();
      MemberDto memberDto = resultList.get(0);
      System.out.println("username = " + memberDto.getUsername());
      System.out.println("age = " + memberDto.getAge());

      tx.commit();
    } catch (Exception e) {
      e.printStackTrace();
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}