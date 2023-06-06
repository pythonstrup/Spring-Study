package jpql.lecture;

import java.util.List;
import javax.persistence.EntityManager;
import jpql.domain.Member;
import jpql.domain.Team;

public class Join {

  private void innerJoin(EntityManager em) {
    Team team = new Team();
    team.setName("teamA");
    em.persist(team);

    Member member = new Member();
    member.setUsername("user1");
    member.setAge(20);
    member.changeTeam(team);
    em.persist(member);

    em.flush();
    em.clear();

    String query = "select m from Member m inner join m.team t";
    List<Member> resultList = em.createQuery(query, Member.class)
        .getResultList();
  }

  private void leftOuterJoin(EntityManager em) {
    Team team = new Team();
    team.setName("teamA");
    em.persist(team);

    Member member = new Member();
    member.setUsername("user1");
    member.setAge(20);
    member.changeTeam(team);
    em.persist(member);

    em.flush();
    em.clear();

    String query = "select m from Member m left join m.team t";
    List<Member> resultList = em.createQuery(query, Member.class)
        .getResultList();
  }

  private void thetaJoin(EntityManager em) {
    Team team = new Team();
    team.setName("teamA");
    em.persist(team);

    Member member = new Member();
    member.setUsername("user1");
    member.setAge(20);
    member.changeTeam(team);
    em.persist(member);

    em.flush();
    em.clear();

    String query = "select m, t from Member m, Team t where m.team.name = t.name";
    List<Member> resultList = em.createQuery(query, Member.class)
        .getResultList();

    System.out.println("resultList.size() = " + resultList.size());
  }

  private void leftJoinWithRelation(EntityManager em) {
    Team team = new Team();
    team.setName("teamA");
    em.persist(team);

    Member member = new Member();
    member.setUsername("user1");
    member.setAge(20);
    member.changeTeam(team);
    em.persist(member);

    em.flush();
    em.clear();

    String query = "select m from Member m left join m.team t on t.name = 'teamA'";
    List<Member> resultList = em.createQuery(query, Member.class)
        .getResultList();

    System.out.println("resultList.size() = " + resultList.size());
  }

  // 연관관계 없는 엔티티 외부 조인
  private void leftJoinWithoutRelation(EntityManager em) {
    Team team = new Team();
    team.setName("teamA");
    em.persist(team);

    Member member = new Member();
    member.setUsername("user1");
    member.setAge(20);
    member.changeTeam(team);
    em.persist(member);

    em.flush();
    em.clear();

    String query = "select m from Member m left join Team t on m.username = t.name";
    List<Member> resultList = em.createQuery(query, Member.class)
        .getResultList();

    System.out.println("resultList.size() = " + resultList.size());
  }
}
