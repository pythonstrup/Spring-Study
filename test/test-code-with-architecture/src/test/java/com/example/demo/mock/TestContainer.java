package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.service.CertificationServiceImpl;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

  public final MailSender mailSender;
  public final PostRepository postRepository;
  public final PostService postService;
  public final UserRepository userRepository;
  public final UserService userService;
  public final CertificationServiceImpl certificationService;
  public final UserController userController;
  public final UserCreateController userCreateController;
  public final PostController postController;
  public final PostCreateController postCreateController;

  @Builder
  public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
    this.mailSender = new FakeMailSender();
    this.userRepository = new FakeUserRepository();
    this.postRepository = new FakePostRepository();
    this.postService = PostServiceImpl.builder()
        .postRepository(this.postRepository)
        .userRepository(this.userRepository)
        .clockHolder(clockHolder)
        .build();

    this.certificationService = new CertificationServiceImpl(this.mailSender);
    this.userService = UserServiceImpl.builder()
        .certificationService(this.certificationService)
        .userRepository(this.userRepository)
        .clockHolder(clockHolder)
        .uuidHolder(uuidHolder)
        .build();

    this.postController = PostController.builder()
        .postService(this.postService)
        .build();
    this.postCreateController = PostCreateController.builder()
        .postService(this.postService)
        .build();
    this.userController = UserController.builder()
        .userService(this.userService)
        .build();
    this.userCreateController = UserCreateController.builder()
        .userService(this.userService)
        .build();
  }
}
