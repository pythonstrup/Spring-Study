package study.datajpa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

// 구현체를 따로 만든 적이 없는데, 해당 객체를 어떻게 사용할 수 있는 걸까?
// Spring Data JPA가 자바의 기본적인 프록시 기술로 가짜 객체를 만들어 주입해줬기 때문이다.
public interface MemberRepository extends JpaRepository<Member, Long> {

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
}
