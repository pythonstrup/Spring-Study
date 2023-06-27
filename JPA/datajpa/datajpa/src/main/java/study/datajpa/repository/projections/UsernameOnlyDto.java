package study.datajpa.repository.projections;

import lombok.Getter;

@Getter
public class UsernameOnlyDto {

  private final String username;

  // 생성자의 파라미터 이름과 매칭해 프로젝션이 가능하다.
  // 파라미터명이 일치하지 않으면 에러가 발생하니 주의하자
  public UsernameOnlyDto(String username) {
    this.username = username;
  }
}
