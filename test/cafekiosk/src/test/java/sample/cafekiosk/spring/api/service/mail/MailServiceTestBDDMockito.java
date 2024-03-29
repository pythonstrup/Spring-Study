package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTestBDDMockito {

  @Mock
  MailSendClient mailSendClient;

  @Mock
  MailSendHistoryRepository mailSendHistoryRepository;

  @InjectMocks
  MailService mailService;

  @Test
  @DisplayName("메일 전송 테스트")
  public void sendMail() {
    // given
//    when(mailService.sendMail(anyString(), anyString(), anyString(), anyString()))
//        .thenReturn(true);

    // BDD에 맞게 Mockito를 사용하기 위해 쓰인다.
    // 사실 Mockito 객체와 기능은 동일하나 이름만 다르다.
    BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
        .willReturn(true);

    // when
    boolean result = mailService.sendMail("", "", "", "");

    // then
    assertThat(result).isTrue();
    verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
  }
}