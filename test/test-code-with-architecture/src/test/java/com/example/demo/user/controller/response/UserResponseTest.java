package com.example.demo.user.controller.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class UserResponseTest {

  @Test
  public void User로_응답을_생성할_수_있다() {
    // given
    User user = User.builder()
        .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .build();

    // when
    UserResponse userResponse = UserResponse.from(user);

    // then
    assertThat(userResponse.getId()).isEqualTo(1);
    assertThat(userResponse.getNickname()).isEqualTo("bell");
    assertThat(userResponse.getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
  }
}