package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.repository.PostEntity;
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
    PostEntity result = postService.getById(1);

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
  void PostCreateDto_를_이용해_게시글을_생성할_수_있다() {
    // given
    PostCreateDto postCreateDto = PostCreateDto.builder()
        .writerId(1)
        .content("foobar")
        .build();

    // when
    PostEntity result = postService.create(postCreateDto);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getContent()).isEqualTo("foobar");
    assertThat(result.getCreatedAt()).isGreaterThanOrEqualTo(0L);
  }

  @Test
  void PostUpdateDto_를_이용해_게시글을_수정할_수_있다() {
    // given
    PostUpdateDto userUpdateDto = PostUpdateDto.builder()
        .content("good bye")
        .build();

    // when
    postService.update(1, userUpdateDto);

    // then
    PostEntity result = postService.getById(1);
    assertThat(result.getId()).isEqualTo(1);
    assertThat(result.getContent()).isEqualTo("good bye");
    assertThat(result.getModifiedAt()).isGreaterThanOrEqualTo(0);
  }
}