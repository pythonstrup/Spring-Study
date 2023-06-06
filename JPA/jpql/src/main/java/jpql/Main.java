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
      Team team1 = new Team();
      team1.setName("teamA");
      em.persist(team1);

      Team team2 = new Team();
      team2.setName("teamB");
      em.persist(team2);

      Member member1 = new Member();
      member1.setUsername("user1");
      member1.setAge(20);
      member1.changeTeam(team1);
      em.persist(member1);

      Member member2 = new Member();
      member2.setUsername("user2");
      member2.setAge(21);
      member2.changeTeam(team1);
      em.persist(member2);

      Member member3 = new Member();
      member3.setUsername("user3");
      member3.setAge(28);
      member3.changeTeam(team2);
      em.persist(member3);

      Member member4 = new Member();
      member4.setUsername("user4");
      member4.setAge(24);
      em.persist(member4);

      em.flush();
      em.clear();

      String query = "select distinct t from Team t join fetch t.members";
      List<Team> resultList = em.createQuery(query, Team.class).getResultList();
      for (Team team : resultList) {
        System.out.println("team = " + team.getName() + " members = " +  team.getMembers().size());
        for (Member member : team.getMembers()) {
          System.out.println("member = " + member);
        }
        System.out.println("==========================================");
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