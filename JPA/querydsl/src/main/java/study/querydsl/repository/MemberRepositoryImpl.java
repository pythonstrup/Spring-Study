package study.querydsl.repository;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;

public class MemberRepositoryImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

//  private final JPAQueryFactory queryFactory;
//  public MemberRepositoryImpl(EntityManager em) {
//    this.queryFactory = new JPAQueryFactory(em);
//  }


  // QuerydslRepositorySupport가 제공하는 기능
  // 직접 EntityManager를 주입받을 필요가 없다.
  private final EntityManager em;
  private final JPAQueryFactory queryFactory;
  public MemberRepositoryImpl() {
    super(Member.class);

    em = super.getEntityManager();
    queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<MemberTeamDto> search(MemberSearchCondition condition) {
    return from(member)
        .leftJoin(member.team, team)
        .where(
            usernameEq(condition.getUsername()),
            teamNameEq(condition.getTeamName()),
            ageLoe(condition.getAgeLoe()),
            ageGoe(condition.getAgeGoe())
        ).select(new QMemberTeamDto(
            member.id.as("memberId"),
            member.username,
            member.age,
            team.id.as("teamId"),
            team.name.as("teamName")
        )).fetch();

//    return queryFactory
//        .select(new QMemberTeamDto(
//            member.id.as("memberId"),
//            member.username,
//            member.age,
//            team.id.as("teamId"),
//            team.name.as("teamName")
//        ))
//        .from(member)
//        .leftJoin(member.team, team)
//        .where(
//            usernameEq(condition.getUsername()),
//            teamNameEq(condition.getTeamName()),
//            ageLoe(condition.getAgeLoe()),
//            ageGoe(condition.getAgeGoe())
//        )
//        .fetch();
  }

  @Override
  public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
    // fetchResults는 deprecated 되었다.
    // 복잡한 쿼리에 대해서 어려 오류 사례를 만들었기 때문이다.
    // 따라서 아래 방식은 웬만하면 사용하지 말자.
    QueryResults<MemberTeamDto> result = queryFactory
        .select(new QMemberTeamDto(
            member.id.as("memberId"),
            member.username,
            member.age,
            team.id.as("teamId"),
            team.name.as("teamName")
        ))
        .from(member)
        .leftJoin(member.team, team)
        .where(
            usernameEq(condition.getUsername()),
            teamNameEq(condition.getTeamName()),
            ageLoe(condition.getAgeLoe()),
            ageGoe(condition.getAgeGoe())
        ).offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<MemberTeamDto> content = result.getResults();
    long total = result.getTotal();

    return new PageImpl<>(content, pageable, total);
  }

  // 치명적인 단점
  // from으로 시작해야한다. => 익숙하지 않은 컨벤션
  // Sort가 지원이 안된다..?
  // Method Chain이 끊겨버린다..
  public Page<MemberTeamDto> searchPageSimple2(MemberSearchCondition condition, Pageable pageable) {
    JPQLQuery<MemberTeamDto> jpqlQuery = from(member)
        .leftJoin(member.team, team)
        .where(
            usernameEq(condition.getUsername()),
            teamNameEq(condition.getTeamName()),
            ageLoe(condition.getAgeLoe()),
            ageGoe(condition.getAgeGoe())
        ).select(new QMemberTeamDto(
            member.id.as("memberId"),
            member.username,
            member.age,
            team.id.as("teamId"),
            team.name.as("teamName")
        ));

    JPQLQuery<MemberTeamDto> query = getQuerydsl().applyPagination(pageable, jpqlQuery);
    QueryResults<MemberTeamDto> result = query.fetchResults();
    List<MemberTeamDto> content = result.getResults();
    long total = result.getTotal();
    return new PageImpl<>(content, pageable, total);
  }

  @Override
  public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
    // fetchResult 대신 아래와 같은 방법을 사용하는 것을 권장
    List<MemberTeamDto> result = queryFactory
        .select(new QMemberTeamDto(
            member.id.as("memberId"),
            member.username,
            member.age,
            team.id.as("teamId"),
            team.name.as("teamName")
        ))
        .from(member)
        .leftJoin(member.team, team)
        .where(
            usernameEq(condition.getUsername()),
            teamNameEq(condition.getTeamName()),
            ageLoe(condition.getAgeLoe()),
            ageGoe(condition.getAgeGoe())
        ).offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPAQuery<Long> countQuery = queryFactory
        .select(Wildcard.count)
        .from(member)
        .leftJoin(member.team, team)
        .where(
            usernameEq(condition.getUsername()),
            teamNameEq(condition.getTeamName()),
            ageLoe(condition.getAgeLoe()),
            ageGoe(condition.getAgeGoe())
        );

//    return new PageImpl<>(result, pageable, totalCount);
    return PageableExecutionUtils.getPage(result, pageable, () -> countQuery.fetch().get(0));
  }

  private BooleanExpression usernameEq(String username) {
    return StringUtils.hasText(username) ? member.username.eq(username): null;
  }

  private BooleanExpression teamNameEq(String teamName) {
    return StringUtils.hasText(teamName) ? team.name.eq(teamName): null;
  }

  private BooleanExpression ageGoe(Integer ageGoe) {
    return ageGoe != null ? member.age.goe(ageGoe): null;
  }

  private BooleanExpression ageLoe(Integer ageLoe) {
    return ageLoe != null ? member.age.loe(ageLoe): null;
  }
}
