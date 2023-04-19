package com.biggy.servlet.web.frontcontroller.v3;

import com.biggy.servlet.web.frontcontroller.ModelView;
import com.biggy.servlet.web.frontcontroller.MyView;
import com.biggy.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.biggy.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.biggy.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
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
@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

  private Map<String, ControllerV3> controllerMap = new HashMap<>();

  public FrontControllerServletV3() {
    controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
    controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
    controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String requestURI = request.getRequestURI();

    ControllerV3 controller = controllerMap.get(requestURI);
    if (controller == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // paramMap
    Map<String, String> paramMap = createParamMap(request);
    ModelView mv = controller.process(paramMap);

    String viewName = mv.getViewName();// 논리 이름 ex) new-form
    MyView view = viewResolver(viewName);

    view.render(mv.getModel(), request, response);
  }

  private MyView viewResolver(String viewName) {
    return new MyView("/WEB-INF/views/" + viewName + ".jsp");
  }

  private Map<String, String> createParamMap(HttpServletRequest request) {
    Map<String, String> paramMap = new HashMap<>();
    request.getParameterNames().asIterator()
        .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
    return paramMap;
  }
}
