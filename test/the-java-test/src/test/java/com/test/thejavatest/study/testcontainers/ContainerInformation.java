package com.test.thejavatest.study.testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.test.thejavatest.domain.Member;
import com.test.thejavatest.domain.Study;
import com.test.thejavatest.member.MemberService;
import com.test.thejavatest.study.StudyRepository;
import com.test.thejavatest.study.StudyService;
import com.test.thejavatest.study.testcontainers.ContainerInformation.ContainerContextPropertyInitializer;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
@Slf4j
@ContextConfiguration(initializers = ContainerContextPropertyInitializer.class)
public class ContainerInformation {

  @Mock
  MemberService memberService;

  @Autowired
  StudyRepository studyRepository;

  @Autowired
  Environment environment;

  @Value("${container.port}") int mappedPort;

  @Container
  private static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
      .withExposedPorts(5432)
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
    System.out.println("mappedPort by container = " + postgreSQLContainer.getMappedPort(5432));
    System.out.println("mappedPort by spring env = " + environment.getProperty("container.port"));
    System.out.println("mappedPort by spring env value = " + mappedPort);
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

  static class ContainerContextPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertyValues.of("container.port="+postgreSQLContainer.getMappedPort(5432))
          .applyTo(applicationContext.getEnvironment());
    }
  }
}
