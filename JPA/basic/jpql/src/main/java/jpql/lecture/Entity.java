package jpql.lecture;

import javax.persistence.EntityManager;
import jpql.domain.Member;
import jpql.domain.Team;

public class Entity {

  private void pkValue() {
    String query1 = "select count(m.id) from Member m";
    String query2 = "select count(m) from Member m";
    // JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본키 값을 사용한다.
    // 따라서 아래와 같이 동일한 쿼리가 나가게 된다.
    // select count(m.id) as cnt from Member m
  }

  private void transformEntityByPk(EntityManager em) {
    Member member = new Member();
    member.setId(1L);

    // 엔티티를 파라미터로 전달
    String query1 = "select m from Member m where m = :member";
    em.createQuery(query1)
        .setParameter("member", member)
        .getResultList();


    // 식별자를 직접 전달
    String query2 = "select m from Member m where m.id = :memberId";
    em.createQuery(query2)
        .setParameter("memberId", member.getId())
        .getResultList();


    // 아래와 같은 동일 쿼리 실행
    // select m.* from Member m where m.id=?
  }

  // 엔티티 직접 사용 - 외래키 값 사용
  private void foreignKey(EntityManager em) {
    Team team = new Team();
    em.persist(team);

    Member member = new Member();
    member.changeTeam(team);
    em.persist(member);

    // 엔티티를 파라미터로 전달
    String query1 = "select m from Member m where m.team = :team";
    em.createQuery(query1)
        .setParameter("team", team)
        .getResultList();


    // 식별자를 직접 전달
    String query2 = "select m from Member m where m.team.id = :teamId";
    em.createQuery(query2)
        .setParameter("teamId", member.getTeam().getId())
        .getResultList();
  }
}
