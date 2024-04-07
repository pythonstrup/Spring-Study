package com.test.thejavatest.study;

import com.test.thejavatest.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyController {

  private final StudyRepository studyRepository;

  @GetMapping("/study/{id}")
  public Study getStudy(@PathVariable Long id) {
    return studyRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Study not found for " + id));
  }

  @PostMapping("/login")
  public Study createStudy(@RequestBody Study study) {
    return studyRepository.save(study);
  }
}
