package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

// 구현체를 따로 만든 적이 없는데, 해당 객체를 어떻게 사용할 수 있는 걸까?
// Spring Data JPA가 자바의 기본적인 프록시 기술로 가짜 객체를 만들어 주입해줬기 때문이다.
public interface MemberRepository extends JpaRepository<Member, Long> {

}
