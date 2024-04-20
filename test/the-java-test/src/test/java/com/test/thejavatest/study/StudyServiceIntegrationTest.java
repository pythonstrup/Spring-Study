package com.test.thejavatest.study;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.test.thejavatest.domain.Member;
import com.test.thejavatest.domain.Study;
import com.test.thejavatest.member.MemberService;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers // JUnit5 확장팩으로 테스트 클래스에 @Container를 사용한 필드를 찾아서 컨테이너 라이프사이클 관련 메소드를 실행해준다.
public class StudyServiceIntegrationTest {

  @Mock
  MemberService memberService;

  @Autowired
  StudyRepository studyRepository;

  @Container // 인스턴스 필드에 사용하면 모든 테스트마다 컨테이너를 재시작하고, 스태틱 필드에 사용하면 클래스 내부 모든 테스트에서 동일한 컨테이너를 재사용한다.
  private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer().withDatabaseName("studytest");

  @AfterEach
  void tearDown() {
    studyRepository.deleteAllInBatch();
  }

  @Test
  void createNewStudy() {
    // given
    StudyService studyService = new StudyService(memberService, studyRepository);
    assertNotNull(studyService);

    Member member = new Member();
    member.setId(1L);
    member.setEmail("bell@mobidoc.us");

    Study study = new Study(10, "테스트");

    given(memberService.findById(1L)).willReturn(Optional.of(member));

    // when
    studyService.createNewStudy(1L, study);

    // then
    assertEquals(1L, study.getOwnerId());
  }
}
