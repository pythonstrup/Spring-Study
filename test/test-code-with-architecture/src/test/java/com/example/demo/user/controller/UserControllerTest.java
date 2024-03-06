package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD),
})
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void 사용자는_특정_유저의_정보를_개인정보가_소거된_상태로_전달_받을_수_있다() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/api/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.email").value("bell@mobidoc.us"))
        .andExpect(jsonPath("$.nickname").value("bell"))
        .andExpect(jsonPath("$.address").doesNotExist())
        .andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  void 사용자는_존재하지_않는_유저의_아이디로_api_를_호출할_경우_404_응답을_받는다() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/api/users/12151263772"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Users에서 ID 12151263772를 찾을 수 없습니다."));
  }

  @Test
  void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/api/users/2/verify")
            .queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
        .andExpect(status().isFound());
    UserEntity userEntity = userJpaRepository.findById(2L).get();
    assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러가_발생한다() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/api/users/2/verify")
            .queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaabbb"))
        .andExpect(status().isForbidden());;
  }

  @Test
  void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/api/users/me")
            .header("EMAIL", "bell@mobidoc.us"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.email").value("bell@mobidoc.us"))
        .andExpect(jsonPath("$.nickname").value("bell"))
        .andExpect(jsonPath("$.address").value("Goyang"))
        .andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  void 사용자는_내_정보를_수정할_수_있다() throws Exception {
    // given
    UserUpdate userUpdate = UserUpdate.builder()
        .nickname("newbell")
        .address("Seoul")
            .build();

    // when
    // then
    mockMvc.perform(put("/api/users/me")
            .header("EMAIL", "bell@mobidoc.us")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userUpdate)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.email").value("bell@mobidoc.us"))
        .andExpect(jsonPath("$.nickname").value("newbell"))
        .andExpect(jsonPath("$.address").value("Seoul"))
        .andExpect(jsonPath("$.status").value("ACTIVE"));
  }
}