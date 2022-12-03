package dev.ncovercash;

import java.util.Arrays;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EntryPoint {

  private static final Pattern NOT_NUM_REGEX = Pattern.compile("[^\\d.]");

  public static void main(String[] args) {
    if (args.length < 1) {
      log.error("Please provide at least one parameter (the day number)");
      System.exit(1);
    }
    String[] dayArgs = Arrays.copyOfRange(args, 1, args.length);

    try {
      int day = Integer.parseInt(NOT_NUM_REGEX.matcher(args[0]).replaceAll(""));
      getDay(day, dayArgs).run();
    } catch (IllegalArgumentException e) {
      log.error(e);
      e.printStackTrace();
    }
  }

  public static AbstractDay getDay(int day, String[] dayArgs) {
    switch (day) {
      case 1:
        return new Day1(dayArgs);
      default:
        log.error("Unknown day {}", day);
        throw new IllegalArgumentException("Unknown day provided");
    }
  }
}