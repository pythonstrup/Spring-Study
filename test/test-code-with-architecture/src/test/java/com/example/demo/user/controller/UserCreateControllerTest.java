package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class UserCreateControllerTest {

  @Test
  void 사용자는_회원가입을_할_수_있고_회원가입된_사용자는_PENDING_상태이다() throws Exception {
    // given
    TestContainer testContainer = TestContainer.builder()
        .clockHolder(new TestClockHolder(10000L))
        .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
        .build();

    // when
    ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(
        UserCreate.builder()
            .email("bell@mobidoc.us")
            .nickname("bell")
            .address("Goyang")
            .build());

    // then
    Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1L);
    assertThat(result.getBody().getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(result.getBody().getNickname()).isEqualTo("bell");
    assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
    assertThat(result.getBody().getLastLoginAt()).isNull();
    assertThat(testContainer.userRepository.getById(1L).getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
  }
}