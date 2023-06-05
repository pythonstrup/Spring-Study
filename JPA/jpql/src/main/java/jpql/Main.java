package jpql;

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

      Member member = new Member();
      member.setUsername("user1");
      member.setAge(20);
      member.changeTeam(team);
      member.setMemberType(MemberType.ADMIN);
      em.persist(member);

      em.flush();
      em.clear();

      String query = "select m.username, 'HELLO', true from Member m "
          + "where m.memberType = :memberType";
      List<Object[]> result = em.createQuery(query)
          .setParameter("memberType", MemberType.ADMIN)
          .getResultList();

      for (Object[] objects : result) {
        System.out.println("objects = " + objects[0]);
        System.out.println("objects = " + objects[1]);
        System.out.println("objects = " + objects[2]);
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