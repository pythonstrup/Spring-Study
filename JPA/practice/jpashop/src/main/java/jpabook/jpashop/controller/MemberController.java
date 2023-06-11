package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import java.util.List;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.MemberFormDto;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/members/new")
  public String createForm(Model model) {
    model.addAttribute("memberForm", new MemberFormDto());
    return "members/createMemberForm";
  }

  @PostMapping("/members/new")
  public String crate(@Valid MemberFormDto form, BindingResult result) {

    if (result.hasErrors()) {
      return "members/createMemberForm";
    }

    Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

    Member member = new Member();
    member.setUsername(form.getName());
    member.setAddress(address);

    memberService.join(member);
    return "redirect:/";
  }

  @GetMapping("/members")
  public String list(Model model) {
    // 화면을 바로 그려주더라도! 웬만하면 Entity가 아니라 Dto로 뿌려주는 것이 좋다.
    // API로는 Entity를 절대로 반환해서는 안된다. (API 스펙이 변할 수 있고, 개인정보가 유출될 수도 있다.)
    List<Member> members = memberService.findMembers();
    model.addAttribute("members", members);
    return "members/memberList";
  }
}
