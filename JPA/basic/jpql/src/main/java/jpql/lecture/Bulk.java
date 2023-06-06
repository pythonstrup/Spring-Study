package jpql.lecture;

import javax.persistence.EntityManager;
import jpql.domain.Member;
import jpql.domain.Team;

public class Bulk {

  // 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리를 날린다.
  // 벌크 연산을 먼저 수행한 후에는 꼭! 영속성 콘텍스트를 초기화해줘야 한다!

  private void bulkUpdate(EntityManager em) {
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

    // 자동으로 FLUSH 호출함
    int resultCount = em.createQuery("update Member m set m.age = 20")
        .executeUpdate();
    System.out.println("resultCount = " + resultCount);

    // Flush만 됐기 때문에 1차캐시가 남아있어 20살이 아닌 원래 나이 그대로 뜬다.
    System.out.println("member1.getAge() = " + member1.getAge());
    System.out.println("member2.getAge() = " + member2.getAge());
    System.out.println("member3.getAge() = " + member3.getAge());

    // 아래와 같이 find로 해도 1차캐시의 영향을 받기 때문에 또 업데이트된 값이 뜨지 않는다.
    Member findMember = em.find(Member.class, member1.getId());
    System.out.println("findMember.getAge() = " + findMember.getAge());

    // 때문에 아래와 같이 영속성 컨텍스트를 꼭 초기화해줘야한다.
    em.clear();
  }
}
