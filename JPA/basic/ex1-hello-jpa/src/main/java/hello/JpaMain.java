package hello;

import hello.entity.Member;
import hello.entity.cascadeEx.Child;
import hello.entity.cascadeEx.Parent;
import hello.entity.embed.Address;
import hello.entity.embed.AddressEntity;
import hello.entity.embed.Period;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.ManyToMany;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class JpaMain {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Member member1 = new Member();
      member1.setUsername("user1");
      em.persist(member1);

      List<Member> resultList = em.createNativeQuery(
              "select MEMBER_ID, city, street, zipcode, USERNAME from MEMBER", Member.class)
          .getResultList();

      for (Member member : resultList) {
        System.out.println("member = " + member);
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
