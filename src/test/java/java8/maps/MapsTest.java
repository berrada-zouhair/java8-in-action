package java8.maps;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapsTest {

  Map<String, Integer> inventory;

  @BeforeEach
  void setUp() {
    inventory = new HashMap<>();
    inventory.put("hp", null);
    inventory.put("lenovo", 100);
    inventory.put("huwawei", 200);
  }

  @Test
  void should_get_or_default_value() {

    Integer macBookPrice = inventory.getOrDefault("mac book", 0);
    Integer hpPrice = inventory.getOrDefault("hp", 0);

    assertThat(macBookPrice).isEqualTo(0);
    assertThat(hpPrice).isNull();
  }

  @Test
  void should_compute_if_absent() {
    inventory.computeIfAbsent("dell", k -> inventory.values()
        .stream()
        .filter(Objects::nonNull)
        .mapToInt(Integer::new)
        .sum());
    assertThat(inventory.get("dell")).isEqualTo(300);
  }

  @Test
  void should_merge() {
    Integer lenovoPrice = inventory.merge("lenovo", 50, Integer::sum);
    assertThat(lenovoPrice).isEqualTo(150);
  }
}
