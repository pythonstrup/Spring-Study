package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberRepository memberRepository;

  @GetMapping("v1/members/{id}")
  public String findMember(@PathVariable Long id) {
    Member member = memberRepository.findById(id).get();
    return member.getUsername();
  }

  // 아래와 같이 PK 값으로 바로 Member를 조회해오는 방법이 있다.
  // 하지만 권장하지는 않는다.
  @GetMapping("v2/members/{id}")
  public String findMemberV2(@PathVariable("id") Member member) {
    return member.getUsername();
  }

  @PostConstruct
  public void init() {
    memberRepository.save(new Member("userA"));
  }
}
