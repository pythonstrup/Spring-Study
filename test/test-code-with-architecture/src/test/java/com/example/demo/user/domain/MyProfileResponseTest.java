package com.example.demo.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.controller.response.MyProfileResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MyProfileResponseTest {

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
    MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

    // then
    assertThat(myProfileResponse.getId()).isEqualTo(1);
    assertThat(myProfileResponse.getNickname()).isEqualTo("bell");
    assertThat(myProfileResponse.getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(myProfileResponse.getAddress()).isEqualTo("goyang");
    assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
  }
}