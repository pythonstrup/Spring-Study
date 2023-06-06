package jpql.lecture;

import java.util.List;
import javax.persistence.EntityManager;
import jpql.domain.Address;
import jpql.domain.Member;
import jpql.domain.Team;
import jpql.dto.MemberDto;

public class Projection {

  // 프로젝션 대상: Entity, Embedded Type, Scalar Type(숫자, 문자 등 기본 데이터 타입)
  // SELECT m FROM Member m => 엔티티 프로젝션
  // SELECT m.team FROM Member m => 엔티티 프로젝션
  // SELECT m.address FROM Member m => 임베디드 타입 프로젝션
  // SELECT m.useranme, m.age FROM Member m => 스칼라 타입 프로젝션


  private void entity(EntityManager em) {
    Member member = new Member();
    member.setUsername("user1");
    member.setAge(28);
    em.persist(member);

    em.flush();
    em.clear();

    List<Member> result = em.createQuery("select m from Member m", Member.class)
        .getResultList();

    // JPQL로 가져온 객체로 영속성 컨텍스트에서 관리된다!!
    Member findMember = result.get(0);
    findMember.setAge(20);
  }

  private void relationEntity(EntityManager em) {
    Member member = new Member();
    member.setUsername("user1");
    member.setAge(28);
    em.persist(member);

    em.flush();
    em.clear();

//    List<Team> result = em.createQuery("select m.team from Member m", Team.class)
//        .getResultList();
    // 실제 SQL 문의 Select 문은 다르게 나간다. 알아서 조인을 해준다.
    // select team1_.id as id1_3_, team1_.name as name2_3_
    // from Member member0_ inner join Team team1_ on member0_.TEAM_ID=team1_.id

    // 그런데 사실 JPQL은 최대한 실제 SQL 문과 비슷하게 적어줘야한다.
    // 그렇지 않으면 코드가 예측이 안되기 때문이다.
    // 그래야 튜닝할 때도 편하다.
    List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class)
        .getResultList();
  }

  private void embeddedType(EntityManager em) {
    List<Address> result = em.createQuery("select o.address from Order o", Address.class)
        .getResultList();

    // 임베디드 타입은 특정 객체에 소속된 객체이기 때문에 아래와 같이 독립적으로 조회할 수는 없다는 한계를 가짐
//    List<Address> result = em.createQuery("select a from Address a", Address.class)
//        .getResultList();
  }

  // DISTINCT를 통해 중복 제거 가능!!
  private void scalar(EntityManager em) {
    em.createQuery("select distinct m.username, m.age from Member m")
        .getResultList();
  }

  // 스칼라 타입 프로젝션 확인하는 방법 1
  // Query로 조회하기
  private void scalarProjection1(EntityManager em) {
    Member member = new Member();
    member.setUsername("user1");
    member.setAge(28);
    em.persist(member);

    em.flush();
    em.clear();

    List resultList = em.createQuery("select distinct m.username, m.age from Member m")
        .getResultList();
    Object o = resultList.get(0);
    Object[] result = (Object[]) o;
    System.out.println("username = " + result[0]);
    System.out.println("age = " + result[1]);
  }

  // 방법 2
  // 제너릭으로 Object[] 타입 명시
  private void scalarProjection2(EntityManager em) {
    Member member = new Member();
    member.setUsername("user1");
    member.setAge(28);
    em.persist(member);

    em.flush();
    em.clear();

    List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from Member m")
        .getResultList();
    Object[] result = resultList.get(0);
    System.out.println("username = " + result[0]);
    System.out.println("age = " + result[1]);
  }

  // 방법 3
  // 제일 깔끔한 방법
  // DTO와 new를 사용한다.
  private void scalarProjection3(EntityManager em) {
    Member member = new Member();
    member.setUsername("user1");
    member.setAge(28);
    em.persist(member);

    em.flush();
    em.clear();

    // 생성자를 호출하듯이!! (주의! 패키지명을 전부 다 적어줘야한다)
    List<MemberDto> resultList = em.createQuery("select new jpql.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
        .getResultList();
    MemberDto memberDto = resultList.get(0);
    System.out.println("username = " + memberDto.getUsername());
    System.out.println("age = " + memberDto.getAge());
  }
}
