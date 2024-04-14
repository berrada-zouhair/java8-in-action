package java8.collect.stream;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java8.ProjectAssertions.assertThat;
import static java8.collect.stream.Currency.EUR;
import static java8.collect.stream.Currency.MAD;
import static java8.collect.stream.Currency.USD;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionTest {

  private Transaction mad1;
  private Transaction usd;
  private Transaction eur1;
  private Transaction eur2;
  private Transaction mad2;
  private List<Transaction> transactions;

  @BeforeEach
  void setUp() {
    mad1 = new Transaction(MAD, 100.5);
    usd = new Transaction(USD, 50);
    eur1 = new Transaction(EUR, 30);
    eur2 = new Transaction(EUR, 5.2);
    mad2 = new Transaction(MAD, 300.7);
    transactions = asList(
        mad1,
        usd,
        eur1,
        eur2,
        mad2
    );
  }

  @Test
  void should_group_transactions_by_currency() {
    Map<Currency, List<Transaction>> transactionsByCurrency = transactions.stream()
        .collect(groupingBy(Transaction::getCurrency));
    assertThat(transactionsByCurrency)
        .hasSize(3)
        .containsKeys(MAD, USD, EUR);
    assertThat(transactionsByCurrency.get(MAD)).containsExactly(mad1, mad2);
    assertThat(transactionsByCurrency.get(EUR)).containsExactly(eur1, eur2);
    assertThat(transactionsByCurrency.get(USD)).containsExactly(usd);
  }

  @Test
  void should_group_transactions_by_expensiveness() {
    Map<Boolean, List<Transaction>> transactionsByExpensiveness = transactions.stream()
        .collect(groupingBy(Transaction::isExpensive));
    assertThat(transactionsByExpensiveness)
        .hasSize(2)
        .containsKeys(TRUE, FALSE);
    assertThat(transactionsByExpensiveness.get(TRUE)).containsExactly(mad1, usd, mad2);
    assertThat(transactionsByExpensiveness.get(FALSE)).containsExactly(eur1, eur2);
  }

  @Test
  void should_get_max_transaction_amount() {
    Optional<Transaction> max = transactions.stream()
        .collect(Collectors.maxBy(Comparator.comparing(Transaction::getAmount)));
    assertThat(max).isPresent().hasValue(mad2);

    max = transactions.stream()
        .max(Comparator.comparing(Transaction::getAmount));
    assertThat(max).isPresent().hasValue(mad2);
  }

  @Test
  void should_get_transactions_count() {
    Long count = transactions.stream().collect(Collectors.counting());
    assertThat(count).isEqualTo(5);

    count = transactions.stream().count();
    assertThat(count).isEqualTo(5);
  }

  @Test
  void should_get_transactions_average() {
    Double average = transactions.stream()
        .collect(Collectors.averagingDouble(Transaction::getAmount));
    assertThat(average).isEqualTo(97.28);
  }

  @Test
  void should_get_transactions_summary() {
    DoubleSummaryStatistics summary = transactions.stream()
        .collect(Collectors.summarizingDouble(Transaction::getAmount));
    assertThat(summary.getAverage()).isEqualTo(97.28);
    assertThat(summary.getCount()).isEqualTo(5);
    assertThat(summary.getMax()).isEqualTo(300.7);
    assertThat(summary.getMin()).isEqualTo(5.2);
  }

  @Test
  void should_join_transactions_currencies_having_amount_greater_than_average() {
    Double average = transactions.stream()
        .collect(Collectors.averagingDouble(Transaction::getAmount));
    String currencies = transactions.stream()
        .filter(transaction -> transaction.getAmount() >= average)
        .map(transaction -> transaction.getCurrency().toString())
        .distinct()
        .collect(Collectors.joining());
    assertThat(currencies).isEqualTo("MAD");
  }

  @Test
  void should_get_transactions_amounts_sum() {
    Double sum = transactions.stream()
        .collect(Collectors.reducing((double) 0, Transaction::getAmount, Double::sum));
    assertThat(sum).isEqualTo(486.4);

    sum = transactions.stream()
        .mapToDouble(Transaction::getAmount)
        .sum();
    assertThat(sum).isEqualTo(486.4);
  }

  @Test
  void should_multi_level_group_transaction() {
    Transaction t1 = new Transaction(MAD, 10);
    Transaction t2 = new Transaction(USD, 50);
    Transaction t3 = new Transaction(EUR, 60);
    Transaction t4 = new Transaction(EUR, 20);
    Transaction t5 = new Transaction(MAD, 300);
    List<Transaction> transactions = asList(
        t1,
        t2,
        t3,
        t4,
        t5
    );
    Map<Currency, Map<Boolean, List<Transaction>>> groupedByCurrencyThenExpensiveness = transactions
        .stream()
        .collect(
            groupingBy(
                Transaction::getCurrency,
                groupingBy(Transaction::isExpensive)
            )
        );
    assertThat(groupedByCurrencyThenExpensiveness)
        .hasSize(3)
        .containsKeys(MAD, USD, EUR);

    assertThat(groupedByCurrencyThenExpensiveness.get(MAD))
        .hasSize(2)
        .containsKeys(FALSE, TRUE);
    assertThat(groupedByCurrencyThenExpensiveness.get(MAD).get(FALSE)).containsExactly(t1);
    assertThat(groupedByCurrencyThenExpensiveness.get(MAD).get(TRUE)).containsExactly(t5);

    assertThat(groupedByCurrencyThenExpensiveness.get(EUR))
        .hasSize(2)
        .containsKeys(FALSE, TRUE);
    assertThat(groupedByCurrencyThenExpensiveness.get(EUR).get(FALSE)).containsExactly(t4);
    assertThat(groupedByCurrencyThenExpensiveness.get(EUR).get(TRUE)).containsExactly(t3);

    assertThat(groupedByCurrencyThenExpensiveness.get(USD))
        .hasSize(1)
        .containsKeys(TRUE);
    assertThat(groupedByCurrencyThenExpensiveness.get(USD).get(TRUE)).containsExactly(t2);
  }

  @Test
  void should_count_transactions_by_currency() {
    Map<Currency, Long> collect = transactions.stream()
        .collect(groupingBy(Transaction::getCurrency, Collectors.counting()));
    assertThat(collect)
        .hasSize(3)
        .containsKeys(MAD, USD, EUR);
    assertThat(collect.get(MAD)).isEqualTo(2);
    assertThat(collect.get(EUR)).isEqualTo(2);
    assertThat(collect.get(USD)).isEqualTo(1);

  }

  @Test
  void should_get_max_transactions_by_currency() {
    Map<Currency, Optional<Transaction>> maxTransactionsByCurrency = transactions.stream()
        .collect(
            groupingBy(Transaction::getCurrency,
                Collectors.maxBy(Comparator.comparing(Transaction::getAmount)))
        );
    assertThat(maxTransactionsByCurrency)
        .hasSize(3)
        .containsKeys(MAD, USD, EUR);
    assertThat(maxTransactionsByCurrency.get(MAD)).hasValue(mad2);
    assertThat(maxTransactionsByCurrency.get(EUR)).hasValue(eur1);
    assertThat(maxTransactionsByCurrency.get(USD)).hasValue(usd);
  }

  @Test
  void should_get_count_expensive_and_cheap_transactions_by_currency() {
    Transaction t1 = new Transaction(MAD, 10);
    Transaction t2 = new Transaction(USD, 50);
    Transaction t3 = new Transaction(EUR, 60);
    Transaction t4 = new Transaction(EUR, 20);
    Transaction t5 = new Transaction(MAD, 300);
    List<Transaction> transactions = asList(
        t1,
        t2,
        t3,
        t4,
        t5
    );
    Map<Currency, Map<Boolean, Long>> collect = transactions.stream()
        .collect(
            groupingBy(Transaction::getCurrency,
                groupingBy(Transaction::isExpensive, Collectors.counting())
            ));
    System.out.println(collect);
  }

  @Test
  void collect_and_then() {
    Transaction maxTransaction = transactions.stream().collect(
        Collectors.collectingAndThen(
            Collectors.maxBy(Comparator.comparing(Transaction::getAmount)),
            Optional::get
        )
    );
    assertThat(maxTransaction).isEqualTo(mad2);
  }

  @Test
  void should_partitionate_transaction_basing_on_expensiveness() {
    Map<Boolean, List<Transaction>> transactionsByExpensiveness = transactions.stream()
        .collect(Collectors.partitioningBy(Transaction::isExpensive));
    assertThat(transactionsByExpensiveness).hasSize(2).containsKeys(TRUE, FALSE);
    assertThat(transactionsByExpensiveness.get(TRUE)).containsExactly(mad1, usd, mad2);
    assertThat(transactionsByExpensiveness.get(FALSE)).containsExactly(eur1, eur2);
  }

//  @Test
//  void should_get_max_transaction_by_currency() {
//    Collector<Transaction, Object, Object> x = Collectors
//        .collectingAndThen(Collectors.maxBy(Comparator.comparing(Transaction::getAmount))
//            Comparator.comparing(Transaction::getAmount));
//
//
//
//    Map<Currency, Double> maxTransactionsAmount = transactions.stream().collect(
//        groupingBy(Transaction::getCurrency,
//            Collectors
//                .collectingAndThen(Collectors.maxBy(Comparator.comparing(Transaction::getAmount))
//                    Comparator.comparing(Transaction::getAmount)))
//    );
//    assertThat(maxTransactionsAmount).hasSize(3).containsKeys(MAD, USD, EUR);
//  }
}
