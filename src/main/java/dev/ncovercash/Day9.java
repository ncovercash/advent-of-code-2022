package dev.ncovercash;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day9 extends AbstractDay {

  @Data
  @AllArgsConstructor
  class Tuple {

    private int x;
    private int y;

    public Map.Entry<Integer, Integer> getEntry() {
      return new AbstractMap.SimpleEntry<>(this.getX(), this.getY());
    }
  }

  public Day9(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("9.txt");

    Set<Map.Entry<Integer, Integer>> firstTailPositions = new HashSet<>();
    Set<Map.Entry<Integer, Integer>> lastTailPositions = new HashSet<>();

    List<Tuple> knots = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      knots.add(new Tuple(0, 0));
    }

    firstTailPositions.add(knots.get(1).getEntry());
    lastTailPositions.add(knots.get(9).getEntry());

    for (String line : allInput) {
      char direction = line.charAt(0);
      int amount = Integer.parseInt(line.substring(2));

      for (int n = 0; n < amount; n++) {
        switch (direction) {
          case 'R':
            knots.get(0).setX(knots.get(0).getX() + 1);
            break;
          case 'U':
            knots.get(0).setY(knots.get(0).getY() + 1);
            break;
          case 'L':
            knots.get(0).setX(knots.get(0).getX() - 1);
            break;
          case 'D':
            knots.get(0).setY(knots.get(0).getY() - 1);
            break;
        }

        for (int i = 0; i < 9; i++) {
          if (
            Math.abs(knots.get(i).getX() - knots.get(i + 1).getX()) > 1 ||
            Math.abs(knots.get(i).getY() - knots.get(i + 1).getY()) > 1
          ) {
            knots
              .get(i + 1)
              .setX(
                knots.get(i + 1).getX() +
                Integer.signum(knots.get(i).getX() - knots.get(i + 1).getX())
              );
            knots
              .get(i + 1)
              .setY(
                knots.get(i + 1).getY() +
                Integer.signum(knots.get(i).getY() - knots.get(i + 1).getY())
              );
          }
        }

        firstTailPositions.add(knots.get(1).getEntry());
        lastTailPositions.add(knots.get(9).getEntry());
      }
    }

    log.info("Part one solution: {}", firstTailPositions.size());
    log.info("Part two solution: {}", lastTailPositions.size());
  }
}
