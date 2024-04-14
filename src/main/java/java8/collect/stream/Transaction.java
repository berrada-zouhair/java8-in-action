package java8.collect.stream;

import lombok.ToString;

@ToString
public class Transaction {

  private final Currency currency;
  private final double amount;

  public Transaction(Currency currency, double amount) {
    this.currency = currency;
    this.amount = amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public boolean isExpensive() {
    return amount >= 50;
  }

  public double getAmount() {
    return amount;
  }
}
