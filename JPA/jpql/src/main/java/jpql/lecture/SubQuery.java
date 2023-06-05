package jpql.lecture;

import javax.persistence.EntityManager;

public class SubQuery {

  // JPA는 기본적으로 WHERE, HAVING 절에서만 서브쿼리 사용 가능
  // 하이버네이트에서 SELECT 절도 지원


  private void selectSubQuery(EntityManager em) {
    String query = "select (select avg(m1.age) from Member m1) as avgAge "
        + "from Member m left join Team t on m.username = t.name";
  }

  // FROM 절의 서브 쿼리는 현재 JPQL에서 불가능
  // JOIN으로 풀 수 있으면 풀어서 해결해야 한다.
  // 정 안되면 Native Query를 사용해야 한다.
  private void fromSubQuery(EntityManager em) {
    String query = "select mm.age, mm.username "
        + "from (select m.age, m.username from Member m) as mm"; // 불가!!!!!
  }
}
