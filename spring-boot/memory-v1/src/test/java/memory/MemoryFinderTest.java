package memory;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemoryFinderTest {
  @Test
  void get() {
    MemoryFinder memoryFinder = new MemoryFinder();
    Memory memory = memoryFinder.get();
    assertThat(memory).isNotNull();
  }
}