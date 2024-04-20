package com.test.thejavatest.junit5;

import static org.assertj.core.api.Assertions.assertThat;

import com.test.thejavatest.domain.Study;
import com.test.thejavatest.annotation.SlowTest;
import com.test.thejavatest.extension.FindSlowTestExtensionCustom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.RegisterExtension;

//@ExtendWith(FindSlowTestExtension.class) // 선언적 방법
//@ExtendWith(FindSlowTestExtensionCustom.class) // 생성자에 인자를 전달해 인스턴스를 만들어야 하면 에러가 발생한다.
class StudyTestExtension {

  @RegisterExtension
  static FindSlowTestExtensionCustom findSlowTestExtensionCustom =
      new FindSlowTestExtensionCustom(1000L);

//  @Test
  @SlowTest
  @DisplayName("테스트")
  void create_study() throws InterruptedException {
    Thread.sleep(1005L);
    Study study = new Study(10);
    assertThat(study.getPeopleLimit()).isGreaterThan(0);
  }
}