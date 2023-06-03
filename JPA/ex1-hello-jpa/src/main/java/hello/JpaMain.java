package hello;

import hello.entity.Member;
import hello.entity.cascadeEx.Child;
import hello.entity.cascadeEx.Parent;
import hello.entity.embed.Address;
import hello.entity.embed.Period;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Address homeAddress = new Address("city", "street", "10000");

      Member member1 = new Member();
      member1.setUsername("user1");
      member1.setHomeAddress(homeAddress);
      member1.setWorkPeriod(new Period());
      em.persist(member1);

      Address newHomeAddress = new Address("newCity", "newStreet", homeAddress.getZipcode());
      member1.setHomeAddress(newHomeAddress);

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
