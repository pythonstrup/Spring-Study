package jpql.lecture;

import java.util.List;
import javax.persistence.EntityManager;
import jpql.constants.MemberType;
import jpql.domain.Member;
import jpql.domain.Team;

public class Case {

  private void basicCase(EntityManager em) {
    Team team = new Team();
    team.setName("teamA");
    em.persist(team);

    Member member = new Member();
    member.setUsername("user1");
    member.setAge(20);
    member.changeTeam(team);
    member.setMemberType(MemberType.ADMIN);
    em.persist(member);

    em.flush();
    em.clear();

    String query =
        "select "
            + "case when m.age <= 10 then 'student fee' "
            + "when m.age >= 60 then 'senior fee' "
            + "else 'normal fee' "
            + "end "
            + "from Member m";
    List<String> resultList = em.createQuery(query, String.class).getResultList();
    for (String s : resultList) {
      System.out.println("s = " + s);
    }
  }

  private void coalesce(EntityManager em) {
    Team team = new Team();
    team.setName("teamA");
    em.persist(team);

    Member member = new Member();
    member.setUsername(null);
    member.setAge(20);
    member.changeTeam(team);
    member.setMemberType(MemberType.ADMIN);
    em.persist(member);

    em.flush();
    em.clear();

    String query =
        "select coalesce(m.username, 'non-named member') as username "
            + "from Member m ";
    List<String> resultList = em.createQuery(query, String.class).getResultList();
    for (String s : resultList) {
      System.out.println("s = " + s);
    }
  }


  private void nullIf(EntityManager em) {
    Team team = new Team();
    team.setName("teamA");
    em.persist(team);

    Member member = new Member();
    member.setUsername("user1");
    member.setAge(20);
    member.changeTeam(team);
    member.setMemberType(MemberType.ADMIN);
    em.persist(member);

    em.flush();
    em.clear();

    // 만약 사용자 이름이 user123이면 null을 반환
    String query =
        "select nullif(m.username, 'user123') as username from Member m ";
    List<String> resultList = em.createQuery(query, String.class).getResultList();
    for (String s : resultList) {
      System.out.println("s = " + s);
    }
  }
}
