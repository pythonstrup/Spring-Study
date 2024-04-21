package com.test.thejavatest.study.testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.test.thejavatest.domain.Member;
import com.test.thejavatest.domain.Study;
import com.test.thejavatest.member.MemberService;
import com.test.thejavatest.study.StudyRepository;
import com.test.thejavatest.study.StudyService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// TestContainers에서 지원하지 않는 DB Module로 컨테이너 구성할 때 사용하는 방법
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
@Slf4j
public class GenericContainerTest {

  @Mock
  MemberService memberService;

  @Autowired
  StudyRepository studyRepository;

  @Container
  private static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
      .withExposedPorts(5432)
//      .withEnv("POSTGRES_DB", "studytest")
      .withEnv("POSTGRES_PASSWORD", "studytest")
      .waitingFor(Wait.forListeningPort());

  @BeforeAll
  static void beforeAll() {
    Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
    postgreSQLContainer.followOutput(logConsumer);
  }

  @BeforeEach
  void setUp() {
    System.out.println("============");
    System.out.println("mappedPort = " + postgreSQLContainer.getMappedPort(5432));
    System.out.println("============");
  }

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

  @Test
  void createNewStudy2() {
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
