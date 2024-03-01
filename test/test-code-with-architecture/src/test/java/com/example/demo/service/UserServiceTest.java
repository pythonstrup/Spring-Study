package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
     @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD),
})
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Test
  void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
    // given
    String email = "bell@mobidoc.us";

    // when
    UserEntity result = userService.getByEmail(email);

    // then
    Assertions.assertThat(result.getNickname()).isEqualTo("bell");
  }

  @Test
  void getByEmail_은_PENDING_상태인_유저를_찾아올_수_없다() {
    // given
    String email = "pythonstrup@gmail.com";

    // when

    // then
    Assertions.assertThatThrownBy(() -> {
      userService.getByEmail(email);
    }).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void getById_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
    // given
    String email = "bell@mobidoc.us";

    // when
    UserEntity result = userService.getById(1);

    // then
    Assertions.assertThat(result.getNickname()).isEqualTo("bell");
  }

  @Test
  void getById_은_PENDING_상태인_유저를_찾아올_수_없다() {
    // given
    String email = "pythonstrup@gmail.com";

    // when

    // then
    Assertions.assertThatThrownBy(() -> {
      userService.getById(2);
    }).isInstanceOf(ResourceNotFoundException.class);
  }
}