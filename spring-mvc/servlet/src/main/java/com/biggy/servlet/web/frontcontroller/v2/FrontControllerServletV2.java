package com.biggy.servlet.web.frontcontroller.v2;

import com.biggy.servlet.web.frontcontroller.MyView;
import com.biggy.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import com.biggy.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import com.biggy.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// /front-controller/v2/members/new-form을 호출
// Controller가 MyView를 반환
// 반환받은 MyView를 사용해서 front controller에서 MyView의 render를 실행해 dispatcher가 view를 보여주도록 만듦
@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {

  private Map<String, ControllerV2> controllerMap = new HashMap<>();

  public FrontControllerServletV2() {
    controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
    controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
    controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("FrontControllerServletV1.service");

    String requestURI = request.getRequestURI();

    ControllerV2 controller = controllerMap.get(requestURI);
    if (controller == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    MyView view = controller.process(request, response);
    view.render(request, response);
  }
}
