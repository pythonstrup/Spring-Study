package study.querydsl.repository;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;

// Custom Interface에 얽매이지 말자.
// 아래와 같이 특화된 조회용 Repository로 분리할 수도 있다.
@Repository
public class MemberQueryRepository {
  private final JPAQueryFactory queryFactory;

  public MemberQueryRepository(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  public List<MemberTeamDto> search(MemberSearchCondition condition) {
    return queryFactory
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
        )
        .fetch();
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
