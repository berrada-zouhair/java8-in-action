package java8.lambda.and.method.reference;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class AppleAssertion extends AbstractAssert<AppleAssertion, Apple> {

  public AppleAssertion(Apple apple) {
    super(apple, AppleAssertion.class);
  }

  public static AppleAssertion assertThat(Apple apple) {
    return new AppleAssertion(apple);
  }

  public AppleAssertion hasColor(Color color) {
    isNotNull();
    Assertions.assertThat(actual.getColor()).isEqualTo(color);
    return this;
  }

  public AppleAssertion hasWeight(int weight) {
    isNotNull();
    Assertions.assertThat(actual.getWeight()).isEqualTo(weight);
    return this;
  }
}
