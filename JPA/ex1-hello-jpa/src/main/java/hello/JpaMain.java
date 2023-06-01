package hello;

import hello.entity.Member;
import hello.entity.Team;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import org.hibernate.Hibernate;
import org.hibernate.jpa.internal.PersistenceUnitUtilImpl;

public class JpaMain {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Team team1 = new Team();
      team1.setName("team");
      em.persist(team1);

      Team team2 = new Team();
      team2.setName("team");
      em.persist(team2);

      Member member1 = new Member();
      member1.setUsername("member");
      team1.addMember(member1);
      em.persist(member1);

      Member member2 = new Member();
      member2.setUsername("member");
      team2.addMember(member2);
      em.persist(member2);

      em.flush();
      em.clear();

//      Member findMember = em.find(Member.class, member.getId());

      // SQL: select * from Member
      // SQL: select * from Team where TEAM_ID = ?
      List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
          .getResultList();

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
