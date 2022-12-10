package dev.ncovercash;

import java.io.StringWriter;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day10 extends AbstractDay {

  public int getStrengthAddition(int cycle, int register) {
    if ((cycle - 20) % 40 == 0) {
      return register * cycle;
    }
    return 0;
  }

  private String getCrtPixel(int cycle, int register) {
    int x = (cycle - 1) % 40;
    if (Math.abs(x - register) <= 1) {
      return "#";
    } else {
      return ".";
    }
  }

  public Day10(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("10.txt");

    int register = 1;
    int cycle = 1;

    int strength = 0;

    for (String instruction : allInput) {
      if (instruction.startsWith("addx")) {
        strength += getStrengthAddition(cycle, register);
        cycle++;
        strength += getStrengthAddition(cycle, register);
        cycle++;
        int change = Integer.parseInt(instruction.substring(5));
        register += change;
      } else {
        strength += getStrengthAddition(cycle, register);
        cycle++;
      }
    }

    log.info("Part 1 solution: {}", strength);

    StringWriter sw = new StringWriter();

    register = 1;
    cycle = 1;

    for (String instruction : allInput) {
      if (instruction.startsWith("addx")) {
        sw.write(getCrtPixel(cycle, register));
        cycle++;
        sw.write(getCrtPixel(cycle, register));
        cycle++;
        int change = Integer.parseInt(instruction.substring(5));
        register += change;
      } else {
        sw.write(getCrtPixel(cycle, register));
        cycle++;
      }
    }

    log.info("Part 2 solution:");
    String solution = sw.toString();
    for (int i = 0; i < solution.length(); i += 40) {
      log.info(solution.substring(i, i + 40));
    }
  }
}
