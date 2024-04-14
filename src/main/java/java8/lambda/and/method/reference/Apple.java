package java8.lambda.and.method.reference;

import java.util.function.Predicate;
import lombok.ToString;

@ToString(exclude = {"isBright", "isHeavy"})
public class Apple implements Comparable<Apple> {

  private final Color color;
  private final int weight;

  private Predicate<Apple> isHeavy = apple -> apple.getWeight() >= 100;
  private Predicate<Apple> isBright = apple -> apple.getColor().equals(Color.GREEN);

  public Apple(Color color, int weight) {
    this.color = color;
    this.weight = weight;
  }

  public Color getColor() {
    return color;
  }

  public int getWeight() {
    return weight;
  }

  public boolean isHeavy() {
    return isHeavy.test(this);
  }

  @Override
  public int compareTo(Apple apple) {
    return weight - apple.weight;
  }

  public boolean matches(Color color, int weight) {
    return this.weight == weight && this.color == color;
  }

  public boolean isLightWeight() {
    return isHeavy.negate().test(this);
  }

  public boolean isExpensive() {
    return isHeavy.or(isBright).test(this);
  }
}
