package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule.Feature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

  public static void main(String[] args) {
    SpringApplication.run(JpashopApplication.class, args);
  }

  // Hibernate5Module은 javax 패키지를 사용한다.
  // 스프링부트 3.x 부터는 jakarta를 사용하기 때문에 사용할 수 없다.
//  @Bean
//  Hibernate5Module hibernate5Module() {
//    Hibernate5Module hibernate5Module = new Hibernate5Module();
//    hibernate5Module.configure(Feature.FORCE_LAZY_LOADING, true);
//    return hibernate5Module;
//  }

  // 사실 잘 사용하는 방법은 아니다. (엔티티 직접 노출)
  // 이 모듈을 사용해서 엔티티를 반환하기보다는 DTO를 사용하는 것이 좋다.
  @Bean
  Hibernate5JakartaModule hibernate5JakartaModule() {
    Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
//    hibernate5JakartaModule.configure(Feature.FORCE_LAZY_LOADING, true); // Lazy Loading 강제
    return hibernate5JakartaModule;
  }
}
