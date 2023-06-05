package jpql.lecture;

import java.util.List;
import javax.persistence.EntityManager;
import jpql.constants.MemberType;
import jpql.domain.Member;
import jpql.domain.Team;

public class TypeExpression {

  // Enum의 경우, 자바 Package명을 전부 포함에서 적어줘야 한다.
  private void enumExpression(EntityManager em) {
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

    // 아래와 같이 문자열이나, boolean을 그냥 넣는 것도 가능하다.
    String query = "select m.username, 'HELLO', true from Member m "
        + "where m.memberType = jpql.constants.MemberType.USER";
    List<Object[]> result = em.createQuery(query).getResultList();

    for (Object[] objects : result) {
      System.out.println("objects = " + objects[0]);
      System.out.println("objects = " + objects[1]);
      System.out.println("objects = " + objects[2]);
    }
  }

  // enum 패키지까지 적는 것이 부담스럽다면 아래와 같이 파라미터 세팅으로 처리할 수도 있다.
  private void enumParameterBinding(EntityManager em) {
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

    String query = "select m.username, 'HELLO', true from Member m "
        + "where m.memberType = :memberType";
    List<Object[]> result = em.createQuery(query)
        .setParameter("memberType", MemberType.ADMIN)
        .getResultList();

    for (Object[] objects : result) {
      System.out.println("objects = " + objects[0]);
      System.out.println("objects = " + objects[1]);
      System.out.println("objects = " + objects[2]);
    }
  }
}
