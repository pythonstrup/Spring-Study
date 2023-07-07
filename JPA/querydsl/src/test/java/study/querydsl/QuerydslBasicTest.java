package study.querydsl;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

  @PersistenceContext
  EntityManager em;

  @PersistenceUnit
  EntityManagerFactory emf;

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
  void group(){
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

  @Test
  void join() {
    List<Member> result = queryFactory
        .selectFrom(member)
//        .join(member.team, team) // inner join
        .leftJoin(member.team, team)
        .where(team.name.eq("teamA"))
        .fetch();

    assertThat(result)
        .extracting("username")
        .containsExactly("member1", "member2");
  }

  /**
   * theta join => 연관관계가 없는 컬럼으로 조인
   */
  @Test
  void thetaJoin() {
    em.persist(new Member("teamA"));
    em.persist(new Member("teamB"));

    List<Member> result = queryFactory
        .select(member)
        .from(member, team)
        .where(member.username.eq(team.name))
        .fetch();

    assertThat(result)
        .extracting("username")
        .containsExactly("teamA", "teamB");
  }

  /**
   * 회원은 모두 조회 => 회원과 팀을 조인할 때 팀 이름이 teamA인 팀만 조인
   * JPQL: select m, t from Member m left join m.team t on t.name = 'teamA'
   */
  @Test
  void joinOnFiltering() {
    // on절은 outer join을 할 때 사용하는 것이 좋다.

    // left join
//    List<Tuple> result = queryFactory
//        .select(member, team)
//        .from(member)
//        .leftJoin(member.team, team)
//          .on(team.name.eq("teamA"))
//        .fetch();

    // inner join = on보다는 where로 처리하자.
    List<Tuple> result = queryFactory
        .select(member, team)
        .from(member)
//        .join(member.team, team)
//          .on(team.name.eq("teamA"))
        .join(member.team, team)
        .where(team.name.eq("teamA")) // 위의 on절의 결과와 똑같다.
        .fetch();

    for (Tuple tuple : result) {
      System.out.println("tuple = " + tuple);
    }
  }

  /**
   * 연관관계가 없는 엔티티 외부조인
   * 회원의 이름 = 팀 이름
   */
  @Test
  void joinOnNoRelation() {
    em.persist(new Member("teamA"));
    em.persist(new Member("teamB"));
    em.persist(new Member("teamC"));

    List<Tuple> result = queryFactory
        .select(member, team)
        .from(member)
//        .leftJoin(team).on(member.username.eq(team.name))
        .join(team).on(member.username.eq(team.name))
        .fetch();

    for (Tuple tuple : result) {
      System.out.println("tuple = " + tuple);
    }
  }

  @Test
  void fetchJoinNo() {
    em.flush();
    em.clear();

    Member findMember = queryFactory
        .selectFrom(member)
        .where(member.username.eq("member1"))
        .fetchOne();

    boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
    assertThat(loaded).as("fetch join 미적용").isFalse();
  }

  @Test
  void fetchJoinUse() {
    em.flush();
    em.clear();

    Member findMember = queryFactory
        .selectFrom(member)
        .join(member.team, team).fetchJoin()
        .where(member.username.eq("member1"))
        .fetchOne();

    boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
    assertThat(loaded).as("fetch join 적용").isTrue();
  }

  // 서브쿼리
  // com.querydsl.jpa.JPAExpressions 사용
  // from 절의 서브쿼리(인라인 뷰)는 지원하지 않는다.
  // 해결방안
  // 1. 서브쿼리를 조인으로 변경한다. (불가능한 경우도 있음)
  // 2. 애플리케이션에서 쿼리를 2번 분리해서 실행한다.
  // 3. nativeSQL을 사용한다. (정말 안되면)
  /**
   * 나이가 가장 많은 회원을 조회
   */
  @Test
  void subQuery() {
    QMember memberSub = new QMember("memberSub");

    List<Member> result = queryFactory
        .selectFrom(member)
        .where(member.age.eq(
            select(memberSub.age.max())
                .from(memberSub)
        ))
        .fetch();

    assertThat(result).extracting("age")
        .containsExactly(40);
  }

  /**
   * 나이가 평균 이상인 회원을 조회
   */
  @Test
  void subQueryGoe() {
    QMember memberSub = new QMember("memberSub");

    List<Member> result = queryFactory
        .selectFrom(member)
        .where(member.age.goe(
            select(memberSub.age.avg())
                .from(memberSub)
        ))
        .fetch();

    assertThat(result).extracting("age")
        .containsExactly(30, 40);
  }

  // 좋은 예제는 아니지만...
  @Test
  void subQueryIn() {
    QMember memberSub = new QMember("memberSub");

    List<Member> result = queryFactory
        .selectFrom(member)
        .where(member.age.in(
            select(memberSub.age)
                .from(memberSub)
                .where(memberSub.age.gt(10))
        ))
        .fetch();

    assertThat(result).extracting("age")
        .containsExactly(20, 30, 40);
  }

  // 평균나이 출력 예제
  @Test
  void selectSubQuery() {
    QMember memberSub = new QMember("memberSub");

    List<Tuple> result = queryFactory
        .select(member.username,
            select(memberSub.age.avg())
                .from(memberSub)
        ).from(member)
        .fetch();

    for (Tuple tuple : result) {
      System.out.println("tuple = " + tuple);
    }
  }

  @Test
  void basicCase() {
    List<String> result = queryFactory
        .select(member.age
            .when(10).then("열살")
            .when(20).then("스무살")
            .otherwise("etc"))
        .from(member)
        .fetch();

    for (String s : result) {
      System.out.println("s = " + s);
    }
  }

  @Test
  void complexCase() {
    List<String> result = queryFactory
        .select(new CaseBuilder()
            .when(member.age.between(0, 20)).then("0~20")
            .when(member.age.between(21, 30)).then("21~30")
            .otherwise("etc")
        ).from(member)
        .fetch();

    for (String s : result) {
      System.out.println("s = " + s);
    }
  }

  // 상수 넣어주기
  @Test
  void constant() {
    List<Tuple> result = queryFactory
        .select(member.username, Expressions.constant("A"))
        .from(member)
        .fetch();

    for (Tuple tuple : result) {
      System.out.println("tuple = " + tuple);
    }
  }

  @Test
  void concat() {
    // {username}_{age}
    // age가 문자가 아니기 때문에 문자열로 변환이 필요!
    // 이 방법은 특히 ENUM을 처리할 때 자주 사용된다!!
    List<String> result = queryFactory
        .select(member.username.concat("_").concat(member.age.stringValue()))
        .from(member)
//        .where(member.username.eq("member1"))
        .fetch();

    for (String s : result) {
      System.out.println("s = " + s);
    }
  }

  @Test
  void simpleProjection() {
    List<String> result = queryFactory
        .select(member.username)
        .from(member)
        .fetch();

    for (String s : result) {
      System.out.println("s = " + s);
    }
  }

  @Test
  void tupleProjection() {
    List<Tuple> result = queryFactory
        .select(member.username, member.age)
        .from(member)
        .fetch();

    for (Tuple tuple : result) {
      String username = tuple.get(member.username);
      Integer age = tuple.get(member.age);
      System.out.println("username = " + username);
      System.out.println("age = " + age);
    }
  }

  // JPQL로 작성하면 생성자 방식만 지원하기 때문에 new를 사용해야하고,
  // 패키지 명을 전부 적어줘야 한다..
  @Test
  void findDtoByJPQL() {
    List<MemberDto> result = em.createQuery(
            "select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
        .getResultList();

    for (MemberDto memberDto : result) {
      System.out.println("memberDto = " + memberDto);
    }
  }

  /**
   * QueryDSL로 DTO 조회하기
   */

  @Test
  void findDtoBySetter() {
    // Setter를 이용한 방식
    // 기본생성자가 필요한 방식이다.
    List<MemberDto> result = queryFactory
        .select(
            Projections.bean(
                MemberDto.class,
                member.username,
                member.age
            )
        ).from(member)
        .fetch();

    for (MemberDto memberDto : result) {
      System.out.println("memberDto = " + memberDto);
    }
  }

  @Test
  void findDtoByField() {
    // 객체의 필드값에 값을 넣어주는 방식
    // 생성자도 Setter 도 필요 없다. => 그럼 어떻게 하는거지? 리플렉션인가?
    List<MemberDto> result = queryFactory
        .select(
            Projections.fields(
                MemberDto.class,
                member.username,
                member.age
            )
        ).from(member)
        .fetch();

    for (MemberDto memberDto : result) {
      System.out.println("memberDto = " + memberDto);
    }
  }

  @Test
  void findDtoByConstructor() {
    // 생성자를 사용하는 방식
    // 생성자 파라미터의 인자와 일치하지 않으면 에러가 발생한다.
    List<MemberDto> result = queryFactory
        .select(
            Projections.constructor(
                MemberDto.class,
                member.username,
                member.age
            )
        ).from(member)
        .fetch();

    for (MemberDto memberDto : result) {
      System.out.println("memberDto = " + memberDto);
    }
  }

  @Test
  void findUserDto1() {
    // 필드로 가져오려고 할 때 필드명이 일치하지 않는다면?
    List<UserDto> result = queryFactory
        .select(
            Projections.fields(
                UserDto.class,
                member.username.as("name"),
                member.age
            )
        ).from(member)
        .fetch();

    for (UserDto userDto : result) {
      System.out.println("userDto = " + userDto);
    }
  }

  @Test
  void findUserDto2() {
    // 서브 쿼리를 사용하는 방법
    // ExpressionUtils.as 사용
    QMember memberSub = new QMember("memberSub");

    List<UserDto> result = queryFactory
        .select(
            Projections.fields(
                UserDto.class,
                ExpressionUtils.as(member.username, "name"), // 서브쿼리가 아닌 필드에서도 사용이 가능하다.
                ExpressionUtils.as( // 서브쿼리는 다른 방법이 없음.
                    JPAExpressions
                        .select(memberSub.age.max())
                        .from(memberSub), "age")
            )
        ).from(member)
        .fetch();

    for (UserDto userDto : result) {
      System.out.println("userDto = " + userDto);
    }
  }

  @Test
  void findUserDto3() {
    // 생성자를 사용하는 방식
    // 필드명이 달라도 타입만 같으면 문제없다.
    List<UserDto> result = queryFactory
        .select(
            Projections.constructor(
                UserDto.class,
                member.username,
                member.age
            )
        ).from(member)
        .fetch();

    for (UserDto userDto : result) {
      System.out.println("userDto = " + userDto);
    }
  }

  @Test
  void findDtoByQueryProjection() {
    // 이 방법을 사용하려면?
    // 해당 DTO의 생성자 상단에 @QeuryProjection 어노테이션을 추가하고 gradle에서 compileJava를 해준다.
    // Projection.constructor는 파라미터를 잘 못넣었을 때, 컴파일 단계에서 오류를 잡지 못하고 런타임에서 에러가 터진다. (타입, 파라미터 갯수 문제 등)
    // 반면 @QueryProjection 방식은 컴파일 타임에 에러를 잡아준다는 장점이 있다.
    List<MemberDto> result = queryFactory
        .select(new QMemberDto(member.username, member.age))
        .from(member)
        .fetch();

    for (MemberDto memberDto : result) {
      System.out.println("memberDto = " + memberDto);
    }
    // 단점
    // Q-File을 생성해줘야한다.
    // DTO가 QueryDSL에 의존성을 가지게 되어버린다. => 예를 들어, QueryDSL 라이브러리를 뺏을 때 DTO에서도 문제가 생긴다는 얘기다.
    // DTO는 Controller, Service, Repository 가리지 않고 모든 곳에서 사용되는 중요한 요소인데, 해당 어노테이션으로 인해 순수하지 않은 객체가 되어버릴 수 있다.
  }


  /**
   * 동적쿼리
   */
  @Test
  void dynamicQuery_BooleanBuilder() {
    String usernameParam = "member1";
//    Integer ageParam = 10;
    Integer ageParam = null;

    List<Member> result = searchMember1(usernameParam, ageParam);
    assertThat(result.size()).isEqualTo(1);
  }

  private List<Member> searchMember1(String usernameCond, Integer ageCond) {
    BooleanBuilder builder = new BooleanBuilder();
    if (usernameCond != null) {
      builder.and(member.username.eq(usernameCond));
    }

    if (ageCond != null) {
      builder.and(member.age.eq(ageCond));
    }

    return queryFactory
        .selectFrom(member)
        .where(builder)
        .fetch();
  }

  @Test
  void dynamicQuery_WhereParam() {
    String usernameParam = "member1";
    Integer ageParam = 10;
//    Integer ageParam = null;

    List<Member> result = searchMember2(usernameParam, ageParam);
    assertThat(result.size()).isEqualTo(1);
  }

  private List<Member> searchMember2(String usernameCond, Integer ageCond) {
    return queryFactory
        .selectFrom(member)
        .where(allEq(usernameCond, ageCond)) // where에 null이 넘어오면 무시된다!
        .fetch();
  }

  private BooleanExpression usernameEq(String usernameCond) {
    return usernameCond != null ? member.username.eq(usernameCond): null;
  }

  private BooleanExpression ageEq(Integer ageCond) {
    return ageCond != null? member.age.eq(ageCond): null;
  }

  private BooleanExpression allEq(String usernameCond, Integer ageCond) {
    return usernameEq(usernameCond).and(ageEq(ageCond));
  }

  /**
   * 벌크연산
   */
  @Test
  void bulkUpdate() {
    long count = queryFactory
        .update(member)
        .set(member.username, "비회원")
        .where(member.age.lt(28))
        .execute();

    // 주의해야할 점
    // 벌크연산을 하면 영속성 컨텍스트를 무시하고 DB에 영향을 주기 때문에 DB의 값과 영속성 컨텍스트의 값의 괴리가 생긴다.
    // 벌크연산을 실행하고 나면 꼭 영속성 컨텍스트를 초기화해줘야한다.
    em.flush();
    em.clear();
  }

  @Test
  void bulkAdd() {
    long count = queryFactory
        .update(member)
//        .set(member.age, member.age.add(1))
        .set(member.age, member.age.add(-1))
        .execute();
  }

  @Test
  void bulkDelete() {
    long count = queryFactory
        .delete(member)
        .where(member.age.gt(18))
        .execute();
  }

  @Test
  void sqlFunction() {
    List<String> result = queryFactory
        .select(
            Expressions.stringTemplate(
                "function('replace', {0}, {1}, {2})",
                member.username,
                "member",
                "M"
            )
        ).from(member)
        .fetch();

    for (String s : result) {
      System.out.println("s = " + s);
    }
  }

  @Test
  void sqlFunction2() {
    List<String> result = queryFactory
        .select(member.username)
        .from(member)
//        .where(member.username.eq(
//            Expressions.stringTemplate("function('lower', {0})", member.username)))
        .where(member.username.eq(member.username.lower()))
        .fetch();

    for (String s : result) {
      System.out.println("s = " + s);
    }
  }
}
