package jpabook.jpashop.repository;

import static org.junit.jupiter.api.Assertions.*;

import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Test
  @Transactional // 스프링에서는 Test에서 @Transactional을 걸면 해당 코드를 마친 후, 롤백을 해버린다는 사실!
//  @Rollback(value = false) // 해당 어노테이션을 통해 롤백이 되지 않도록 설정할 수도 있다.
  public void testMember() throws Exception {
    // given
    Member member = new Member();
    member.setUsername("memberA");

    // when
    Long savedId = memberRepository.save(member);
    Member findMember = memberRepository.find(savedId);

    // then
    Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
    Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    Assertions.assertThat(findMember).isEqualTo(member);
    // 영속성 컨텍스트의 1차캐시에서 해당 객체를 findMember로 가져오기 때문에 아예 같은 객체로 취급된다.
  }

}