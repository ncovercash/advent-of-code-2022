package dev.ncovercash;

import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day6 extends AbstractDay {

  public Day6(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("6.txt");

    String input = allInput.get(0);

    for (int i = 3; i < input.length(); i++) {
      if (input.subSequence(i - 3, i + 1).chars().distinct().count() == 4) {
        log.info("Part one solution: {}", i + 1);
        break;
      }
    }

    for (int i = 13; i < input.length(); i++) {
      if (input.subSequence(i - 13, i + 1).chars().distinct().count() == 14) {
        log.info("Part two solution: {}", i + 1);
        break;
      }
    }
  }
}
