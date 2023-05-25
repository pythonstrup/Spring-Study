package com.hello;

import java.util.List;
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
      // 비영속
      Member member = new Member();
      member.setId(100L);
      member.setName("HelloJPA");

      // 영속
      System.out.println("===BEFORE===");
      em.persist(member);
      // em.detach(member); // 영속에서 분리 -> 준영속 상태
      // em.remove(member); // 객체를 삭제한 상태
      System.out.println("===AFTER===");

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }

  private void basic() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {
      // save
//      Member member = new Member();
//      member.setId(3L);
//      member.setName("park park");
//      em.persist(member);

      // em.persist를 호출하지 않아도 자동으로 변경된다.(update문 실행됨)
      // 더티 체킹!! 영속성 컨텍스트!!
//      Member findMember = em.find(Member.class, 1L);
//      findMember.setName("new Kim");

      // JPQL
      String jpql = "select m from Member as m where m.name like '%park%'";
      List<Member> resultList = em.createQuery(jpql, Member.class)
          .setFirstResult(0) // offset - pagination
          .setMaxResults(2)  // limit - pagination
          .getResultList();
      for (Member member : resultList) {
        System.out.println("member.name = " + member.getName());
      }

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}
