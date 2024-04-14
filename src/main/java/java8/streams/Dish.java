package java8.streams;

public class Dish {

  private final String name;
  private final int calories;
  private DishType dishType;


  public Dish(String name, int calories, DishType dishType) {
    this.name = name;
    this.calories = calories;
    this.dishType = dishType;
  }

  public boolean isLowCalories() {
    return calories < 400;
  }

  public String getName() {
    return name;
  }

  public DishType getDishType() {
    return dishType;
  }

  public boolean isOfType(DishType dishType) {
    return this.dishType == dishType;
  }

  public boolean isHealthy() {
    return this.calories < 950;
  }

  public int getCalories() {
    return calories;
  }
}
