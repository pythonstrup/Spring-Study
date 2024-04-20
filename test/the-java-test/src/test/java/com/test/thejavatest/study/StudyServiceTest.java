package com.test.thejavatest.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.thejavatest.domain.Member;
import com.test.thejavatest.domain.Study;
import com.test.thejavatest.member.MemberService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    Member member = new Member();
    member.setId(1L);
    member.setEmail("bell@mobidoc.us");

    when(memberService.findById(anyLong())).thenReturn(Optional.of(member));

    Study study = new Study(10, "java");
    studyService.createNewStudy(1L, study);
  }

  @Test
  void exceptionStubbing() {
    StudyService studyService = new StudyService(memberService, studyRepository);

    Member member = new Member();
    member.setId(1L);
    member.setEmail("bell@mobidoc.us");

    when(memberService.findById(anyLong())).thenThrow(new RuntimeException());

    Study study = new Study(10, "java");
    assertThatThrownBy(() -> studyService.createNewStudy(1L, study))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void validate() {
    Mockito.doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
    assertThatThrownBy(() -> memberService.validate(1L))
        .isInstanceOf(IllegalArgumentException.class);
    memberService.validate(2L);
  }

  @Test
  void createStudy() {
    StudyService studyService = new StudyService(memberService, studyRepository);

    Member member = new Member();
    member.setId(1L);
    member.setEmail("bell@mobidoc.us");

    when(memberService.findById(anyLong()))
        .thenReturn(Optional.of(member))
        .thenThrow(new RuntimeException())
        .thenReturn(Optional.empty());

    Optional<Member> member1 = memberService.findById(1L);
    assertThat(member1).isPresent();
    assertThat(member1.get().getId()).isEqualTo(1L);

    assertThatThrownBy(() -> memberService.findById(1L))
        .isInstanceOf(RuntimeException.class);

    Optional<Member> member3 = memberService.findById(1L);
    assertThat(member3).isEmpty();
  }

  @Test
  @DisplayName("")
  void practice() {
    // given
    StudyService studyService = new StudyService(memberService, studyRepository);
    Study study = new Study(10, "test");

    Member member = new Member();
    member.setId(1L);
    member.setEmail("bell@mobidoc.us");
    when(memberService.findById(1L)).thenReturn(Optional.of(member));
    when(studyRepository.save(study)).thenReturn(study);

    // when
    Study newStudy = studyService.createNewStudy(1L, study);

    // then
    assertAll(
        () -> assertThat(study).isNotNull(),
        () -> assertThat(study.getName()).isEqualTo("test"),
        () -> assertThat(study.getPeopleLimit()).isEqualTo(10),
        () -> assertThat(study.getStatus()).isEqualTo(StudyStatus.DRAFT)
    );
  }

  @Test
  @DisplayName("")
  void notifyStudy() {
    // given
    StudyService studyService = new StudyService(memberService, studyRepository);
    Study study = new Study(10, "test");

    Member member = new Member();
    member.setId(1L);
    member.setEmail("bell@mobidoc.us");
    when(memberService.findById(1L)).thenReturn(Optional.of(member));
    when(studyRepository.save(study)).thenReturn(study);

    // when
    Study newStudy = studyService.createNewStudy(1L, study);

    // then
    assertAll(
        () -> assertThat(study).isNotNull(),
        () -> assertThat(study.getName()).isEqualTo("test"),
        () -> assertThat(study.getPeopleLimit()).isEqualTo(10),
        () -> assertThat(study.getStatus()).isEqualTo(StudyStatus.DRAFT)
    );

    verify(memberService, times(1)).notify(study);
//    verifyNoMoreInteractions(memberService);
    verify(memberService, times(1)).notify(member);
    verify(memberService, never()).validate(anyLong());

    InOrder inOrder = inOrder(memberService);
    inOrder.verify(memberService).notify(study);
    inOrder.verify(memberService).notify(member);
  }

  @Test
  @DisplayName("")
  void BDDMockito() {
    // given
    StudyService studyService = new StudyService(memberService, studyRepository);
    Study study = new Study(10, "test");

    Member member = new Member();
    member.setId(1L);
    member.setEmail("bell@mobidoc.us");
    BDDMockito.given(memberService.findById(1L)).willReturn(Optional.of(member));
    BDDMockito.given(studyRepository.save(study)).willReturn(study);

    // when
    Study newStudy = studyService.createNewStudy(1L, study);

    // then
    assertAll(
        () -> assertThat(study).isNotNull(),
        () -> assertThat(study.getName()).isEqualTo("test"),
        () -> assertThat(study.getPeopleLimit()).isEqualTo(10),
        () -> assertThat(study.getStatus()).isEqualTo(StudyStatus.DRAFT)
    );

    BDDMockito.then(memberService).should(times(1)).notify(study);
    BDDMockito.then(memberService).should(times(1)).notify(member);
    BDDMockito.then(memberService).shouldHaveNoInteractions();
  }

  // 아래와 같이 파라미터로 넣어줘도 된다.
//  @Test
//  void createStudyService(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {
//    StudyService studyService = new StudyService(memberService, studyRepository);
//    assertNotNull(studyService);
//  }
}