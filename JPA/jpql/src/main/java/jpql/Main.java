package jpql;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import jpql.constants.MemberType;
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

      Member member1 = new Member();
      member1.setUsername("user1");
      member1.setAge(20);
      member1.changeTeam(team);
      em.persist(member1);

      Member member2 = new Member();
      member2.setUsername("user2");
      member2.setAge(21);
      member2.changeTeam(team);
      em.persist(member2);

      em.flush();
      em.clear();

      String query = "select m.username from Team t join t.members m";
      Collection result = em.createQuery(query, Collection.class).getResultList();
      for (Object o : result) {
        System.out.println("o = " + o);
      }

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