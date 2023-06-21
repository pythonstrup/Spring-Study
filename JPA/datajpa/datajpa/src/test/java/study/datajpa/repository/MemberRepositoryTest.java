package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  TeamRepository teamRepository;

  @Test
  public void testMember() {
    // given
    Member member = new Member("userA");

    // when
    Member savedMember = memberRepository.save(member);

    // then
    Member findMember = memberRepository.findById((savedMember.getId())).get();

    Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
    Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    Assertions.assertThat(findMember).isEqualTo(member);
  }

  @Test
  void basicCRUD() {
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");
    memberRepository.save(member1);
    memberRepository.save(member2);

    // 단건 조회
    Member findMember1 = memberRepository.findById(member1.getId()).get();
    Member findMember2 = memberRepository.findById(member2.getId()).get();
    assertThat(findMember1).isEqualTo(member1);
    assertThat(findMember2).isEqualTo(member2);

    // 리스트 조회 검증
    List<Member> all = memberRepository.findAll();
    assertThat(all.size()).isEqualTo(2);

    // 카운트 검증
    long count = memberRepository.count();
    assertThat(count).isEqualTo(2);

    // 삭제 검증
    memberRepository.delete(member1);
    memberRepository.delete(member2);
    long deletedCount = memberRepository.count();
    assertThat(deletedCount).isEqualTo(0);
  }

  @Test
  void findByUsernameAndAgeGreaterThan() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("AAA", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

    assertThat(result.get(0).getUsername()).isEqualTo("AAA");
    assertThat(result.get(0).getAge()).isEqualTo(20);
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  void testNamedQuery() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("AAA", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> result = memberRepository.findByUsername("AAA");
    Member findMember = result.get(0);
    assertThat(findMember).isEqualTo(member1);
  }

  @Test
  void testQuery() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("AAA", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> result = memberRepository.findUser("AAA", 10);
    assertThat(result.size()).isEqualTo(1);

    Member findMember = result.get(0);
    assertThat(findMember).isEqualTo(member1);
  }

  @Test
  void findUsernameList() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("BBB", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);

    List<String> usernameList = memberRepository.findByUsernameList();
    assertThat(usernameList.get(0)).isEqualTo("AAA");
    assertThat(usernameList.get(1)).isEqualTo("BBB");
  }

  @Test
  void findMemberDto() {
    Team team = new Team("teamA");
    teamRepository.save(team);

    Member member1 = new Member("AAA", 10);
    member1.changeTeam(team);
    memberRepository.save(member1);

    List<MemberDto> memberDto = memberRepository.findMemberDto();
    assertThat(memberDto.get(0).getId()).isEqualTo(member1.getId());
    assertThat(memberDto.get(0).getUsername()).isEqualTo(member1.getUsername());
    assertThat(memberDto.get(0).getTeamName()).isEqualTo(team.getName());
  }

  @Test
  void findByNames() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("BBB", 20);
    Member member3 = new Member("CCC", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);

    List<Member> findMembers = memberRepository.findByNames(List.of("AAA", "CCC"));
    assertThat(findMembers.size()).isEqualTo(2);
    assertThat(findMembers.get(0).getUsername()).isEqualTo("AAA");
    assertThat(findMembers.get(1).getUsername()).isEqualTo("CCC");
  }

  @Test
  void testReturnType() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("BBB", 20);
    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> memberList = memberRepository.findListByUsername("AAA");

    // 만약 결과값이 2개 이상 가져와지면 오류가 발생한다.
    // query did not return a unique result: 2
    Member findMember = memberRepository.findMemberByUsername("AAA");
    Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");
    assertThat(memberList).isExactlyInstanceOf(ArrayList.class);
    assertThat(findMember).isExactlyInstanceOf(Member.class);
    assertThat(optionalMember).isExactlyInstanceOf(Optional.class);

    List<Member> emptyList = memberRepository.findListByUsername("a");
    assertThat(emptyList).isExactlyInstanceOf(ArrayList.class);
    assertThat(emptyList.size()).isEqualTo(0);

    Member emptyMember = memberRepository.findMemberByUsername("a");
    assertThat(emptyMember).isNull();

    Optional<Member> emptyOptionalMember = memberRepository.findOptionalByUsername("a");
    assertThat(emptyOptionalMember).isExactlyInstanceOf(Optional.class);
    assertThat(emptyOptionalMember.isEmpty()).isTrue();
  }
}