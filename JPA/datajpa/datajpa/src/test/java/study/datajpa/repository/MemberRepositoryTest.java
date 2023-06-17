package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

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
}