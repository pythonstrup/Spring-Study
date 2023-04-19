package com.biggy.servlet.web.frontcontroller.v5;

import com.biggy.servlet.web.frontcontroller.ModelView;
import com.biggy.servlet.web.frontcontroller.MyView;
import com.biggy.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.biggy.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.biggy.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import com.biggy.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.biggy.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.biggy.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import com.biggy.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import com.biggy.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerV5 extends HttpServlet {

  private final Map<String, Object> handlerMappingMap = new HashMap<>();
  private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

  public FrontControllerV5() {
    initHandlerMappingMap();
    initHandlerAdapters();
  }

  private void initHandlerMappingMap() {
    // V3
    handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
    handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
    handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

    // V4
    handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
    handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
    handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
  }

  private void initHandlerAdapters() {
    handlerAdapters.add(new ControllerV3HandlerAdapter());
    handlerAdapters.add(new ControllerV4HandlerAdapter());
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // ex) MemberFormControllerV3
    Object handler = getHandler(request);
    if (handler == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    MyHandlerAdapter adapter = getHandlerAdapter(handler);

    ModelView mv = adapter.handle(request, response, handler);

    String viewName = mv.getViewName();// 논리 이름 ex) new-form
    MyView view = viewResolver(viewName);

    view.render(mv.getModel(), request, response);
  }

  private Object getHandler(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    return handlerMappingMap.get(requestURI);
  }

  private MyHandlerAdapter getHandlerAdapter(Object handler) {
    for (MyHandlerAdapter adapter : handlerAdapters) {
      if (adapter.supports(handler)) {
        return adapter;
      }
    }
    throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
  }

  private MyView viewResolver(String viewName) {
    return new MyView("/WEB-INF/views/" + viewName + ".jsp");
  }
}
