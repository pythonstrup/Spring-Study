package com.example.demo.medium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/post-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD),
})
class PostServiceTest {
  @Autowired
  private PostService postService;

  @Test
  void getById_로_존재하는_게시물을_조회할_수_있다() {
    // given

    // when
    Post result = postService.getById(1);

    // then
    assertThat(result.getContent()).isEqualTo("hello world");
    assertThat(result.getWriter().getEmail()).isEqualTo("bell@mobidoc.us");
  }

  @Test
  void getById_은_존재하지_않는_게시물을_찾아올_수_없다() {
    // given
    String email = "pythonstrup@gmail.com";

    // when

    // then
    assertThatThrownBy(() -> {
      postService.getById(512509175);
    }).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void PostCreate_를_이용해_게시글을_생성할_수_있다() {
    // given
    PostCreate postCreate = PostCreate.builder()
        .writerId(1)
        .content("foobar")
        .build();

    // when
    Post result = postService.create(postCreate);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getContent()).isEqualTo("foobar");
    assertThat(result.getCreatedAt()).isGreaterThanOrEqualTo(0L);
  }

  @Test
  void PostUpdate_를_이용해_게시글을_수정할_수_있다() {
    // given
    PostUpdate postUpdate = PostUpdate.builder()
        .content("good bye")
        .build();

    // when
    Post result = postService.update(1, postUpdate);

    // then
    Post post = postService.getById(1);
    assertThat(post.getId()).isEqualTo(1);
    assertThat(post.getContent()).isEqualTo("good bye");
    assertThat(post.getModifiedAt()).isGreaterThanOrEqualTo(0);
  }
}