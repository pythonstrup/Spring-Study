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
import javax.persistence.Persistence;

public class JpaMain {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      Address homeAddress = new Address("homeCity", "street", "10000");

      Member member = new Member();
      member.setUsername("user1");
      member.setHomeAddress(homeAddress);

      member.getFavoriteFoods().add("Chicken");
      member.getFavoriteFoods().add("Pork belly");
      member.getFavoriteFoods().add("Pizza");

      member.getAddressHistory().add(new AddressEntity(new Address("city1", "street1", "10001")));
      member.getAddressHistory().add(new AddressEntity(new Address("city2", "street2", "10002")));

      em.persist(member);

      em.flush();
      em.clear();

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
