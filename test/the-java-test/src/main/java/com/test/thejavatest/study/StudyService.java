package com.test.thejavatest.study;

import com.test.thejavatest.domain.Member;
import com.test.thejavatest.domain.Study;
import com.test.thejavatest.member.MemberService;
import java.util.Optional;
import org.springframework.stereotype.Service;

public class StudyService {

  private final MemberService memberService;
  private final StudyRepository studyRepository;

  public StudyService(MemberService memberService, StudyRepository studyRepository) {
    assert memberService != null;
    assert studyRepository != null;
    this.memberService = memberService;
    this.studyRepository = studyRepository;
  }

  public Study createNewStudy(Long memberId, Study study) {
    Optional<Member> member = memberService.findById(memberId);
    study.setOwner(member.orElseThrow(() -> new IllegalArgumentException("Member doesn't exist for id: " + memberId)));
    study.setOwner(member.get());
    Study newStudy = studyRepository.save(study);
    memberService.notify(newStudy);
    memberService.notify(member.get());
    return newStudy;
  }
}
