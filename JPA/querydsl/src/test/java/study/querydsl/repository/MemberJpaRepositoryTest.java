package study.querydsl.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

  @PersistenceContext
  EntityManager em;

  @Autowired
  MemberJpaRepository memberJpaRepository;

  @Test
  void basicTest() {
    Member member = new Member("member1", 10);
    memberJpaRepository.save(member);

    Member findMember = memberJpaRepository.findById(member.getId()).get();
    assertThat(findMember).isEqualTo(member);

    List<Member> result1 = memberJpaRepository.findAll();
    assertThat(result1).containsExactly(member);

    List<Member> result2 = memberJpaRepository.findByUsername("member1");
    assertThat(result2).containsExactly(member);

    // queryDsl
    List<Member> queryDslResult1 = memberJpaRepository.findAllQueryDsl();
    assertThat(queryDslResult1).containsExactly(member);
    List<Member> queryDslResult2 = memberJpaRepository.findByUsernameQueryDsl("member1");
    assertThat(queryDslResult2).containsExactly(member);
  }


}