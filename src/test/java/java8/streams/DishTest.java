package java8.streams;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java8.streams.DishType.BRITISH;
import static java8.streams.DishType.GERMAN;
import static java8.streams.DishType.MOROCCAN;
import static java8.streams.DishType.TURKISH;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class DishTest {

  private Dish moroccan1 = new Dish("Dish1", 450, MOROCCAN);
  private Dish british = new Dish("Dish2", 200, BRITISH);
  private Dish german = new Dish("Dish3", 400, GERMAN);
  private Dish turkish = new Dish("Dish4", 350, TURKISH);
  private Dish moroccan2 = new Dish("Dish5", 900, MOROCCAN);
  private Collection<Dish> dishes = Arrays.asList(
      moroccan1,
      british,
      german,
      turkish,
      moroccan2
  );


  @Test
  void should_get_names_of_dishes_with_low_calories() {
    List<String> lowDishesCalories = dishes.stream()
        .filter(Dish::isLowCalories)
        .map(Dish::getName)
        .collect(toList());
    assertThat(lowDishesCalories).containsExactly("Dish2", "Dish4");
  }

  @Test
  void should_group_dishes_by_type() {
    Map<DishType, List<Dish>> dishesByType = dishes.stream()
        .collect(groupingBy(Dish::getDishType));

    assertThat(dishesByType)
        .hasSize(4)
        .containsOnlyKeys(MOROCCAN, TURKISH, BRITISH, GERMAN);
    assertThat(dishesByType.get(MOROCCAN))
        .hasSize(2)
        .containsExactly(moroccan1, moroccan2);
    assertThat(dishesByType.get(GERMAN))
        .hasSize(1)
        .containsExactly(german);
    assertThat(dishesByType.get(BRITISH))
        .hasSize(1)
        .containsExactly(british);
    assertThat(dishesByType.get(TURKISH))
        .hasSize(1)
        .containsExactly(turkish);
  }

  @Test
  void should_limit_stream_size() {
    List<DishType> dishTypes = dishes.stream()
        .map(Dish::getDishType)
        .limit(2)
        .collect(toList());
    assertThat(dishTypes)
        .hasSize(2)
        .containsExactly(MOROCCAN, BRITISH);
  }

  @Test
  void should_skip_elements() {
    long countElements = dishes.stream().skip(2).count();
    assertThat(countElements).isEqualTo(3);
  }

  @Test
  void should_flat_stream_of_array() {
    List<String> letters = Arrays.stream(new String[]{"hello", "world"})
        .map(word -> word.split(""))
        .flatMap(Arrays::stream)
        .distinct()
        .collect(toList());
    assertThat(letters).containsExactly("h", "e", "l", "o", "w", "r", "d");
  }

  @Test
  void should_flat_stream_of_list() {
    List<String> letters = Arrays.asList("hello", "world").stream()
        .map(word -> Arrays.stream(word.split("")).collect(toList()))
        .flatMap(Collection::stream)
        .distinct()
        .collect(toList());
    assertThat(letters).containsExactly("h", "e", "l", "o", "w", "r", "d");
  }

  @Test
  void should_get_pairs_of_each_elements_in_both_tables() {
    List<Integer> numbers1 = Arrays.asList(1, 2, 3);
    List<Integer> numbers2 = Arrays.asList(3, 4);
    List<String> collect = numbers1.stream()
        .flatMap(
            aNumber1 -> numbers2.stream()
                .map(aNumber2 -> Arrays.toString(new int[]{aNumber1, aNumber2}))
        ).collect(toList());
    assertThat(collect).containsExactly("[1, 3]", "[1, 4]", "[2, 3]", "[2, 4]", "[3, 3]", "[3, 4]");
  }

  @Test
  void should_check_if_any_moroccan_dish() {
    boolean anyMatchMoroccan = dishes.stream().anyMatch(dish -> dish.isOfType(MOROCCAN));
    assertThat(anyMatchMoroccan).isTrue();
  }

  @Test
  void should_check_that_all_dishes_are_healthy() {
    boolean areAllDishesHealthy = dishes.stream().allMatch(Dish::isHealthy);
    assertThat(areAllDishesHealthy).isTrue();
  }

  @Test
  void should_check_that_none_unhealthy_dish_exist() {
    boolean isNoneHealtyAbsent = dishes.stream().noneMatch(dish -> !dish.isHealthy());
    assertThat(isNoneHealtyAbsent).isTrue();
  }

  @Test
  void should_find_any_moroccan_dish() {
    Optional<Dish> anyMoroccanDish = dishes.stream()
        .filter(dish -> dish.isOfType(MOROCCAN))
        .findAny();
    assertThat(anyMoroccanDish).isPresent();
  }

  @Test
  void should_sum_dishes_calories() {
    Integer sumCalories = dishes.stream()
        .map(Dish::getCalories)
        .reduce(0, Integer::sum);
    assertThat(sumCalories).isEqualTo(2300);
  }

  @Test
  void should_sum_dishes_calories_with_no_initial_value() {
    Optional<Integer> sumCalories = dishes.stream()
        .map(Dish::getCalories)
        .reduce(Integer::sum);
    assertThat(sumCalories).isPresent().hasValue(2300);
  }

  @Test
  void should_sum_dishes_calories_with_stream_method() {
    int sumCalories = dishes.stream()
        .mapToInt(Dish::getCalories)
        .sum();
    assertThat(sumCalories).isEqualTo(2300);
  }

  @Test
  void should_get_max_and_min_dishes_calories() {
    OptionalInt max = dishes.stream()
        .mapToInt(Dish::getCalories)
        .max();
    OptionalInt min = dishes.stream()
        .mapToInt(Dish::getCalories)
        .min();
    assertThat(max).isPresent().hasValue(900);
    assertThat(min).isPresent().hasValue(200);
  }

  @Test
  void should_generate_range_stream() {
    List<Integer> integers = IntStream.rangeClosed(0, 10)
        .filter(value -> value % 2 == 0)
        .boxed()
        .collect(toList());
    assertThat(integers).hasSize(6)
        .containsExactlyInAnyOrder(0, 2, 4, 6, 8 , 10);
  }

  @Test
  void should_create_stream_from_array() {
    int sum = Arrays.stream(new int[]{1, 2, 5, 20}).sum();
    assertThat(sum).isEqualTo(28);
  }

  @Test
  void should_generate_stream_from_a_seed() {
    List<Integer> collect1 = Stream.iterate(0, n -> n + 2).limit(5).collect(toList());

    Supplier<Double> supplier = Math::random;
    List<Double> collect2 = Stream.generate(supplier).limit(5).collect(toList());

    assertThat(collect1).containsExactly(0, 2, 4, 6, 8);
    assertThat(collect2).hasSize(5);
  }

  @Test
  void should_generate_fibonacci_series() {
    Supplier<Integer[]> fibonacci = new Supplier<Integer[]>() {

      private int left = 0;
      private int right = 1;

      private boolean isFirstElement() {
        return left == 0 && right == 0;
      }

      @Override
      public Integer[] get() {
        if (isFirstElement()) {
          return new Integer[] {0, 1};
        }
        Integer[] generatedTuple = {right, (left + right)};
        this.left = right;
        return generatedTuple;
      }
    };
  }
}
