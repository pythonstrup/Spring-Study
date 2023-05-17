package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDataSourcePropertiesV2;
import hello.datasource.MyDataSourcePropertiesV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

// @ConfigurationPropertiesScan이 있으면 없어도 된다.
@EnableConfigurationProperties(MyDataSourcePropertiesV3.class)
@Slf4j
public class MyDataSourceConfigV3 {

  private final MyDataSourcePropertiesV3 properties;

  public MyDataSourceConfigV3(MyDataSourcePropertiesV3 properties) {
    this.properties = properties;
  }

  @Bean
  public MyDataSource myDataSource() {
    return new MyDataSource(
        properties.getUrl(),
        properties.getUsername(),
        properties.getPassword(),
        properties.getEtc().getMaxConnection(),
        properties.getEtc().getTimeout(),
        properties.getEtc().getOptions());
  }
}
