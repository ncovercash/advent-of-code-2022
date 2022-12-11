package dev.ncovercash;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day11 extends AbstractDay {

  @Data
  class Monkey {

    List<BigInteger> originalItems;
    List<BigInteger> items;
    UnaryOperator<BigInteger> operation;
    Function<BigInteger, Integer> throwDetermination;
    int inspections;

    public Monkey(
      ArrayList<Integer> list,
      UnaryOperator<BigInteger> operation,
      Function<BigInteger, Integer> throwDetermination
    ) {
      this.items = new ArrayList<>();
      this.originalItems = new ArrayList<>();
      list.stream().map(t -> new BigInteger(t.toString())).forEach(items::add);
      list
        .stream()
        .map(t -> new BigInteger(t.toString()))
        .forEach(originalItems::add);

      this.operation = operation;
      this.throwDetermination = throwDetermination;

      this.inspections = 0;
    }

    public void inspect() {
      inspections++;
    }
  }

  public Day11(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    // LCM of all modulus ops
    BigInteger MOD = new BigInteger("9699690");

    List<Monkey> monkeys = new ArrayList<>();

    monkeys.add(
      new Monkey(
        new ArrayList<>(Arrays.asList(54, 82, 90, 88, 86, 54)),
        w -> w.multiply(new BigInteger("7")),
        w -> w.mod(new BigInteger("11")).intValueExact() == 0 ? 2 : 6
      )
    );
    monkeys.add(
      new Monkey(
        new ArrayList<>(Arrays.asList(91, 65)),
        w -> w.multiply(new BigInteger("13")),
        w -> w.mod(new BigInteger("5")).intValueExact() == 0 ? 7 : 4
      )
    );
    monkeys.add(
      new Monkey(
        new ArrayList<>(Arrays.asList(62, 54, 57, 92, 83, 63, 63)),
        w -> w.add(BigInteger.ONE),
        w -> w.mod(new BigInteger("7")).intValueExact() == 0 ? 1 : 7
      )
    );
    monkeys.add(
      new Monkey(
        new ArrayList<>(Arrays.asList(67, 72, 68)),
        w -> w.multiply(w),
        w -> w.mod(new BigInteger("2")).intValueExact() == 0 ? 0 : 6
      )
    );
    monkeys.add(
      new Monkey(
        new ArrayList<>(Arrays.asList(68, 89, 90, 86, 84, 57, 72, 84)),
        w -> w.add(new BigInteger("7")),
        w -> w.mod(new BigInteger("17")).intValueExact() == 0 ? 3 : 5
      )
    );
    monkeys.add(
      new Monkey(
        new ArrayList<>(Arrays.asList(79, 83, 64, 58)),
        w -> w.add(new BigInteger("6")),
        w -> w.mod(new BigInteger("13")).intValueExact() == 0 ? 3 : 0
      )
    );
    monkeys.add(
      new Monkey(
        new ArrayList<>(Arrays.asList(96, 72, 89, 70, 88)),
        w -> w.add(new BigInteger("4")),
        w -> w.mod(new BigInteger("3")).intValueExact() == 0 ? 1 : 2
      )
    );
    monkeys.add(
      new Monkey(
        new ArrayList<>(Arrays.asList(79)),
        w -> w.add(new BigInteger("8")),
        w -> w.mod(new BigInteger("19")).intValueExact() == 0 ? 4 : 5
      )
    );

    for (int i = 0; i < 20; i++) {
      monkeys.forEach(monkey -> {
        for (BigInteger item : monkey.getItems()) {
          monkey.inspect();
          item = monkey.getOperation().apply(item).mod(MOD);
          item = item.divide(new BigInteger("3"));
          monkeys
            .get(monkey.getThrowDetermination().apply(item))
            .getItems()
            .add(item);
        }
        monkey.setItems(new ArrayList<>());
      });
    }
    log.info(
      "Part one solution: {}",
      monkeys
        .stream()
        .map(monkey -> monkey.getInspections())
        .sorted()
        .skip(monkeys.size() - 2)
        .reduce(1, (a, b) -> a * b)
    );

    // reset
    monkeys.forEach(monkey -> {
      monkey.setItems(monkey.getOriginalItems());
      monkey.setInspections(0);
    });

    for (int i = 0; i < 10000; i++) {
      monkeys.forEach(monkey -> {
        for (BigInteger item : monkey.getItems()) {
          monkey.inspect();
          item = monkey.getOperation().apply(item).mod(MOD);
          monkeys
            .get(monkey.getThrowDetermination().apply(item))
            .getItems()
            .add(item);
        }
        monkey.setItems(new ArrayList<>());
      });
    }

    log.info(
      "Part two solution: {}",
      monkeys
        .stream()
        .map(monkey -> monkey.getInspections())
        .sorted()
        .skip(monkeys.size() - 2)
        .map(t -> t.toString())
        .map(BigInteger::new)
        .reduce(BigInteger.ONE, (a, b) -> a.multiply(b))
    );
  }
}
