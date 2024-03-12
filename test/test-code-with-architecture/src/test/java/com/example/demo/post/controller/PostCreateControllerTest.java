package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class PostCreateControllerTest {

  @Test
  void 사용자는_게시글을_작성할_수_있다() {
    // given
    TestContainer testContainer = TestContainer.builder()
        .clockHolder(new TestClockHolder(2222L))
        .build();
    testContainer.userRepository.save(User.builder()
        .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("Goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .build());

    // when
    ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(
        PostCreate.builder()
            .writerId(1)
            .content("foobar")
            .build());

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1L);
    assertThat(result.getBody().getWriter().getNickname()).isEqualTo("bell");
    assertThat(result.getBody().getContent()).isEqualTo("foobar");
    assertThat(result.getBody().getCreatedAt()).isEqualTo(2222L);
  }
}