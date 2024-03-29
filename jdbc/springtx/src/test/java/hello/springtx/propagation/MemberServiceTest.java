package hello.springtx.propagation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

@Slf4j
@SpringBootTest
class MemberServiceTest {

  @Autowired MemberService memberService;
  @Autowired MemberRepository memberRepository;
  @Autowired LogRepository logRepository;

  /**
   * memberService    @Transactional: OFF
   * memberRepository @Transactional: ON
   * logRepository    @Transactional: ON
   */
  @Test
  void outerTxOff_success() {
    // given
    String username = "outerTxOff_success";

    // when
    memberService.joinV1(username);

    // then: 모든 데이터가 정상 저장
    assertTrue(memberRepository.find(username).isPresent());
    assertTrue(logRepository.find(username).isPresent());
  }

  /**
   * memberService    @Transactional: OFF
   * memberRepository @Transactional: ON
   * logRepository    @Transactional: ON Exception
   */
  @Test
  void outerTxOff_fail() {
    // given
    String username = "로그예외_outerTxOff_fail";

    // when
    Assertions.assertThatThrownBy(() -> memberService.joinV1(username))
        .isInstanceOf(RuntimeException.class);

    // then: 로그만 롤백되어 있어야한다.
    assertTrue(memberRepository.find(username).isPresent());
    assertTrue(logRepository.find(username).isEmpty());
  }

  /**
   * memberService    @Transactional: ON
   * memberRepository @Transactional: OFF
   * logRepository    @Transactional: OFF
   */
  @Test
  void singleTx() {
    // given
    String username = "outerTxOff_success";

    // when
    memberService.joinV1(username);

    // then: 모든 데이터가 정상 저장
    assertTrue(memberRepository.find(username).isPresent());
    assertTrue(logRepository.find(username).isPresent());
  }

  /**
   * memberService    @Transactional: ON
   * memberRepository @Transactional: ON
   * logRepository    @Transactional: ON
   */
  @Test
  void outerTxOn_success() {
    // given
    String username = "outerTxOn_success";

    // when
    memberService.joinV1(username);

    // then: 모든 데이터가 정상 저장
    assertTrue(memberRepository.find(username).isPresent());
    assertTrue(logRepository.find(username).isPresent());
  }

  /**
   * memberService    @Transactional: ON
   * memberRepository @Transactional: ON
   * logRepository    @Transactional: ON Exception
   */
  @Test
  void outerTxOn_fail() {
    // given
    String username = "로그예외_outerTxOn_fail";

    // when
    Assertions.assertThatThrownBy(() -> memberService.joinV1(username))
        .isInstanceOf(RuntimeException.class);

    // then: 모든 데이터가 롤백된다.
    assertTrue(memberRepository.find(username).isEmpty());
    assertTrue(logRepository.find(username).isEmpty());
  }

  /**
   * memberService    @Transactional: ON
   * memberRepository @Transactional: ON
   * logRepository    @Transactional: ON Exception
   */
  @Test
  void recoverException_fail() {
    // given
    String username = "로그예외_recoverException_fail";

    // when
    Assertions.assertThatThrownBy(() -> memberService.joinV2(username))
        .isInstanceOf(UnexpectedRollbackException.class);

    // then: member는 커밋되길 기대했지만! 모든 데이터가 롤백된다.
    assertTrue(memberRepository.find(username).isEmpty());
    assertTrue(logRepository.find(username).isEmpty());
  }

  /**
   * memberService    @Transactional: ON
   * memberRepository @Transactional: ON
   * logRepository    @Transactional: ON(REQUIRES_NEW) Exception
   */
  @Test
  void recoverException_success() {
    // given
    String username = "로그예외_recoverException_fail";

    // when
    memberService.joinV2(username);

    // then: member는 커밋되길 기대했지만! 모든 데이터가 롤백된다.
    assertTrue(memberRepository.find(username).isPresent());
    assertTrue(logRepository.find(username).isEmpty());
  }
}