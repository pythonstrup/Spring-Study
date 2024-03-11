package com.example.demo.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostServiceTest {

  private PostService postService;

  @BeforeEach
  void init() {
    FakeUserRepository userRepository = new FakeUserRepository();
    FakePostRepository postRepository = new FakePostRepository();
    User user1 = User.builder()
        .id(1L)
        .email("bell@mobidoc.us")
        .nickname("bell")
        .address("Goyang")
        .status(UserStatus.ACTIVE)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();
    User user2 = User.builder()
        .id(2L)
        .email("pythonstrup@gmail.us")
        .nickname("'pythonstrup'")
        .address("Goyang")
        .status(UserStatus.PENDING)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .build();
    userRepository.save(user1);
    userRepository.save(user2);
    postRepository.save(Post.builder()
        .id(1L)
        .writer(user1)
        .content("hello world")
        .modifiedAt(1678530673958L)
        .createdAt(1678530673958L)
        .build());

    this.postService = PostService.builder()
        .postRepository(postRepository)
        .userRepository(userRepository)
        .clockHolder(new TestClockHolder(1678530677777L))
        .build();
  }

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
    assertThat(result.getCreatedAt()).isEqualTo(1678530677777L);
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
    assertThat(post.getModifiedAt()).isEqualTo(1678530677777L);
  }
}