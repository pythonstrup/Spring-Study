package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDataSourcePropertiesV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

// @ConfigurationPropertiesScan 덕분에 없어도 됨
//@EnableConfigurationProperties(MyDataSourcePropertiesV2.class)
@Slf4j
public class MyDataSourceConfigV2 {

  private final MyDataSourcePropertiesV2 properties;

  public MyDataSourceConfigV2(MyDataSourcePropertiesV2 properties) {
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
