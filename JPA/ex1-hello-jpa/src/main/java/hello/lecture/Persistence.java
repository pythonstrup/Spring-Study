package hello.lecture;

import hello.Member;
import javax.persistence.EntityManager;

public class Persistence {

  private void persist1(EntityManager em) {
    // 비영속
    Member member = new Member();
    member.setId(101L);
    member.setUsername("HelloJPA");

    // 영속
    System.out.println("===BEFORE===");
    em.persist(member);
    em.detach(member); // 영속에서 분리 -> 준영속 상태
    em.remove(member); // 객체를 삭제한 상태
    System.out.println("===AFTER===");

    // 조회를 하는데, select 쿼리를 날리지 않는다!! => 1차 캐시를 먼저 조회하기 때문이다.
    Member findMember = em.find(Member.class, 101L);

    System.out.println("findMember.id = " + findMember.getId());
    System.out.println("findMember.name = " + findMember.getUsername());
  }

  private void persist2(EntityManager em) {
    // 결과적으로 쿼리는 1번 나간다.
//      Member findMember1 = em.find(Member.class, 101L);
//      Member findMember2 = em.find(Member.class, 101L); // 1차 캐시로 가지고 온다.
//      System.out.println("result = " + (findMember1 == findMember2));
//
//      Member member1 = new Member(150L, "A");
//      Member member2 = new Member(160L, "B");
//      em.persist(member1);
//      em.persist(member2);
//
//    Member member = em.find(Member.class, 150L);
//    member.setName("ZZZZZZZ");
//    System.out.println("=======================");
  }

  private void flush(EntityManager em) {
//    Member member = new Member(200L, "member200");
//    em.persist(member);

//    em.flush(); // 쿼리가 즉시 실행된다.
    // 영속성 컨텍스트를 비우는 게 아니라, 변경내용을 데이터베이스에 동기화하는 작업이다.

    System.out.println("===========================");
  }

  private void semiPersistence(EntityManager em) {
//    Member member1 = em.find(Member.class, 150L);
//    member1.setName("AAAAAAAA");

    // em.detach(member1); // 특정 엔티티만 준영속 상태로 전환한다.
//    em.clear(); // 영속성 컨텍스트를 통으로 다 지워버린다. 그러므로 1차 캐시가 작동하지 않고 다시 select하게 된다.
    // em.close(); // 영속성 컨텍스트를 아예 닫아버린다.

//    Member member2 = em.find(Member.class, 150L);

    System.out.println("===========================");
  }


}
