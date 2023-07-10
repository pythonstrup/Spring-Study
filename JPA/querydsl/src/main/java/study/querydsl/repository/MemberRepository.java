package study.querydsl.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
  // select m from Member m where m.username = ?
  List<Member> findByUsername(String username);
}
