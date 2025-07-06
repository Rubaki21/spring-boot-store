package ru.technical.store;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@RequiredArgsConstructor
class StoreApplicationIT {

  private final ApplicationContext ctx;

  @Test
  void contextLoads() {
    Assertions.assertThat(ctx).isNotNull();
  }
}
