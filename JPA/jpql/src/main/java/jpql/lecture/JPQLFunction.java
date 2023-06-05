package jpql.lecture;

import java.util.List;
import javax.persistence.EntityManager;
import jpql.constants.MemberType;
import jpql.domain.Member;
import jpql.domain.Team;

public class JPQLFunction {

  // JPQL 기본 함수 - 그냥 사용할 수 있다.
  // CONCAT, SUBSTRING, TRIM, LOWER, UPPER, LENGTH, LOCATE, ABS, SQRT, MOD, SIZE, INDEX 등

  // 기본 함수 이외의 함수는 SQL 방언을 추가하고 function() 으로 감싸서 사용해야한다.
  // ex) select function('group_concat', i.name) from Item i

  // 사실 dialect에 기본적인 함수들이 웬만하면 다 등록이 되어있다.


  // 기본 함수
  private void basicFunc(EntityManager em) {
    Member member1 = new Member();
    member1.setUsername("user1");
    member1.setAge(20);
    em.persist(member1);

    Member member2 = new Member();
    member2.setUsername("user1");
    member2.setAge(20);
    em.persist(member2);

    em.flush();
    em.clear();

    String query = "select 'a' || 'b' from Member m "; // 유사 concat
    String concat = "select concat('a', 'b') from Member m ";
    String substring = "select substring(m.username, 2, 4) from Member m ";
    String locate = "select locate('de', 'abcdefg') from Member m "; // Integer Type
    String size = "select size(t.members) from Team t"; // Integer Type

    List<String> resultList = em.createQuery(query, String.class).getResultList();
    for (String s : resultList) {
      System.out.println("s = " + s);
    }
  }

  // 사용자 정의 함수
  private void func(EntityManager em) {
    String query = "select function('group_concat', m.username) from Member m";
  }
}
