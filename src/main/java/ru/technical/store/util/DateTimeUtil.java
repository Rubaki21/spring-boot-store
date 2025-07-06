package ru.technical.store.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateTimeUtil {

  private static final String DATE_TIME_PATTERN_FOR_RESULT = "yyyy-MM-dd HH:mm:ss";

  public static String nowDateTime() {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN_FOR_RESULT);
    return LocalDateTime.now().format(formatter);
  }
}
