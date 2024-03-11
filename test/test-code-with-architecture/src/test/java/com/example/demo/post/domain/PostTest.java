package com.example.demo.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.mock.TestClockHolder;
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
    Post post = Post.of(writer, postCreate, new TestClockHolder(1000L));
    
    // then
    assertThat(post.getContent()).isEqualTo("hello world");
    assertThat(post.getWriter().getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(post.getWriter().getNickname()).isEqualTo("bell");
    assertThat(post.getWriter().getAddress()).isEqualTo("goyang");
    assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    assertThat(post.getCreatedAt()).isEqualTo(1000L);
  }

  @Test
  public void PostUpdate로_게시물을_수정할_수_있다() {
    // given
    PostUpdate postUpdate = PostUpdate.builder()
        .content("foobar")
        .build();
    User writer = User.builder()
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();
    Post post = Post.builder()
        .id(1L)
        .writer(writer)
        .content("hello world")
        .modifiedAt(1678530673958L)
        .createdAt(1678530673958L)
        .build();

    // when
    Post result = post.update(postUpdate, new TestClockHolder(1000L));

    // then
    assertThat(result.getContent()).isEqualTo("foobar");
    assertThat(result.getWriter().getEmail()).isEqualTo("bell@mobidoc.us");
    assertThat(result.getWriter().getNickname()).isEqualTo("bell");
    assertThat(result.getWriter().getAddress()).isEqualTo("goyang");
    assertThat(result.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(result.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    assertThat(result.getCreatedAt()).isEqualTo(1678530673958L);
    assertThat(result.getModifiedAt()).isEqualTo(1000L);
  }
}