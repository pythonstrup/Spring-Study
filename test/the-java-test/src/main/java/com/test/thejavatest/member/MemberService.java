package com.test.thejavatest.member;

import com.test.thejavatest.domain.Member;
import java.util.Optional;

public interface MemberService {

  Optional<Member> findById(Long memberId);
}
