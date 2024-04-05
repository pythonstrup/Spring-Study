package com.test.thejavatest.study;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.test.thejavatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

  @Mock
  MemberService memberService;

  @Mock
  StudyRepository studyRepository;

  @Test
  void createStudyService() {
    StudyService studyService = new StudyService(memberService, studyRepository);
    assertNotNull(studyService);
  }

  // 아래와 같이 파라미터로 넣어줘도 된다.
//  @Test
//  void createStudyService(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {
//    StudyService studyService = new StudyService(memberService, studyRepository);
//    assertNotNull(studyService);
//  }
}