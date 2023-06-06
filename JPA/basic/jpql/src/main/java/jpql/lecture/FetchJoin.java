package jpql.lecture;

import java.util.List;
import javax.persistence.EntityManager;
import jpql.domain.Member;
import jpql.domain.Team;

public class FetchJoin {

  // SQL 조인 종류가 아니다.
  // JPQL에서 성능 최적화를 위해 제공하는 기능이다.
  // 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능이다.

  // 예시 시나리오
  private void scenario(EntityManager em) {

    Team team1 = new Team();
    team1.setName("teamA");
    em.persist(team1);

    Team team2 = new Team();
    team2.setName("teamB");
    em.persist(team2);

    Member member1 = new Member();
    member1.setUsername("user1");
    member1.setAge(20);
    member1.changeTeam(team1);
    em.persist(member1);

    Member member2 = new Member();
    member2.setUsername("user2");
    member2.setAge(21);
    member2.changeTeam(team1);
    em.persist(member2);

    Member member3 = new Member();
    member3.setUsername("user3");
    member3.setAge(28);
    member3.changeTeam(team2);
    em.persist(member3);

    Member member4 = new Member();
    member4.setUsername("user4");
    member4.setAge(24);
    em.persist(member4);

    em.flush();
    em.clear();
  }

  // member4가 없다고 가정
  private void normalSelect(EntityManager em) {
    String query = "select m from Member m";
    List<Member> resultList = em.createQuery(query, Member.class).getResultList();
    for (Member member : resultList) {
      System.out.println("member = " + member.getUsername() + " " + member.getTeam().getName());
    }
    // Team은 Lazy Loading으로 설정되어 있는 상태이다.
    // user1 => teamA(SQL Query)
    // user2 => teamA(1차 캐시)
    // user3 => teamB(SQL Query)
  }


  private void basicFetchJoin(EntityManager em) {

    String query = "select m from Member m join fetch m.team";
    // 실제 쿼리는 아래와 같이 나간다.
    // select m.*, t.* from member m inner join team t on m.team_id=t.id
    // 따라서 team의 값이 null인 member4가 있어도, 일치하는 team이 없기 때문에 값을 가져오지 않는다.

    List<Member> resultList = em.createQuery(query, Member.class).getResultList();
    for (Member member : resultList) {
      System.out.println("member = " + member.getUsername() + " " + member.getTeam().getName());
    }
  }

  private void collectionFetchJoin(EntityManager em) {
    String query = "select t from Team t join fetch t.members";

    List<Team> resultList = em.createQuery(query, Team.class).getResultList();
    for (Team team : resultList) {
      System.out.println("team = " + team.getName() + " members = " +  team.getMembers().size());
      for (Member member : team.getMembers()) {
        System.out.println("member = " + member);
      }
      System.out.println("==========================================");
    }
    // 여기서 똑같은 teamA가 2줄이 받아져버린다는 한계가 있다. 아래는 출력 결과값이다.
    // 참고, join fetch를 하지 않으면 1개만 받아온다!
    /**
     * team = teamA members = 2
     * member = Member{id=3, username='user1', age=20, team=jpql.domain.Team@3a26ec8d}
     * member = Member{id=4, username='user2', age=21, team=jpql.domain.Team@3a26ec8d}
     * ==========================================
     * team = teamA members = 2
     * member = Member{id=3, username='user1', age=20, team=jpql.domain.Team@3a26ec8d}
     * member = Member{id=4, username='user2', age=21, team=jpql.domain.Team@3a26ec8d}
     * ==========================================
     * team = teamB members = 1
     * member = Member{id=5, username='user3', age=28, team=jpql.domain.Team@79a04e5f}
     * ==========================================
     */

    // 만약 1줄로 합쳐주고 싶다면 distinct를 활용하면 된다.
    // 원래 distinct는 지정한 모든 컬럼이 같아야 하지만, JPA는 식별자(id)를 통해 중복된 엔티티를 제거해준다!
    String queryDistinct = "select distinct t from Team t join fetch t.members";
  }

  // 한계
  // Fetch Join 대상에는 별칭을 주면 안된다. (하이버네이트는 가능하지만 가급적 사용하지 않는 것이 좋다.)
  private void alias() { // 하지 말자!
    String query = "select distinct t from Team t join fetch t.members as m where m.age > 10";

    // Fetch 조인을 이어할 때는 별칭을 사용하기도 한다.
    String aliasQuery = "select distinct t from Team t join fetch t.members as m join fetch m.somethings";
  }

  // 둘 이상의 컬렉션은 Fetch Join을 할 수 없다.
  private void collection() {}

  // 컬렉션을 Fetch Join하면 페이징 API(setFirstResult, setMaxResult)를 사용할 수 없다.
  // 일대일, 다대일 같은 단일값 연관 관계는 Fetch Join해도 페이징이 가능하다.
  // 그러나 하이버네이트는 경고 로그를 남기고 메모리에서 페이징하기 때문에 매우 위험하다!
  private void paging(EntityManager em) {
    String query = "select t From Team t join fetch t.members";
    List<Team> resultList = em.createQuery(query, Team.class)
        .setFirstResult(0)
        .setMaxResults(1)
        .getResultList();
    System.out.println("resultList = " + resultList);
  }
  // 방향을 뒤집어서 해결할 수도 있지만.. (그럼 페이징이 잘 안된다.)

  // 아래 예시는 Lazy 로딩을 사용한 방법이기 때문에 성능이 안 좋아진다는 단점이 존재한다.
  // 하지만 @BatchSize 어노테이션을 사용하면 해당 문제를 해결할 수 있다!
  // 해당 객체에 직접 넣어주는 방식(Team 객체 확인) 또는 Global Setting(persistence.xml)으로 가져가는 방법이 있다.
  // BatchSize는 적절한 숫자를 기입하자(되도록 1000 이하)
  private void pagingEx(EntityManager em) {
    String query = "select t From Team t";
    List<Team> resultList = em.createQuery(query, Team.class)
        .setFirstResult(0)
        .setMaxResults(2)
        .getResultList();
    System.out.println("resultList = " + resultList);

    for (Team team : resultList) {
      System.out.println("team = " + team);
      for (Member member : team.getMembers()) {
        System.out.println("member = " + member);
      }
    }
  }
}
