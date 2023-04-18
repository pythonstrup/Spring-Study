package com.biggy.servlet.web.frontcontroller.v2.controller;

import com.biggy.servlet.domain.member.Member;
import com.biggy.servlet.domain.member.MemberRepository;
import com.biggy.servlet.web.frontcontroller.MyView;
import com.biggy.servlet.web.frontcontroller.v2.ControllerV2;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberSaveControllerV2 implements ControllerV2 {

  private MemberRepository memberRepository = MemberRepository.getInstance();

  @Override
  public MyView process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("MemberSavaServlet.service");
    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));

    Member member = new Member(username, age);
    memberRepository.save(member);

    // Model에 데이터를 보관해야한다.
    request.setAttribute("member", member);

    return new MyView("/WEB-INF/views/save-result.jsp");
  }
}
