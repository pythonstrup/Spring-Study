package hello.datasource;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Getter
@ConfigurationProperties("my.datasource")
@Validated
public class MyDataSourcePropertiesV3 {

  @NotEmpty
  private String url;

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;

  private Etc etc;

//  @ConstructorBinding // boot 2.x는 필수  vs  boot 3.x는 생성자가 2개 이상일 때만
  public MyDataSourcePropertiesV3(
      String url,
      String username,
      String password,
      @DefaultValue Etc etc) {
    this.url = url;
    this.username = username;
    this.password = password;
    this.etc = etc;
  }

  @Getter
  public static class Etc {
    @Min(1)
    @Max(999)
    private int maxConnection;

    @DurationMin(seconds = 1)
    @DurationMax(seconds = 60)
    private Duration timeout;

    private List<String> options;
//    private List<String> options = new ArrayList<>();


    public Etc(
        int maxConnection,
        Duration timeout,
        @DefaultValue("DEFAULT") List<String> options) {
      this.maxConnection = maxConnection;
      this.timeout = timeout;
      this.options = options;
    }
  }
}
