package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration.Dynamic;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitV3SpringMvc implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    System.out.println("AppInitV3SpringMvc.onStartup");

    // spring container
    AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
    appContext.register(HelloConfig.class);

    // Spring MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
    DispatcherServlet dispatcher = new DispatcherServlet(appContext);

    // Dispatcher Servlet을 Servlet Container에 등록
    Dynamic servlet = servletContext.addServlet("dispatcherV3", dispatcher);

    // 모든 request가 Dispatcher Servlet을 통하도록 설정
    servlet.addMapping("/");
  }
}
