package dev.ncovercash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day21 extends AbstractDay {

  @Data
  static class Monkey {

    enum Operation {
      PLUS,
      MINUS,
      TIMES,
      DIVIDED_BY,
      EQUALS,
    }

    Operation op;
    String monkeyA;
    String monkeyB;
    Long value;
    boolean resolved;

    public Monkey(long val) {
      this.value = val;
      this.resolved = true;
    }

    public Monkey(String monkeyA, String monkeyB, Operation op) {
      this.monkeyA = monkeyA;
      this.monkeyB = monkeyB;
      this.op = op;
    }

    public void resolve(long val) {
      this.value = val;
      this.resolved = true;
    }

    public static Operation getOp(String in) {
      switch (in) {
        case "+":
          return Operation.PLUS;
        case "-":
          return Operation.MINUS;
        case "*":
          return Operation.TIMES;
        case "/":
        default:
          return Operation.DIVIDED_BY;
      }
    }

    public static String getOpStr(Operation in) {
      switch (in) {
        case PLUS:
          return "+";
        case MINUS:
          return "-";
        case TIMES:
          return "*";
        case DIVIDED_BY:
          return "/";
        case EQUALS:
        default:
          return "=";
      }
    }

    public String toString(Map<String, Monkey> map) {
      if (this.isResolved()) {
        return Long.toString(value);
      }

      if (this.op == Operation.TIMES || this.op == Operation.DIVIDED_BY) {
        if ("humn".equals(monkeyA)) {
          return String.format(
            "(human %s %s)",
            getOpStr(op),
            map.get(monkeyB).toString(map)
          );
        }
        if ("humn".equals(monkeyB)) {
          return String.format(
            "(%s %s human)",
            map.get(monkeyA).toString(map),
            getOpStr(op)
          );
        }

        return String.format(
          "(%s %s %s)",
          map.get(monkeyA).toString(map),
          getOpStr(op),
          map.get(monkeyB).toString(map)
        );
      }

      if ("humn".equals(monkeyA)) {
        return String.format(
          "human %s %s",
          getOpStr(op),
          map.get(monkeyB).toString(map)
        );
      }
      if ("humn".equals(monkeyB)) {
        return String.format(
          "%s %s human",
          map.get(monkeyA).toString(map),
          getOpStr(op)
        );
      }

      return String.format(
        "%s %s %s",
        map.get(monkeyA).toString(map),
        getOpStr(op),
        map.get(monkeyB).toString(map)
      );
    }
  }

  public Day21(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("21.txt");

    Map<String, Monkey> monkeys = new HashMap<>();

    for (String line : allInput) {
      String[] split = line.split(": ");
      String monkeyName = split[0];

      if (split[1].contains(" ")) {
        String[] splitOp = split[1].split(" ");

        monkeys.put(
          monkeyName,
          new Monkey(splitOp[0], splitOp[2], Monkey.getOp(splitOp[1]))
        );
      } else {
        monkeys.put(monkeyName, new Monkey(Long.parseLong(split[1])));
      }
    }

    while (true) {
      boolean nonResolved = false;

      for (Monkey monkey : monkeys.values()) {
        if (!monkey.isResolved()) {
          if (
            monkeys.get(monkey.getMonkeyA()).isResolved() &&
            monkeys.get(monkey.getMonkeyB()).isResolved()
          ) {
            long a = monkeys.get(monkey.getMonkeyA()).getValue();
            long b = monkeys.get(monkey.getMonkeyB()).getValue();

            switch (monkey.getOp()) {
              case PLUS:
                monkey.resolve(a + b);
                break;
              case MINUS:
                monkey.resolve(a - b);
                break;
              case TIMES:
                monkey.resolve(a * b);
                break;
              case DIVIDED_BY:
              default:
                monkey.resolve(a / b);
                break;
            }
          } else {
            nonResolved = true;
          }
        }
      }

      if (!nonResolved) {
        break;
      }
    }

    log.info("Part one solution: {}", monkeys.get("root").getValue());

    // remove all for part 2
    monkeys = new HashMap<>();

    for (String line : allInput) {
      String[] split = line.split(": ");
      String monkeyName = split[0];

      if (split[1].contains(" ")) {
        String[] splitOp = split[1].split(" ");

        monkeys.put(
          monkeyName,
          new Monkey(splitOp[0], splitOp[2], Monkey.getOp(splitOp[1]))
        );
      } else {
        monkeys.put(monkeyName, new Monkey(Long.parseLong(split[1])));
      }
    }

    monkeys.put("humn", new Monkey("humn", "humn", Monkey.Operation.EQUALS));
    monkeys.put(
      "root",
      new Monkey(
        monkeys.get("root").getMonkeyA(),
        monkeys.get("root").getMonkeyB(),
        Monkey.Operation.EQUALS
      )
    );

    int numUnknown = Integer.MAX_VALUE;

    // solve all we can
    while (true) {
      int numUnknownThisTime = 0;

      for (Monkey monkey : monkeys.values()) {
        if (!monkey.isResolved()) {
          if (
            monkeys.get(monkey.getMonkeyA()).isResolved() &&
            monkeys.get(monkey.getMonkeyB()).isResolved()
          ) {
            long a = monkeys.get(monkey.getMonkeyA()).getValue();
            long b = monkeys.get(monkey.getMonkeyB()).getValue();

            switch (monkey.getOp()) {
              case PLUS:
                monkey.resolve(a + b);
                break;
              case MINUS:
                monkey.resolve(a - b);
                break;
              case TIMES:
                monkey.resolve(a * b);
                break;
              case DIVIDED_BY:
                monkey.resolve(a / b);
                break;
              default:
                // do nothing on =
                break;
            }
          } else {
            numUnknownThisTime++;
          }
        }
      }

      // no improvement
      if (numUnknownThisTime == numUnknown) {
        break;
      }

      numUnknown = numUnknownThisTime;
    }

    // we know it will now be (some math with human = constant)
    Monkey root = monkeys.get("root");
    Monkey humanSide = !monkeys.get(root.getMonkeyA()).isResolved()
      ? monkeys.get(root.getMonkeyA())
      : monkeys.get(root.getMonkeyB());
    long otherSide = monkeys.get(root.getMonkeyA()).isResolved()
      ? monkeys.get(root.getMonkeyA()).getValue()
      : monkeys.get(root.getMonkeyB()).getValue();

    monkeys.put("negative-1", new Monkey(-1));

    // repeatedly break down till only human is left
    while (true) {
      // humn: humn = humn (to ensure it remains unresolved)
      if (
        "humn".equals(humanSide.getMonkeyA()) &&
        "humn".equals(humanSide.getMonkeyB())
      ) {
        break;
      }

      if (monkeys.get(humanSide.getMonkeyA()).isResolved()) {
        switch (humanSide.getOp()) {
          case PLUS:
            otherSide -= monkeys.get(humanSide.getMonkeyA()).getValue();
            humanSide = monkeys.get(humanSide.getMonkeyB());
            break;
          case MINUS:
            otherSide -= monkeys.get(humanSide.getMonkeyA()).getValue();
            humanSide.setMonkeyA("negative-1");
            humanSide.setOp(Monkey.Operation.TIMES);
            break;
          case TIMES:
            otherSide /= monkeys.get(humanSide.getMonkeyA()).getValue();
            humanSide = monkeys.get(humanSide.getMonkeyB());
            break;
          case DIVIDED_BY:
          default:
            otherSide =
              monkeys.get(humanSide.getMonkeyA()).getValue() / otherSide;
            humanSide = monkeys.get(humanSide.getMonkeyB());
            break;
        }
      } else {
        switch (humanSide.getOp()) {
          case PLUS:
            otherSide -= monkeys.get(humanSide.getMonkeyB()).getValue();
            humanSide = monkeys.get(humanSide.getMonkeyA());
            break;
          case MINUS:
            otherSide += monkeys.get(humanSide.getMonkeyB()).getValue();
            humanSide = monkeys.get(humanSide.getMonkeyA());
            break;
          case TIMES:
            otherSide /= monkeys.get(humanSide.getMonkeyB()).getValue();
            humanSide = monkeys.get(humanSide.getMonkeyA());
            break;
          case DIVIDED_BY:
          default:
            otherSide *= monkeys.get(humanSide.getMonkeyB()).getValue();
            humanSide = monkeys.get(humanSide.getMonkeyA());
            break;
        }
      }
    }

    log.info("Part two solution: {}", otherSide);
  }
}
