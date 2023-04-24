package com.biggy.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Slf4j
@RestController
public class LogTestController {

  // lombok의 `@Slf4j` 로 대체 가능
  private final Logger log = LoggerFactory.getLogger(getClass());

  @RequestMapping("/log-test")
  public String logTest() {

    String name = "Spring";
    String name2 = "NestJS";

    // sout는 로그 레벨을 선택할 수 없고, 무조건 출력하기 때문에 사용하지 않는 것이 좋다.
    // 무조건 콘솔에만 남길 수 있다는 특징이 있다.
//    System.out.println("name = " + name);

    // trace와 debug는 실행했을 때 설정해주지 않으면 보이지 않는다.
    // 더 좋은 점! 설정을 해주면 파일로 남기거나 네트워크로 넘겨줄 수도 있다!
    // 성능도 내부 버퍼링, 멀티 쓰레드 등 덕분에 sout보다 좋다.
    log.trace("trace log={}, {}", name, name2);
    log.debug("info log={}", name);
    log.info("info log={}", name);
    log.warn("info log={}", name);
    log.error("info log={}", name);

    // 아래와 같이 사용하는 것은 권장하지 않는다.
    // 아래 코드는 trace를 출력하지도 않는데 연산을 해버린다.
    // "trace my log=" + "Spring" => "trace my log=Spring" 이라는 결과값을 가지고 있기 때문에! 쓰지 않는 것이 좋다.
    log.trace("trace my log=" + name);

    // 아래는 파라미터를 넘기는 것이기 때문에, trace를 출력하지 않으면 연산 자체를 하지 않는다.
    log.trace("trace log={}, {}", name, name2);

    return "ok";
  }
}
