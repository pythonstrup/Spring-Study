package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  private UserServiceImpl userService;

  @BeforeEach
  void init() {
    FakeMailSender fakeMailSender = new FakeMailSender();
    FakeUserRepository userRepository = new FakeUserRepository();
    this.userService = UserServiceImpl.builder()
        .certificationService(new CertificationServiceImpl(fakeMailSender))
        .userRepository(userRepository)
        .clockHolder(new TestClockHolder(1000L))
        .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
        .build();
    userRepository.save(User.builder()
            .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("Goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build());
    userRepository.save(User.builder()
            .id(2L)
        .email("pythonstrup@gmail.us")
        .nickname("'pythonstrup'")
        .address("Goyang")
        .status(UserStatus.PENDING)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .build());
  }

  @Test
  void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
    // given
    String email = "bell@mobidoc.us";

    // when
    User result = userService.getByEmail(email);

    // then
    assertThat(result.getNickname()).isEqualTo("bell");
  }

  @Test
  void getByEmail_은_PENDING_상태인_유저를_찾아올_수_없다() {
    // given
    String email = "pythonstrup@gmail.com";

    // when

    // then
    assertThatThrownBy(() -> {
      userService.getByEmail(email);
    }).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void getById_은_ACTIVE_상태인_유저를_찾아올_수_있다() {
    // given
    String email = "bell@mobidoc.us";

    // when
    User result = userService.getById(1);

    // then
    assertThat(result.getNickname()).isEqualTo("bell");
  }

  @Test
  void getById_은_PENDING_상태인_유저를_찾아올_수_없다() {
    // given
    String email = "pythonstrup@gmail.com";

    // when

    // then
    assertThatThrownBy(() -> {
      userService.getById(2);
    }).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void userCreate_를_이용해_유저를_생성할_수_있다() {
    // given
    UserCreate userCreate = UserCreate.builder()
        .email("bell2@gmail.com")
        .address("Goyang")
        .nickname("bell2")
        .build();

    // when
    User result = userService.create(userCreate);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }

  @Test
  void userUpdate_를_이용해_유저를_수정할_수_있다() {
    // given
    UserUpdate userUpdate = UserUpdate.builder()
        .address("Incheon")
        .nickname("bell3")
        .build();

    // when
    userService.update(1, userUpdate);

    // then
    User result = userService.getById(1);
    assertThat(result.getId()).isEqualTo(1);
    assertThat(result.getAddress()).isEqualTo("Incheon");
    assertThat(result.getNickname()).isEqualTo("bell3");
  }

  @Test
  void user_가_로그인하면_마지막_로그인_시간이_변경된다() {
    // given

    // when
    userService.login(1);

    // then
    User result = userService.getById(1);
    assertThat(result.getId()).isEqualTo(1);
    assertThat(result.getLastLoginAt()).isEqualTo(1000L);
  }

  @Test
  void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
    // given

    // when
    userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

    // then
    User result = userService.getById(2);
    assertThat(result.getId()).isEqualTo(2);
    assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void PENDING_상태의_사용자는_잘못된_인증_코드로_접근하면_에러가_발생한다() {
    // given

    // when

    // then
    assertThatThrownBy(() -> {
      userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac");
    }).isInstanceOf(CertificationCodeNotMatchedException.class);
  }
}