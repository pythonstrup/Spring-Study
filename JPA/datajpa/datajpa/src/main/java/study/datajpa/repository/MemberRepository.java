package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.custom.MemberCustomRepository;
import study.datajpa.repository.projections.MemberProjection;
import study.datajpa.repository.projections.UsernameOnly;
import study.datajpa.repository.projections.UsernameOnlyDto;

// 구현체를 따로 만든 적이 없는데, 해당 객체를 어떻게 사용할 수 있는 걸까?
// Spring Data JPA가 자바의 기본적인 프록시 기술로 가짜 객체를 만들어 주입해줬기 때문이다.
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository,
    JpaSpecificationExecutor<Member> { // JpaSpecificationExecutor는 웬만하면 사용하지 않는 것이 좋다고... 지옥을 맛볼 수 있다.

  // 실무에서 NamedQuery는 거의 사용하지 않는다... => 대다수가 Repository에서 JPQL을 작성하는 걸 선호하는 것 같다.
//  @Query(name = "Member.findByUsername") // 주석 처리를 하면 NamedQuery를 먼저 찾아보고, 없으면 관례대로 실행한다.
  List<Member> findByUsername(@Param("username") String username);

  List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

  // 실수로 오타를 작성하면 컴파일할 때 문법 오류를 다 잡아줄 수 있다는 장점이 있다.
  // 동적 쿼리는 QueryDSL을 추천
  @Query("select m from Member m where m.username = :username and m.age = :age")
  List<Member> findUser(@Param("username") String username, @Param("age") int age);

  // 값 조회
  @Query("select m.username from Member m")
  List<String> findByUsernameList();

  // DTO 조회 => 생성자로 반환을 해줘야한다.
  @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
  List<MemberDto> findMemberDto();

  // 컬렉션 파라미터 바인딩
  @Query("select m from Member m where m.username in :names")
  List<Member> findByNames(@Param("names") List<String> names);

  List<Member> findListByUsername(String username);

  Member findMemberByUsername(String username);

  Optional<Member> findOptionalByUsername(String username);

  Page<Member> findByAge(int age, Pageable pageable);

  Slice<Member> findSliceByAge(int age, Pageable pageable);

  // count 쿼리를 분리해 불필요한 Join을 줄여 성능을 향상시킬 수 있다!!
  @Query(value = "select m from Member m left join m.team t",
      countQuery = "select count(m.username) from Member m")
  Page<Member> findPageQueryByAge(int age, Pageable pageable);

  @Modifying(clearAutomatically = true)
  @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
  int bulkAgePlus(@Param("age") int age);

  @Query("select m from Member m left join fetch m.team")
  List<Member> findMemberFetchJoin();

  @Override
  @EntityGraph(attributePaths = {"team"})
  List<Member> findAll();

  @EntityGraph(attributePaths = {"team"})
  @Query("select m from Member m")
  List<Member> findMemberEntityGraph();

  //  @EntityGraph(attributePaths = {"team"})
  @EntityGraph("Member.all")
  // Entity에서 직접 @NamedEntityGraph를 사용해 등록 가능!
  List<Member> findEntityGraphByUsername(@Param("username") String username);

  @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
  Member findReadOnlyByUsername(String username);

  // select for update / 비관적락
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  List<Member> findLockByUsername(String username);

  // Projections
  // 프로젝션 대상이 root 엔티티면 유용하지만.. root 엔티티를 넘어가는 순가 JPQL Select 최적화가 안된다.
  // 따라서 실무의 복잡한 쿼리를 해결하기에는 한계가 있다. 단순할 때만 사용하는 것을 추천하고 복잡하면 QueryDSL을 사용하자.
  List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

  List<UsernameOnlyDto> findProjectionsDtoByUsername(@Param("username") String username);

  <T> List<T> findProjectionGenericByUsername(@Param("username") String username,
      Class<T> type); // 동적 프로젝션

  // Native Query
  // 가급적 사용하지 않는 것이 좋다. 최후의 수단으로만 사용! 스프링 데이터 Projections을 사용하자.
  @Query(value = "select * from member where username = ?", nativeQuery = true)
  Member findByNativeQeury(String username);

  @Query(value = "select m.member_id as id, m.username, t.name as teamName "
      + "from member m left join team t ",
      countQuery = "select count(*) from member",
      nativeQuery = true)
  Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
