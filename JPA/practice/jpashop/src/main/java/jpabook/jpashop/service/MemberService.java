package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// JPA가 조회를 최적화해준다.
// 영속성 컨텍스트를 flush하지 않고, 더티체킹을 하지 않는다는 이점
// DB에 따라 드라이버가 읽기 전용으로 리소스를 최대한 적게 쓰면서 조회하는 것도 있다.
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  // readonly를 사용하지 않는 메소드에서만 아래처럼 적용해줄 수 있다.
  @Transactional
  public Long join(Member member) {
    validateDulicateMember(member);
    memberRepository.save(member);
    return member.getId();
  }

  private void validateDulicateMember(Member member) {
    List<Member> findMembers = memberRepository.findByName(member.getUsername());
    if (!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  public Member findOne(Long memberId) {
    return memberRepository.find(memberId);
  }

}
