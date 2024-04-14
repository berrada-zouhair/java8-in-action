package java8.lambda.and.method.reference;

import static java8.ProjectAssertions.assertThat;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppleTest {

  private List<Apple> APPLES;
  private Apple RED_APPLE = new Apple(Color.RED, 100);
  private Apple GREEN_APPLE = new Apple(Color.GREEN, 80);
  private Apple YELLOW_APPLE = new Apple(Color.YELLOW, 120);

  private Consumer<Apple> displayApple = System.out::println; // accept method
  private Function<Apple, String> appleToColor = apple -> apple.getColor()
      .toString(); // apply method
  private Supplier<List<Apple>> applesGenerator = () -> asList(
      RED_APPLE,
      GREEN_APPLE,
      YELLOW_APPLE
  );


  @BeforeEach
  void setUp() {
    APPLES = applesGenerator.get();
  }

  @Test
  void should_display_apples() {
    APPLES.forEach(displayApple.andThen(displayApple));
  }

  @Test
  void should_map_apples_to_colors() {
    String colors = APPLES.stream()
        .map(appleToColor)
        .collect(joining(","));
    assertThat(colors).isEqualTo("RED,GREEN,YELLOW");
  }

  @Test
  void should_get_heavy_apples() {
    List<Apple> heavyApples = APPLES.stream()
        .filter(Apple::isHeavy)
        .collect(toList());
    Assertions.assertThat(heavyApples)
        .hasSize(2)
        .containsExactly(RED_APPLE, YELLOW_APPLE);
  }

  @Test
  void should_get_lightweight_apples() {
    List<Apple> heavyApples = APPLES.stream()
        .filter(Apple::isLightWeight)
        .collect(toList());
    Assertions.assertThat(heavyApples)
        .hasSize(1)
        .containsExactly(GREEN_APPLE);
  }

  @Test
  void should_get_expensive_apples() {
    List<Apple> heavyApples = APPLES.stream()
        .filter(Apple::isExpensive)
        .collect(toList());
    Assertions.assertThat(heavyApples)
        .hasSize(3)
        .containsExactly(RED_APPLE, GREEN_APPLE, YELLOW_APPLE);
  }

  @Test
  void apple_should_be_comparable() {
    assertThat(RED_APPLE).isInstanceOf(Comparable.class);
  }

  @Test
  void red_apple_should_be_heavier_than_green() {
    int comparison = RED_APPLE.compareTo(GREEN_APPLE);
    assertThat(comparison).isGreaterThan(0);
  }

  @Test
  void should_sort_apple_by_weight_descending() {
    List<Apple> sortedApples = APPLES.stream()
//        .sorted((a1, a2) -> a2.getWeight() - a1.getWeight())
        .sorted(Comparator.reverseOrder())
        .collect(toList());
    Assertions.assertThat(sortedApples).containsExactly(YELLOW_APPLE, RED_APPLE, GREEN_APPLE);
  }

  @Test
  void should_sort_apple_by_color_ascending() {
    List<Apple> sortedApples = APPLES.stream()
        .sorted(Comparator.comparing(Apple::getColor))
        .collect(toList());
    Assertions.assertThat(sortedApples).containsExactly(RED_APPLE, GREEN_APPLE, YELLOW_APPLE);
  }

  @Test
  void should_sort_apples_ascending_by_color_then_by_weight() {
    List<Apple> apples = new ArrayList<>(APPLES);
    Apple anotherRedApple = new Apple(Color.RED, 110);
    apples.add(anotherRedApple);

    apples.sort(
        Comparator.comparing(Apple::getColor)
            .thenComparing(Apple::getWeight)
    );
    Assertions
        .assertThat(apples).containsExactly(RED_APPLE, anotherRedApple, GREEN_APPLE, YELLOW_APPLE);
  }

  @Test
  void should_allow_to_use_variables_inside_lambda_when_variables_are_final_or_not_reassigned() {
    int minWeight = 80;
    final Color green = Color.GREEN;

    List<Apple> applesMatching = APPLES.stream()
        .filter(apple -> apple.matches(green, minWeight))
        .collect(toList());
    Assertions.assertThat(applesMatching)
        .hasSize(1)
        .containsExactly(GREEN_APPLE);
  }

  @Test
  void should_use_method_reference_with_two_parameters() {
    List<String> sortedByAlphaColor = APPLES.stream()
        .map(apple -> apple.getColor().toString())
        .sorted(String::compareToIgnoreCase)
        .collect(toList());

    assertThat(sortedByAlphaColor)
        .hasSize(3)
        .containsExactly("GREEN", "RED", "YELLOW");
  }

  @Test
  void should_use_method_reference_with_two_parameters_2() {
    BiPredicate<List<Apple>, Apple> contains = List::contains;
    boolean isRedAppleInApplesList = contains.test(APPLES, RED_APPLE);
    assertThat(isRedAppleInApplesList).isTrue();
  }

  @Test
  void should_use_method_reference_with_two_parameters_3() {
    BiFunction<Color, Integer, Apple> colorAndWeightToApple = Apple::new;
    Apple apple = colorAndWeightToApple.apply(Color.GREEN, 200);
    assertThat(apple).hasColor(Color.GREEN).hasWeight(200);
  }

  @Test
  void should_chain_function_calls() {
    BiFunction<Color, Integer, Apple> colorAndWeightToApple = Apple::new;
    String appleToSting = colorAndWeightToApple.andThen(Apple::toString).apply(Color.YELLOW, 200);
    assertThat(appleToSting).isEqualTo("Apple(color=YELLOW, weight=200)");

  }
}
