package java8;

import java8.lambda.and.method.reference.Apple;
import java8.lambda.and.method.reference.AppleAssertion;
import org.assertj.core.api.Assertions;

public class ProjectAssertions extends Assertions {

  public static AppleAssertion assertThat(Apple apple) {
    return new AppleAssertion(apple);
  }

}
