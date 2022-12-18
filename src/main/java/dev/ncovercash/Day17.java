package dev.ncovercash;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day17 extends AbstractDay {

  enum Direction {
    LEFT,
    RIGHT,
  }

  @AllArgsConstructor
  class Point {

    int x;
    int y;

    public Point down() {
      return new Point(x, y - 1);
    }

    public Point left() {
      return new Point(x - 1, y);
    }

    public Point right() {
      return new Point(x + 1, y);
    }

    public Point shiftY(int delta) {
      return new Point(x, y + delta);
    }

    @Override
    public int hashCode() {
      return y * 7 + x;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj != null && obj.getClass() == this.getClass()) {
        Point other = (Point) obj;
        return this.x == other.x && this.y == other.y;
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("(%d,%d)", x, y);
    }
  }

  public void print(List<boolean[]> tower) {
    log.info("|||||||");
    for (int i = tower.size() - 1; i >= 0; i--) {
      StringWriter sw = new StringWriter();
      for (boolean b : tower.get(i)) {
        if (b) {
          sw.append('#');
        } else {
          sw.append('.');
        }
      }
      log.info(sw.toString());
    }
    log.info("-------");
  }

  public boolean isOk(List<boolean[]> tower, Point p) {
    if (p.y < 0 || p.x < 0 || p.x >= 7) {
      return false;
    }
    if (p.y < tower.size()) {
      return !tower.get(p.y)[p.x];
    }
    return true;
  }

  public void incorporate(List<Point> rock, List<boolean[]> rows) {
    for (Point p : rock) {
      while (p.y >= rows.size()) {
        rows.add(
          new boolean[] { false, false, false, false, false, false, false }
        );
      }

      rows.get(p.y)[p.x] = true;
    }
  }

  public boolean[][] copyRowsForSerialization(List<boolean[]> rows) {
    boolean[][] result = new boolean[rows.size()][7];
    for (int i = 0; i < rows.size(); i++) {
      result[i] = Arrays.copyOf(rows.get(i), 7);
    }
    return result;
  }

  public boolean sameTower(boolean[][] x, boolean[][] y) {
    if (x.length != y.length) {
      return false;
    }

    for (int i = 0; i < x.length; i++) {
      for (int j = 0; j < 7; j++) {
        if (x[i][j] != y[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  public int shrink(List<boolean[]> rows) {
    int bottomNeeded = Integer.MAX_VALUE;
    for (int x = 0; x < 7; x++) {
      for (int y = rows.size() - 1; y >= 0; y--) {
        if (rows.get(y)[x]) {
          bottomNeeded = Math.min(y, bottomNeeded);
          break;
        }
      }
    }

    if (bottomNeeded == Integer.MAX_VALUE) {
      return 0;
    }

    for (int i = 0; i < bottomNeeded; i++) {
      rows.remove(0);
    }

    return bottomNeeded;
  }

  public List<Long> checkCycle(
    List<boolean[]> rows,
    Map<Long, Map<boolean[][], long[]>> cycleCache,
    int jetI,
    long rockI,
    long totalHeight
  ) {
    boolean[][] tower = copyRowsForSerialization(rows);
    long key = jetI * 5 + (rockI % 5);

    for (Map.Entry<boolean[][], long[]> known : cycleCache
      .computeIfAbsent(key, k -> new HashMap<>())
      .entrySet()) {
      if (sameTower(tower, known.getKey())) {
        log.info(
          "CYCLE FOUND!! From i={},h={} to i={},h={}",
          known.getValue()[0],
          known.getValue()[1],
          rockI,
          totalHeight
        );
        return Arrays.asList(
          rockI - known.getValue()[0],
          totalHeight - known.getValue()[1]
        );
      }
    }

    cycleCache
      .computeIfAbsent(key, k -> new HashMap<>())
      .put(tower, new long[] { rockI, totalHeight });

    return null;
  }

  public Day17(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("17-test.txt");

    List<Direction> jets = allInput
      .get(0)
      .chars()
      .mapToObj(c -> c == '<' ? Direction.LEFT : Direction.RIGHT)
      .toList();
    int jetI = 0;

    List<List<Point>> rocks = Arrays.asList(
      Arrays.asList(
        new Point(2, 0),
        new Point(3, 0),
        new Point(4, 0),
        new Point(5, 0)
      ),
      Arrays.asList(
        new Point(3, 0),
        new Point(2, 1),
        new Point(3, 1),
        new Point(4, 1),
        new Point(3, 2)
      ),
      Arrays.asList(
        new Point(2, 0),
        new Point(3, 0),
        new Point(4, 0),
        new Point(4, 1),
        new Point(4, 2)
      ),
      Arrays.asList(
        new Point(2, 0),
        new Point(2, 1),
        new Point(2, 2),
        new Point(2, 3)
      ),
      Arrays.asList(
        new Point(2, 0),
        new Point(3, 0),
        new Point(2, 1),
        new Point(3, 1)
      )
    );

    List<boolean[]> rows = new ArrayList<>();
    long shrunkRows = 0;

    Map<Long, Map<boolean[][], long[]>> cycleCache = new HashMap<>();
    List<Long> cycle = null;

    for (int i = 0; i < 2022; i++) {
      List<Point> rock = rocks
        .get(i % 5)
        .stream()
        .map(p -> p.shiftY(rows.size() + 3))
        .toList();

      while (true) {
        if (jets.get(jetI % jets.size()) == Direction.LEFT) {
          if (rock.stream().map(Point::left).allMatch(p -> isOk(rows, p))) {
            rock = rock.stream().map(Point::left).toList();
          }
        } else {
          if (rock.stream().map(Point::right).allMatch(p -> isOk(rows, p))) {
            rock = rock.stream().map(Point::right).toList();
          }
        }

        jetI = (jetI + 1) % jets.size();

        if (rock.stream().map(Point::down).allMatch(p -> isOk(rows, p))) {
          rock = rock.stream().map(Point::down).toList();
        } else {
          break;
        }
      }

      incorporate(rock, rows);
      if (cycle == null) {
        cycle = checkCycle(rows, cycleCache, jetI, i, shrunkRows + rows.size());
      }
      shrunkRows += shrink(rows);
    }

    log.info("Part one solution: {}", shrunkRows + rows.size());

    for (long i = 2022L; i < 1000000000000L; i++) {
      List<Point> rock = rocks
        .get((int) (i % 5))
        .stream()
        .map(p -> p.shiftY(rows.size() + 3))
        .toList();

      while (true) {
        if (jets.get(jetI % jets.size()) == Direction.LEFT) {
          if (rock.stream().map(Point::left).allMatch(p -> isOk(rows, p))) {
            rock = rock.stream().map(Point::left).toList();
          }
        } else {
          if (rock.stream().map(Point::right).allMatch(p -> isOk(rows, p))) {
            rock = rock.stream().map(Point::right).toList();
          }
        }

        jetI = (jetI + 1) % jets.size();

        if (rock.stream().map(Point::down).allMatch(p -> isOk(rows, p))) {
          rock = rock.stream().map(Point::down).toList();
        } else {
          break;
        }
      }

      incorporate(rock, rows);
      if (cycle == null) {
        cycle = checkCycle(rows, cycleCache, jetI, i, shrunkRows + rows.size());
      }
      if (cycle != null) {
        while (i + cycle.get(0) < 1000000000000L) {
          i += cycle.get(0);
          shrunkRows += cycle.get(1);
        }
      }
      shrunkRows += shrink(rows);
    }

    log.info("Part two solution: {}", shrunkRows + rows.size());
  }
}
