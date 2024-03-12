package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();


  @Test
  void 사용자는_특정_유저의_정보를_개인정보가_소거된_상태로_전달_받을_수_있다() {
    // given
    TestContainer testContainer = TestContainer.builder().build();
    testContainer.userRepository.save(User.builder()
        .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("Goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(1000L)
        .build());

    // when
    ResponseEntity<UserResponse> result = testContainer.userController.getById(1L);

    // then
    Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1L);
    assertThat(result.getBody().getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(result.getBody().getNickname()).isEqualTo("bell");
    assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(result.getBody().getLastLoginAt()).isEqualTo(1000);
  }

  @Test
  void 사용자는_존재하지_않는_유저의_아이디로_api_를_호출할_경우_404_응답을_받는다() {
    // given
    TestContainer testContainer = TestContainer.builder().build();

    // when
    // then
    Assertions.assertThatThrownBy(() -> testContainer.userController.getById(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
    // given
    TestContainer testContainer = TestContainer.builder().build();
    testContainer.userRepository.save(User.builder()
        .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("Goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(1000L)
        .build());

    // when
    ResponseEntity<Void> result = testContainer.userController.verifyEmail(1L,
        "aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
    assertThat(testContainer.userRepository.getById(1L).getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러가_발생한다() {
    // given
    TestContainer testContainer = TestContainer.builder().build();
    testContainer.userRepository.save(User.builder()
        .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("Goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(1000L)
        .build());

    // when
    // then
    assertThatThrownBy(() -> testContainer.userController.verifyEmail(1L, "aaaaaaaa-aaaa-aaaa-aaaaaaaaaaab"))
        .isInstanceOf(CertificationCodeNotMatchedException.class);
  }

  @Test
  void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() {
    // given
    TestContainer testContainer = TestContainer.builder()
        .clockHolder(new TestClockHolder(10000L))
        .build();
    testContainer.userRepository.save(User.builder()
        .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("Goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(1000L)
        .build());

    // when
    ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo(
        "bell@mobidoc.us");

    // then
    Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1L);
    assertThat(result.getBody().getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(result.getBody().getNickname()).isEqualTo("bell");
    assertThat(result.getBody().getAddress()).isEqualTo("Goyang");
    assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(result.getBody().getLastLoginAt()).isEqualTo(10000L);
  }

  @Test
  void 사용자는_내_정보를_수정할_수_있다() {
    // given
    TestContainer testContainer = TestContainer.builder()
        .clockHolder(new TestClockHolder(10000L))
        .build();
    testContainer.userRepository.save(User.builder()
        .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("Goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(1000L)
        .build());

    // when
    ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo(
        "bell@mobidoc.us", UserUpdate.builder()
            .nickname("newbell")
            .address("Seoul")
            .build());

    // then
    Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1L);
    assertThat(result.getBody().getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(result.getBody().getNickname()).isEqualTo("newbell");
    assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
    assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(result.getBody().getLastLoginAt()).isEqualTo(1000);
  }

  // 이렇게 Stub하는 코드를 선호하지 않는다고 하심
  // 애초에 어떤 하위 클래스에 어떤 메소드가 호출되면 어떤 응답을 내려줘야 한다는 것 자체가 구현이 강요되는 것이기 때문이다.
  // 책임을 위임하는 것과 거리가 멀다
//  @Test
//  void 추천하지않는방식_사용자는_특정_유저의_정보를_개인정보가_소거된_상태로_전달_받을_수_있다() {
//    // given
//    UserController userController = UserController.builder()
//        .userReadService(new UserReadService() {
//          @Override
//          public User getByEmail(String email) {
//            return null;
//          }
//
//          @Override
//          public User getById(long id) {
//            return User.builder()
//                .id(id)
//                .email("bell@mobidoc.us")
//                .nickname("bell")
//                .address("Goyang")
//                .status(UserStatus.ACTIVE)
//                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
//                .lastLoginAt(1000L)
//                .build();
//          }
//        })
//        .build();
//
//    // when
//    ResponseEntity<UserResponse> result = userController.getUserById(1L);
//
//    // then
//    Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
//    assertThat(result.getBody()).isNotNull();
//    assertThat(result.getBody().getId()).isEqualTo(1L);
//    assertThat(result.getBody().getEmail()).isEqualTo("bell@mobidoc.us");
//    assertThat(result.getBody().getNickname()).isEqualTo("bell");
//    assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
//    assertThat(result.getBody().getLastLoginAt()).isEqualTo(1000L);
//  }
//
//  @Test
//  void 추천하지않는방식_사용자는_존재하지_않는_유저의_아이디로_api_를_호출할_경우_404_응답을_받는다() {
//    // given
//    UserController userController = UserController.builder()
//        .userReadService(new UserReadService() {
//          @Override
//          public User getByEmail(String email) {
//            return null;
//          }
//
//          @Override
//          public User getById(long id) {
//            throw new ResourceNotFoundException("Users", id);
//          }
//        })
//        .build();
//
//    // when
//    // then
//    Assertions.assertThatThrownBy(() -> userController.getUserById(214127893L))
//        .isInstanceOf(ResourceNotFoundException.class);
//  }
}