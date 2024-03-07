package com.example.demo.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

class PostTest {

  @Test
  public void PostCreate로_게시물을_만들_수_있다() {
    // given
    PostCreate postCreate = PostCreate.builder()
        .writerId(1)
        .content("hello world")
        .build();
    User writer = User.builder()
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();

    // when
    Post post = Post.of(writer, postCreate);
    
    // then
    assertThat(post.getContent()).isEqualTo("hello world");
    assertThat(post.getWriter().getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(post.getWriter().getNickname()).isEqualTo("bell");
    assertThat(post.getWriter().getAddress()).isEqualTo("goyang");
    assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }
}