package hello.lecture;

import hello.entity.Member;
import hello.entity.Team;
import java.util.List;
import javax.persistence.EntityManager;

// @OneToMany와 @ManyToMany는 Lazy Laoding이 기본
// @OneToOne과 @ManyToOne은 Eager Loading이 기본 => 실무할 때, Lazy 로딩으로 설정해줘야하는 어노테이션
public class LazyAndEager {

  // 실무에서는 가급적 지연로딩만 사용한다.
  // 즉시 로딩을 적용하면 예상하지 못한 SQL을 만들어서 반환해버린다.
  private void lazyOrEagerLoading(EntityManager em) {
    Team team1 = new Team();
    team1.setName("team");
    em.persist(team1);

    Team team2 = new Team();
    team2.setName("team");
    em.persist(team2);

    Member member = new Member();
    member.setUsername("member");
    team1.addMember(member);
    team2.addMember(member);
    em.persist(member);

    em.flush();
    em.clear();

    Member findMember = em.find(Member.class, member.getId());

    // Lazy Loading일 때는 Team이 Proxy로 출력
    // Eager Loading일 때는 Team이 Entity로 출력
    System.out.println("Team = " + findMember.getTeam().getClass());

    System.out.println("=============================");
    System.out.println("findMember's team = " + findMember.getTeam().getName()); // 초기화
  }

  // 즉시 로딩은 JPQL에서 N+1 문제를 발생시킨다.
  private void nPlusOneProblem(EntityManager em) {
    Team team1 = new Team();
    team1.setName("team");
    em.persist(team1);

    Team team2 = new Team();
    team2.setName("team");
    em.persist(team2);

    Member member1 = new Member();
    member1.setUsername("member");
    team1.addMember(member1);
    em.persist(member1);

    Member member2 = new Member();
    member2.setUsername("member");
    team2.addMember(member2);
    em.persist(member2);

    em.flush();
    em.clear();

//      Member findMember = em.find(Member.class, member.getId());

    // SQL: select * from Member
    // SQL: select * from Team where TEAM_ID = ?
    List<Member> members = em.createQuery("select m from Member m", Member.class)
        .getResultList();
  }

  // N+1 문제 해결방법: 일단 모든 연관관계를 Lazy Loading으로 세팅한다!
  // 방법은 fetch join, entity graph(어노테이션 사용), batch size 등이 있다.
  // 아래는 fetch join의 예시이다.
  private void fetchJoin(EntityManager em) {
    List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
        .getResultList();
  }
}
