package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) // JDK 동적 프록시
@Import(ProxyDIAspect.class)
public class ProxyJdkProxyTest {

  @Autowired
  MemberService memberService;

  // JDK 동적 프록시에 구체 클래스 타입을 주입하면 안된다.
//  @Autowired
//  MemberServiceImpl memberServiceImpl;

  @Test
  void go() {
    log.info("memberService class={}", memberService.getClass());
//    log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
//    memberServiceImpl.hello("hello");
  }
}
