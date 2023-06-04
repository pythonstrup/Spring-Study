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
      Team team = new Team();
      team.setName("teamA");
      em.persist(team);

      Member member = new Member();
      member.setUsername("user1");
      member.setAge(20);
      member.changeTeam(team);
      em.persist(member);

      em.flush();
      em.clear();

      String query = "select m from Member m left join Team t on m.username = t.name";
      List<Member> resultList = em.createQuery(query, Member.class)
          .getResultList();

      System.out.println("resultList.size() = " + resultList.size());

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