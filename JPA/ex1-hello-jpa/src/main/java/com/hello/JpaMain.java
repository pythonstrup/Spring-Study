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
      // 결과적으로 쿼리는 1번 나간다.
//      Member findMember1 = em.find(Member.class, 101L);
//      Member findMember2 = em.find(Member.class, 101L); // 1차 캐시로 가지고 온다.
//      System.out.println("result = " + (findMember1 == findMember2));

//      Member member1 = new Member(150L, "A");
//      Member member2 = new Member(160L, "B");
//      em.persist(member1);
//      em.persist(member2);

      Member member = em.find(Member.class, 150L);
      member.setName("ZZZZZZZ");
      System.out.println("=======================");

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }

  private void basic(EntityManagerFactory emf, EntityManager em, EntityTransaction tx) {
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

  public static void persist1(EntityManagerFactory emf, EntityManager em, EntityTransaction tx) {
    tx.begin();
    try {
      // 비영속
      Member member = new Member();
      member.setId(101L);
      member.setName("HelloJPA");

      // 영속
      System.out.println("===BEFORE===");
      em.persist(member);
      // em.detach(member); // 영속에서 분리 -> 준영속 상태
      // em.remove(member); // 객체를 삭제한 상태
      System.out.println("===AFTER===");

      // 조회를 하는데, select 쿼리를 날리지 않는다!! => 1차 캐시를 먼저 조회하기 때문이다.
      Member findMember = em.find(Member.class, 101L);

      System.out.println("findMember.id = " + findMember.getId());
      System.out.println("findMember.name = " + findMember.getName());

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }
    emf.close();
  }
}
