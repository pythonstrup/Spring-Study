package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
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

  @PersistenceContext
  EntityManager em;

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

  @Test
  void paging() {
    // given
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 10));
    memberRepository.save(new Member("member4", 10));
    memberRepository.save(new Member("member5", 10));
    memberRepository.save(new Member("member6", 10));

    int age = 10;
    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));

    // 리스트로 받을 수도 있지만, 대신 Page의 기능을 사용하지 못한다!
    // when1
    Page<Member> page = memberRepository.findByAge(age, pageRequest);
    // page to dto => Entity를 그대로 노출하지 않도록 하자!!!
    Page<MemberDto> dtoList = page.map(
        member -> new MemberDto(member.getId(), member.getUsername(), null));
    // then1
    List<Member> content = page.getContent();
    long totalElements = page.getTotalElements();
    assertThat(content.size()).isEqualTo(3);
    assertThat(totalElements).isEqualTo(6);
    assertThat(page.getNumber()).isEqualTo(0);
    assertThat(page.getTotalPages()).isEqualTo(2);
    assertThat(page.isFirst()).isTrue();
    assertThat(page.hasNext()).isTrue();


    // slice는 total count를 가져오지 않는다. (쿼리 안 날림)
    // 대신 limit에서 하나 더 가져와서 다음 페이지가 있는지 확인한다.
    // when2
    Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);
    // then2
    List<Member> contentSlice = slice.getContent();
    assertThat(contentSlice.size()).isEqualTo(3);
    assertThat(slice.getNumber()).isEqualTo(0);
    assertThat(slice.isFirst()).isTrue();
    assertThat(slice.hasNext()).isTrue();


    // 사실 totalCount가 성능상 문제가 될 수 있는 부분이다.
    // @Query 어노테이션을 통해 본 쿼리와 카운트 쿼리를 분리할 수 있다. 카운트 쿼리에서 불필요한 조인을 하지 않도록 만들어 성능을 높여줄 수 있다.
    // when3
    Page<Member> dividedQuery = memberRepository.findPageQueryByAge(age, pageRequest);
    // then3
    List<Member> dividedQueryContent = page.getContent();
    long dividedQueryCount = page.getTotalElements();
    assertThat(dividedQueryContent.size()).isEqualTo(3);
    assertThat(dividedQueryCount).isEqualTo(6);
  }

  @Test
  void bulkUpdate() {
    // given
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 19));
    memberRepository.save(new Member("member3", 20));
    memberRepository.save(new Member("member4", 21));
    memberRepository.save(new Member("member5", 40));

    // when
    int resultCount = memberRepository.bulkAgePlus(20);

    // 벌크연산을 하면 DB에만 업데이트되고 영속성 컨텍스트에서는 적용이 안되기 때문에 초기화작업을 해줘야한다.
//    em.flush(); // 변경사항 DB 저장
//    em.clear(); // 초기화
    // @Modifying의 옵션 중 clearAutomatically = true를 설정하면 자동으로 초기화 작업을 해준다!

    List<Member> findMember = memberRepository.findByUsername("member5");
    Member member5 = findMember.get(0);
    System.out.println("member5.age = " + member5.getAge());

    // then
    assertThat(resultCount).isEqualTo(3);
  }

  @Test
  void findMemberLazy() throws Exception {
    // given
    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    teamRepository.save(teamA);
    teamRepository.save(teamB);

    Member member1 = new Member("member1", 20, teamA);
    Member member2 = new Member("member2", 28, teamB);
    memberRepository.save(member1);
    memberRepository.save(member2);

    em.flush();
    em.clear();

    // when
    // N+1 Problem => entity graph 사용하기 전
//    List<Member> members = memberRepository.findAll();
//    for (Member member : members) {
//      System.out.println("member.getUsername() = " + member.getUsername());
//      System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
//      System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
//    }

    // Fetch Join
//    List<Member> fetchJoinResult = memberRepository.findMemberFetchJoin();
    // 페치 조인을 하면 Team이 Proxy 객체가 아닌 '진찌' Entity 객체가 반환된다.
//    for (Member member : fetchJoinResult) {
//      System.out.println("member.getUsername() = " + member.getUsername());
//      System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
//      System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
//    }

    // @EntityGraph 어노테이션을 추가하면?
    List<Member> entityGraphResult = memberRepository.findAll();
    for (Member member : entityGraphResult) {
      System.out.println("member.getUsername() = " + member.getUsername());
      System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
      System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
    }

    List<Member> findMember = memberRepository.findEntityGraphByUsername("member1");
    System.out.println("findMember = " + findMember);
  }

  @Test
  void queryHint() {
    // given
    Member member1 = new Member("member1", 20);
    memberRepository.save(member1);
    em.flush();
    em.clear();

    // when
//    Member findMember = memberRepository.findById(member1.getId()).get();
//    findMember.setUsername("member2");
//    em.flush();

    // 더티 체킹의 치명적인 단점
    // 원본이 있어야 한다. (원본이 있어야 비교가 가능하고 변경감지를 할 수 있기 때문에)
    // 때문에 객체를 2개로 나눠 관리해야한다. => 메모리를 먹는다.
    // 만약 read only로만 사용할 거라면 @QueryHint를 사용하면 된다.
    // 해당 기능은 JPA에서 제공하는 것이 아니라 Hibernate가 제공하는 것이다.
    // JPA에서는 이를 사용할 수 있도록 '구멍'을 열어줬는데 이게 "query hint"다
    Member findMember = memberRepository.findReadOnlyByUsername("member1");
    findMember.setUsername("member2");
    em.flush();
    em.clear();

    // read only이기 때문에 더티체킹이 작동하지 않고 결과적으로 테이블에 변경이 일어나지 않는다.
    Member afterMember = memberRepository.findById(findMember.getId()).get();
    System.out.println("afterMember.getUsername() = " + afterMember.getUsername());
  }

  @Test
  void testLock() {
    // given
    Member member1 = new Member("member1", 20);
    memberRepository.save(member1);
    em.flush();
    em.clear();

    // 실시간 서비스가 많은 프로그램은 락을 최대한 안 거는 것이 좋다.
    List<Member> findMember = memberRepository.findLockByUsername("member1");
    // select m1_0.member_id,m1_0.age,m1_0.team_id,m1_0.username
    // from member m1_0 where m1_0.username='member1' for update
  }

  @Test
  void callCustom() {
    memberRepository.save(new Member("member1", 19));
    memberRepository.save(new Member("member2", 20));
    List<Member> result = memberRepository.findMemberCustom();
    for (Member member : result) {
      System.out.println("member = " + member);
    }
  }

  @Test
  void specBasic() {
    // given
    Team teamA = new Team("teamA");
    em.persist(teamA);

    Member m1 = new Member("m1", 0, teamA);
    Member m2 = new Member("m2", 0, teamA);
    em.persist(m1);
    em.persist(m2);

    em.flush();
    em.clear();

    // when
    Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
    List<Member> result = memberRepository.findAll(spec);

    // then
    Assertions.assertThat(result.size()).isEqualTo(1);
  }
}