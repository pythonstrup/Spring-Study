package com.test.thejavatest.study;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.test.thejavatest.domain.Member;
import com.test.thejavatest.domain.Study;
import com.test.thejavatest.member.MemberService;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class StudyServiceIntegrationTest {

  @Mock
  MemberService memberService;

  @Autowired
  StudyRepository studyRepository;

  private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer().withDatabaseName("studytest");

  @BeforeAll
  static void beforeAll() {
    postgreSQLContainer.start();
  }

  @AfterAll
  static void afterAll() {
    postgreSQLContainer.stop();
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
