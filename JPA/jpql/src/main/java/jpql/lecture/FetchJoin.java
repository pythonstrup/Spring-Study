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
}
