package dev.ncovercash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day14 extends AbstractDay {

  public void markCavePoint(
    Map<Integer, Map<Integer, Boolean>> cave,
    int x,
    int y
  ) {
    cave.computeIfAbsent(x, newX -> new HashMap<>()).put(y, true);
  }

  public boolean caveOpenAt(
    Map<Integer, Map<Integer, Boolean>> cave,
    int x,
    int y
  ) {
    return !cave
      .computeIfAbsent(x, newX -> new HashMap<>())
      .getOrDefault(y, false);
  }

  public Day14(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("14.txt");

    int maxY = Integer.MIN_VALUE;

    Map<Integer, Map<Integer, Boolean>> cave = new HashMap<>();

    for (int i = 0; i < allInput.size(); i++) {
      try (Scanner lineScanner = new Scanner(allInput.get(i))) {
        lineScanner.useDelimiter(" -> ");

        String initialPoint = lineScanner.next();
        int initialX = Integer.parseInt(initialPoint.split(",")[0]);
        int initialY = Integer.parseInt(initialPoint.split(",")[1]);

        while (lineScanner.hasNext()) {
          String nextPoint = lineScanner.next();
          int nextX = Integer.parseInt(nextPoint.split(",")[0]);
          int nextY = Integer.parseInt(nextPoint.split(",")[1]);

          for (
            int x = Math.min(initialX, nextX);
            x <= Math.max(initialX, nextX);
            x++
          ) {
            for (
              int y = Math.min(initialY, nextY);
              y <= Math.max(initialY, nextY);
              y++
            ) {
              maxY = Math.max(maxY, y);

              markCavePoint(cave, x, y);
            }
          }

          initialX = nextX;
          initialY = nextY;
        }
      }
    }

    int numSand = 0;
    // drop sand
    while (true) {
      int sandX = 500;
      int sandY = 0;

      while (true) {
        if (sandY > maxY) {
          break;
        }
        if (caveOpenAt(cave, sandX, sandY + 1)) {
          sandY++;
        } else if (caveOpenAt(cave, sandX - 1, sandY + 1)) {
          sandX--;
          sandY++;
        } else if (caveOpenAt(cave, sandX + 1, sandY + 1)) {
          sandX++;
          sandY++;
        } else {
          break;
        }
      }
      if (sandY > maxY) {
        break;
      }
      markCavePoint(cave, sandX, sandY);
      numSand++;
    }

    log.info("Part one solution: {}", numSand);

    int floorY = maxY + 2;
    while (true) {
      int sandX = 500;
      int sandY = 0;

      while (true) {
        if (sandY == floorY - 1) {
          break;
        }
        if (caveOpenAt(cave, sandX, sandY + 1)) {
          sandY++;
        } else if (caveOpenAt(cave, sandX - 1, sandY + 1)) {
          sandX--;
          sandY++;
        } else if (caveOpenAt(cave, sandX + 1, sandY + 1)) {
          sandX++;
          sandY++;
        } else {
          break;
        }
      }
      markCavePoint(cave, sandX, sandY);
      numSand++;
      if (sandX == 500 && sandY == 0) {
        break;
      }
    }
    log.info("Part two solution: {}", numSand);
  }
}
