package study.querydsl;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

  @PersistenceContext
  EntityManager em;

  JPAQueryFactory queryFactory;

  @BeforeEach
  public void before() {
    queryFactory = new JPAQueryFactory(em);

    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    em.persist(teamA);
    em.persist(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 20, teamA);

    Member member3 = new Member("member3", 30, teamB);
    Member member4 = new Member("member4", 40, teamB);
    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    em.persist(member4);
  }

  @Test
  public void startJPQL() {
    // member1 찾기
    String qlString = "select m from Member m where m.username = :username";
    Member findMember = em.createQuery(qlString, Member.class)
        .setParameter("username", "member1")
        .getSingleResult();

    assertThat(findMember.getUsername()).isEqualTo("member1");
  }

  @Test
  public void startQuerydsl() {
//    QMember member = new QMember("m");
//    QMember member = QMember.member;
    // 사실 그냥 스태틱 import 사용하는게 최고임

    Member findMember = queryFactory
        .select(member)
        .from(member)
        .where(member.username.eq("member1"))
        .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");
  }

  @Test
  public void search() {
    Member findMember = queryFactory
        .selectFrom(member)
        .where(member.username.eq("member1")
            .and(member.age.eq(10)))
        .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");
    assertThat(findMember.getAge()).isEqualTo(10);

    // 검색조건
    // member.username.eq("member1") => username = 'member1'
    // member.username.ne("member1") => username != 'member1'
    // member.username.eq("member1").not() => username != 'member1'
    // member.username.isNotNull() => username IS NOT NULL
    // member.age.in(10, 20) => age in (10, 20)
    // member.age.notIn(10, 20) => age not in (10, 20)
    // member.age.between(10, 30) => age between 10 and 30
    // member.age.goe(30) => age >= 30
    // member.age.gt(30) => age > 30
    // member.age.loe(30) => age <= 30
    // member.age.lt(30) => age < 30
    // member.username.like("member%") => username like 'member%'
    // member.username.contains("member") => username like '%member%'
    // member.username.startsWith("member") => username like 'member%'
  }

  @Test
  public void searchAndParam() {
    Member findMember = queryFactory
        .selectFrom(member)
        .where(
            member.username.eq("member1"),
            member.age.eq(10)
        ) // .and가 아닌 그냥 ,로 묶는 방법 => 결과는 똑같다.
        .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");
  }

  @Test
  public void resultFetch() {
    List<Member> fetch = queryFactory
        .selectFrom(member)
        .fetch();

    Member fetchOne = queryFactory
        .selectFrom(member)
        .where(member.username.eq("member1" ))
        .fetchOne();

    Member fetchFirst = queryFactory
        .selectFrom(member)
        .fetchFirst();

    // 사용하지 말자.
    // Deprecated!!
//    QueryResults<Member> queryResults = queryFactory
//        .selectFrom(member)
//        .fetchResults();
//    long totalCount = queryResults.getTotal();
//    List<Member> results = queryResults.getResults();
//    assertThat(totalCount).isEqualTo(4);
//    assertThat(results.get(0).getUsername()).isEqualTo("member1");

    // Deprecated
//    long count = queryFactory
//        .selectFrom(member)
//        .fetchCount();
//    assertThat(count).isEqualTo(4);
  }

  /**
   * age 내림차순
   * username 오름차순 => username 없으면 마지막에 출력 (nulls last)
   */
  @Test
  public void sort() {
      em.persist(new Member(null, 50));
    em.persist(new Member("member5", 50));
    em.persist(new Member("member6", 50));

    List<Member> result = queryFactory
        .selectFrom(member)
        .where(member.age.eq(50))
        .orderBy(member.age.desc(), member.username.asc().nullsLast())
        .fetch();

    Member member5 = result.get(0);
    Member member6 = result.get(1);
    Member memberNull = result.get(2);
    assertThat(member5.getUsername()).isEqualTo("member5");
    assertThat(member6.getUsername()).isEqualTo("member6");
    assertThat(memberNull.getUsername()).isNull();
  }

  @Test
  public void paging1() {
    List<Member> result = queryFactory
        .selectFrom(member)
        .orderBy(member.username.desc())
        .offset(1)
        .limit(2)
        .fetch();

    assertThat(result.size()).isEqualTo(2);
  }

  @Test
  public void paging2() {
    // 페이징할 때, fetchResults() 대신 아래의 코드를 사용하는 것을 추천
    List<Member> result = queryFactory
        .selectFrom(member)
        .orderBy(member.username.desc())
        .offset(1)
        .limit(2)
        .fetch();

    Long totalCount = queryFactory
        .select(Wildcard.count)
        .from(member)
        .fetch().get(0);

    assertThat(totalCount).isEqualTo(4);
    assertThat(result.size()).isEqualTo(2);
    assertThat(result);
  }

  @Test
  public void aggregation() {
    List<Tuple> result = queryFactory
        .select(
            member.count(),
            member.age.sum(),
            member.age.avg(),
            member.age.max(),
            member.age.min()
        )
        .from(member)
        .fetch();

    // 실무에서는 튜플보다는 dto를 사용해 정보를 뽑아온다.
    Tuple tuple = result.get(0);
    System.out.println("tuple = " + tuple);

    assertThat(tuple.get(member.count())).isEqualTo(4);
    assertThat(tuple.get(member.age.sum())).isEqualTo(100);
    assertThat(tuple.get(member.age.avg())).isEqualTo(25);
    assertThat(tuple.get(member.age.max())).isEqualTo(40);
    assertThat(tuple.get(member.age.min())).isEqualTo(10);
  }

  /**
   * 팀의 이름과 각 팀의 평균 연령
   */
  @Test
  void group() throws Exception {
    List<Tuple> result = queryFactory
        .select(team.name, member.age.avg())
        .from(member)
        .join(member.team, team)
        .groupBy(team.name)
        .fetch();

    Tuple teamA = result.get(0);
    Tuple teamB = result.get(1);

    assertThat(teamA.get(team.name)).isEqualTo("teamA");
    assertThat(teamA.get(member.age.avg())).isEqualTo(15);

    assertThat(teamB.get(team.name)).isEqualTo("teamB");
    assertThat(teamB.get(member.age.avg())).isEqualTo(35);
  }
}
