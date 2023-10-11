package hello.proxy.cglib.code;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

  private final Object target;

  public TimeMethodInterceptor(Object target) {
    this.target = target;
  }

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy)
      throws Throwable {
    log.info("TimeProxy 실행");
    long startTime = System.currentTimeMillis();

    // methodProxy를 사용하는 것이 좋은데, 아마 내부에 최적화하는 코드가 있는 것으로 추정된다고 함.
    // 기존에 사용했던 Object result = method.invoke(target, args); 보다 좋다고 함.
    Object result = methodProxy.invoke(target, args);

    long endTime = System.currentTimeMillis();
    long resultTime = endTime - startTime;
    log.info("TimeProxy 종료 resultTime={}", resultTime);
    return result;
  }
}
