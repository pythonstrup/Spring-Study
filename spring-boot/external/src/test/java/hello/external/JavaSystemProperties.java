package hello.external;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaSystemProperties {

  public static void main(String[] args) {

    // 코드 상에서 설정 가능
    System.setProperty("helloKey", "helloValue");

    Properties properties = System.getProperties();
    for (Object key: properties.keySet()) {
      log.info("prop {}={}", key, System.getProperty(String.valueOf(key)));
    }

    String url = System.getProperty("url");
    String username = System.getProperty("username");
    String password = System.getProperty("password");

    log.info("url={}", url);
    log.info("username={}", username);
    log.info("password={}", password);
  }
}
