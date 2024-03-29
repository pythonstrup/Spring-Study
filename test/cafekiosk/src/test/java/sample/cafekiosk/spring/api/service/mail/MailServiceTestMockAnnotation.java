package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTestMockAnnotation {

  @Spy
  MailSendClient mailSendClient;

  @Mock
  MailSendHistoryRepository mailSendHistoryRepository;

  @InjectMocks
  MailService mailService;

  @Test
  @DisplayName("메일 전송 테스트")
  public void sendMail() {
    // given
    // spy는 그냥 when을 사용할 수 없다.
//    when(mailService.sendMail(anyString(), anyString(), anyString(), anyString()))
//        .thenReturn(true);

    // sendMail만 Stubbing 되고 나머지 메소드 a, b, c는 정상적으로 작동한다.
    doReturn(true)
        .when(mailSendClient)
        .sendEmail(anyString(), anyString(), anyString(), anyString());

    // when
    boolean result = mailService.sendMail("", "", "", "");

    // then
    assertThat(result).isTrue();
    verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
  }
}