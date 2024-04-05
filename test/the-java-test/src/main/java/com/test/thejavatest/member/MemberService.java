package com.test.thejavatest.member;

import com.test.thejavatest.domain.Member;
import com.test.thejavatest.domain.Study;
import java.util.Optional;

public interface MemberService {

  Optional<Member> findById(Long memberId);

  void validate(Long memberId);

  void notify(Study study);
  void notify(Member member);
}
