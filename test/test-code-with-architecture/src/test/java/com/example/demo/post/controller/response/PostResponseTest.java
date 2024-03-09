package com.example.demo.post.controller.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class PostResponseTest {

  @Test
  public void Post로_응답을_생성할_수_있다() {
    // given
    User writer = User.builder()
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();
    Post post = Post.builder()
        .content("hello world")
        .writer(writer)
        .build();

    // when
    PostResponse postResponse = PostResponse.from(post);

    // then
    assertThat(postResponse.getContent()).isEqualTo("hello world");
    assertThat(postResponse.getWriter().getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(postResponse.getWriter().getNickname()).isEqualTo("bell");
    assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
  }
}