package com.example.demo.user.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.domain.UserStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

//@ExtendWith(SpringExtension.class) // @DataJpaTest에 이미 내장되어 있다.
@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties") // 테스트 프로퍼티를 이런 식으로 지정할 수 있다.
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
    // given

    // when
    Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

    // then
    assertThat(result).isPresent();
  }

  @Test
  void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_준다() {
    // given

    // when
    Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

    // then
    assertThat(result).isEmpty();
  }

  @Test
  void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
    // given

    // when
    Optional<UserEntity> result = userRepository.findByEmailAndStatus("bell@mobidoc.us", UserStatus.ACTIVE);

    // then
    assertThat(result).isPresent();
  }

  @Test
  void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_준다() {
    // given

    // when
    Optional<UserEntity> result = userRepository.findByEmailAndStatus("bell@mobidoc.us", UserStatus.PENDING);

    // then
    assertThat(result).isEmpty();
  }
}