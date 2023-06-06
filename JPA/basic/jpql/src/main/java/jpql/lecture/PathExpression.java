package jpql.lecture;

import java.util.Collection;
import javax.persistence.EntityManager;
import jpql.domain.Member;
import jpql.domain.Team;

// 묵시적 조인은 웬만하면 사용하지 말자!!
// 명시적 조인의 튜닝이 훨씬 쉽다. (조인이 SQL 튜닝의 중요 포인트이기 때문이다.)
public class PathExpression {

  // select m.username  => 상태 필드
  // from Member m
  // join m.team t      => 단일 값 연관 필드
  // join m.orders o    => 컬렉션 값 연관 필드
  // where t.name = 'teamA'

  // 상태 필드
  // m.username에서 경로 탐색이 끝나고 더이상 탐색 X
  private void stateField() {
    String query = "select m.username from Member m";
  }

  // 단일 값 연관 경로
  // 아래 예시를 보면 team에서 더 탐색해나갈 수 있는 것을 볼 수 있다.
  // 묵시적 내부 조인 발생 => 조심해서 사용해야 한다. 쿼리 튜닝이 어려워진다.
  // 탐색이 더 뻗어갈 수 있다.
  private void singleValueAssociatedPath() {
    String query = "select m.team.name from Member m";
  }

  // 컬렉션 값 연관 경로
  // 묵시적 내부 조인이 발생하지만 더 이상 탐색을 진행할 수 없다.
  private void collectionValueAssociatedPath(EntityManager em) {
    Team team = new Team();
    team.setName("teamA");
    em.persist(team);

    Member member1 = new Member();
    member1.setUsername("user1");
    member1.setAge(20);
    member1.changeTeam(team);
    em.persist(member1);

    Member member2 = new Member();
    member2.setUsername("user2");
    member2.setAge(21);
    member2.changeTeam(team);
    em.persist(member2);

    em.flush();
    em.clear();

    String query = "select t.members from Team t";
    // members에서 더이상 탐색을 할 수 없다.

    Collection result = em.createQuery(query, Collection.class).getResultList();
    for (Object o : result) {
      System.out.println("o = " + o);
    }

    // .size는 가능하다.
    String size = "select t.members.size from Team t";
    Integer sizeResult = em.createQuery(size, Integer.class).getSingleResult();
    System.out.println("result = " + sizeResult);
  }

  // 컬렉션 값을 보고 싶다면 명시적 join을 해와야한다.
  private void join() {
    String query = "select m.username from Team t join t.members m";
  }
}
