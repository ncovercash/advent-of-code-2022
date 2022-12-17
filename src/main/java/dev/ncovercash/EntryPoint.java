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
      case 2:
        return new Day2(dayArgs);
      case 3:
        return new Day3(dayArgs);
      case 4:
        return new Day4(dayArgs);
      case 5:
        return new Day5(dayArgs);
      case 6:
        return new Day6(dayArgs);
      case 7:
        return new Day7(dayArgs);
      case 8:
        return new Day8(dayArgs);
      case 9:
        return new Day9(dayArgs);
      case 10:
        return new Day10(dayArgs);
      case 11:
        return new Day11(dayArgs);
      case 12:
        return new Day12(dayArgs);
      case 13:
        return new Day13(dayArgs);
      case 14:
        return new Day14(dayArgs);
      case 15:
        return new Day15(dayArgs);
      case 16:
        return new Day16(dayArgs);
      default:
        log.error("Unknown day {}", day);
        throw new IllegalArgumentException("Unknown day provided");
    }
  }
}
