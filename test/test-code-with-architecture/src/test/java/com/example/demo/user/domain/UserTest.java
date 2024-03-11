package com.example.demo.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void UserCreate_객체로_생성할_수_있다() {
    // given
    UserCreate userCreate = UserCreate.builder()
        .email("bell@mobidoc.us")
        .address("Goyang")
        .nickname("bell")
        .build();

    // when
    User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));

    // then
    assertThat(user.getId()).isNull();
    assertThat(user.getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(user.getNickname()).isEqualTo("bell");
    assertThat(user.getAddress()).isEqualTo("Goyang");
    assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }

  @Test
  void UserUpdate_객체로_수정할_수_있다() {
    // given
    User user = User.builder()
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .build();
    UserUpdate userUpdate = UserUpdate.builder()
        .nickname("newbell")
        .address("Seoul")
        .build();

    // when
    User result = user.update(userUpdate);

    // then
    assertThat(result.getId()).isNull();
    assertThat(result.getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(result.getNickname()).isEqualTo("newbell");
    assertThat(result.getAddress()).isEqualTo("Seoul");
    assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    assertThat(result.getLastLoginAt()).isEqualTo(100L);
  }

  @Test
  void 로그인을_할_수_있고_로그인시_마직막_로그인_시간이_변경된다() {
    // given
    User user = User.builder()
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .build();

    // when
    User result = user.login(new TestClockHolder(1000L));

    // then
    assertThat(result.getId()).isNull();
    assertThat(result.getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(result.getNickname()).isEqualTo("bell");
    assertThat(result.getAddress()).isEqualTo("goyang");
    assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    assertThat(result.getLastLoginAt()).isEqualTo(1000L);
  }

  @Test
  void 유효한_인증_코드로_계정을_활성화_할_수_있다() {
    // given
    User user = User.builder()
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("goyang")
        .status(UserStatus.PENDING)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .build();

    // when
    User result = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // then
    assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void 유효하지_않은_인증_코드로_계정_활성화를_시도하면_에러가_발생한다() {
    // given
    User user = User.builder()
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("goyang")
        .status(UserStatus.PENDING)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .build();

    // when
    // then
    assertThatThrownBy(() -> user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
        .isInstanceOf(CertificationCodeNotMatchedException.class);
  }
}