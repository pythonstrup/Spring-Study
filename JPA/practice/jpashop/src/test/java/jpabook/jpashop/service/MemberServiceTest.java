package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  MemberRepository memberRepository;

  @Test
//  @Rollback(value = false)
  public void joinTest() throws Exception {
    // given
    Member member = new Member();
    member.setUsername("park");
  
    // when
    Long savedId = memberService.join(member);

    // then
    assertEquals(member, memberRepository.find(savedId));
  }

//  @Test(expected = IllegalStateException.class) // JUNIT4 방식
  @Test
  public void duplicateMember() throws Exception {
    // given
    Member member1 = new Member();
    member1.setUsername("park");

    Member member2 = new Member();
    member2.setUsername("park");

    // when
    memberService.join(member1);
//    memberService.join(member2); // 예외가 발생해버린다. = 테스트가 실패해버린다.

    // 이 방법을 사용하면 코드가 굉장히 난잡해진다.
//    try {
//      memberService.join(member2);
//    } catch (IllegalStateException e) {
//      return;
//    }

    // then
    assertThrows(IllegalStateException.class, () -> {
      memberService.join(member2);
    });
//    fail("예외가 발생한다.");
  }
}