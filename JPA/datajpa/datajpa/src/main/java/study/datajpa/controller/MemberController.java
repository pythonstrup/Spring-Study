package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberRepository memberRepository;

  @GetMapping("/v1/members/{id}")
  public String findMember(@PathVariable Long id) {
    Member member = memberRepository.findById(id).get();
    return member.getUsername();
  }

  // 아래와 같이 PK 값으로 바로 Member를 조회해오는 방법이 있다.
  // 하지만 권장하지는 않는다.
  @GetMapping("/v2/members/{id}")
  public String findMemberV2(@PathVariable("id") Member member) {
    return member.getUsername();
  }

  @GetMapping("/v1/members")
  public Page<Member> list(@PageableDefault(size = 5) Pageable pageable) {
    Page<Member> page = memberRepository.findAll(pageable);
    return page;
  }

  // DTO로 변환해서 반환하는 방법
  @GetMapping("/v2/members/dto")
  public Page<MemberDto> listDto(@PageableDefault(size = 5) Pageable pageable) {
    Page<Member> page = memberRepository.findAll(pageable);
    return page.map(MemberDto::new);
  }

  // Pageable을 직접 선언해서 사용하는 방법
  @GetMapping("/v3/members/dto")
  public Page<MemberDto> listPageable(@PageableDefault(size = 5) Pageable pageable) {
    PageRequest request = PageRequest.of(0, 2);

    Page<Member> page = memberRepository.findAll(request);
    return page.map(MemberDto::new);
  }

  @PostConstruct
  public void init() {
    for (int i = 0; i < 100; i++) {
      memberRepository.save(new Member("user" + i, i));
    }
  }
}
