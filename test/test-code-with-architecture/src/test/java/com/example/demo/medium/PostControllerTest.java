package com.example.demo.medium;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.post.domain.PostUpdate;
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
    @Sql(value = "/sql/post-controller-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD),
})
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void 사용자는_게시글을_단건_조회할_수_있다() throws Exception {
    // given

    // when

    // then
    mockMvc.perform(get("/api/posts/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.content").value("hello world"))
        .andExpect(jsonPath("$.writer.id").value(1))
        .andExpect(jsonPath("$.writer.email").value("bell@mobidoc.us"))
        .andExpect(jsonPath("$.writer.nickname").value("bell"));
  }

  @Test
  void 사용자가_존재하지_않는_게시글을_조회할_경우_에러가_발생한다() throws Exception {
    // given

    // when

    // then
    mockMvc.perform(get("/api/posts/53527859823759"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Posts에서 ID 53527859823759를 찾을 수 없습니다."));
  }

  @Test
  void 사용자는_게시글을_수정할_수_있다() throws Exception {
    // given
    PostUpdate postUpdate = PostUpdate.builder()
        .content("new content")
        .build();

    // when

    // then
    mockMvc.perform(put("/api/posts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postUpdate)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.content").value("new content"))
        .andExpect(jsonPath("$.writer.id").value(1))
        .andExpect(jsonPath("$.writer.email").value("bell@mobidoc.us"))
        .andExpect(jsonPath("$.writer.nickname").value("bell"));
  }
}