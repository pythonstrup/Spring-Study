package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

class PostControllerTest {

  @Test
  void 사용자는_게시글을_단건_조회할_수_있다() throws Exception {
    // given
    TestContainer testContainer = TestContainer.builder()
        .build();
    User user = User.builder()
        .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("Goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .build();
    testContainer.userRepository.save(user);
    testContainer.postRepository.save(Post.builder()
        .id(1L)
        .writer(user)
        .content("hello world")
        .createdAt(1678530673958L)
        .modifiedAt(1678530673958L)
        .build());

    // when
    ResponseEntity<PostResponse> result = testContainer.postController.getById(1L);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1L);
    assertThat(result.getBody().getWriter().getNickname()).isEqualTo("bell");
    assertThat(result.getBody().getContent()).isEqualTo("hello world");
    assertThat(result.getBody().getCreatedAt()).isEqualTo(1678530673958L);
    assertThat(result.getBody().getModifiedAt()).isEqualTo(1678530673958L);
  }

  @Test
  void 사용자가_존재하지_않는_게시글을_조회할_경우_에러가_발생한다() throws Exception {
    // given
    TestContainer testContainer = TestContainer.builder()
        .build();

    // when
    // then
    assertThatThrownBy(() -> testContainer.postController.getById(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void 사용자는_게시글을_수정할_수_있다() throws Exception {
    // given
    TestContainer testContainer = TestContainer.builder()
        .clockHolder(() -> 2222L)
        .build();
    User user = User.builder()
        .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("Goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .build();
    testContainer.userRepository.save(user);
    testContainer.postRepository.save(Post.builder()
        .id(1L)
        .writer(user)
        .content("hello world")
        .createdAt(1678530673958L)
        .modifiedAt(1678530673958L)
        .build());

    // when
    ResponseEntity<PostResponse> result = testContainer.postController.update(1L, PostUpdate.builder()
        .content("new content")
        .build());

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1L);
    assertThat(result.getBody().getWriter().getNickname()).isEqualTo("bell");
    assertThat(result.getBody().getContent()).isEqualTo("new content");
    assertThat(result.getBody().getCreatedAt()).isEqualTo(1678530673958L);
    assertThat(result.getBody().getModifiedAt()).isEqualTo(2222L);
  }
}