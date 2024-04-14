package java8.dates;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class DatesTest {

  @Test
  void should_create_local_date() {
    LocalDate date = LocalDate.of(2023, Month.NOVEMBER, 1);
    System.out.println(date);

    LocalDate now = LocalDate.now();
    System.out.println(now);
    int year = now.get(ChronoField.YEAR);
    int month = now.get(ChronoField.MONTH_OF_YEAR);
    int day = now.get(ChronoField.DAY_OF_MONTH);
    System.out.println(year + "-" + month + "-" + day);
  }

  @Test
  void should_create_local_time() {
    LocalTime now = LocalTime.now();
    System.out.println(now);
    int hour = now.get(ChronoField.HOUR_OF_DAY);
    int minute = now.get(ChronoField.MINUTE_OF_HOUR);
    int second = now.get(ChronoField.SECOND_OF_MINUTE);
    int millisecond = now.get(ChronoField.MILLI_OF_SECOND);
    System.out.println(hour + ":" + minute + ":" + second + ":" + millisecond);
  }

  @Test
  void should_parse_date_and_time() {
    LocalDate localDate = LocalDate.parse("2023-11-16");
    LocalTime time = LocalTime.parse("13:26:59");
    System.out.println(localDate);
    System.out.println(time);
  }

  @Test
  void should_combine_date_and_time() {
    LocalDateTime dateTime = LocalDateTime.of(2023, Month.NOVEMBER, 16, 14, 55, 59);
    System.out.println(dateTime.toLocalDate());
    System.out.println(dateTime.toLocalTime());
  }

  @Test
  void should_handle_instant() {
    Date date = new Date();
    Instant instant = Instant.ofEpochMilli(date.getTime());
    System.out.println(instant);
  }

  @Test
  void should_handle_duration_and_period() {
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
    Period period = Period.between(today, tomorrow);
    System.out.println(period.getDays());
    System.out.println(period.getMonths());
    System.out.println(period.getYears());

    LocalTime now = LocalTime.now();
    LocalTime oneHourLater = now.plus(1, ChronoUnit.HOURS);
    Duration duration = Duration.between(now, oneHourLater);
    System.out.println(duration.getSeconds());
  }

  @Test
  void should_set_dates_times() {
    LocalDate setDate = LocalDate.now().with(ChronoField.DAY_OF_MONTH, 30);
    LocalTime setTime = LocalTime.now().with(ChronoField.HOUR_OF_DAY, 20);
    System.out.println(setDate);
    System.out.println(setTime);
  }

  @Test
  void should_handle_temporal_adjusters() {
    LocalDate nextOrSameSunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
    LocalDate lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

    System.out.println(nextOrSameSunday);
    System.out.println(lastDayOfMonth);
  }

  @Test
  void should_get_next_working_day() {
    TemporalAdjuster nextWorkingDay = temporal -> {
      int dayOfWeek = temporal.get(ChronoField.DAY_OF_WEEK);
      int daysToAdd = 1;
      if (DayOfWeek.SATURDAY.getValue() == dayOfWeek) {
        daysToAdd = 2;
      }
      if (DayOfWeek.FRIDAY.getValue() == dayOfWeek) {
        daysToAdd = 3;
      }
      return temporal.plus(daysToAdd, ChronoUnit.DAYS);
    };
    LocalDate date = LocalDate.of(2023, Month.NOVEMBER, 26);
    LocalDate with = date.with(nextWorkingDay);
    System.out.println(with);

    date = LocalDate.of(2023, Month.NOVEMBER, 25);
    with = date.with(nextWorkingDay);
    System.out.println(with);
  }

  @Test
  void should_format_date() {
    LocalDate now = LocalDate.now();
    String format1 = now.format(DateTimeFormatter.BASIC_ISO_DATE);
    String format2 = now.format(DateTimeFormatter.ISO_DATE);
    System.out.println(format1);
    System.out.println(format2);

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    String format3 = now.format(dateTimeFormatter);
    System.out.println(format3);
  }
}
