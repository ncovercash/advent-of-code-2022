package dev.ncovercash;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day18 extends AbstractDay {

  private static List<int[]> getAdjacent(List<Integer> coordinates) {
    return Arrays.asList(
      new int[] {
        coordinates.get(0) + 1,
        coordinates.get(1),
        coordinates.get(2),
      },
      new int[] {
        coordinates.get(0) - 1,
        coordinates.get(1),
        coordinates.get(2),
      },
      new int[] {
        coordinates.get(0),
        coordinates.get(1) + 1,
        coordinates.get(2),
      },
      new int[] {
        coordinates.get(0),
        coordinates.get(1) - 1,
        coordinates.get(2),
      },
      new int[] {
        coordinates.get(0),
        coordinates.get(1),
        coordinates.get(2) + 1,
      },
      new int[] {
        coordinates.get(0),
        coordinates.get(1),
        coordinates.get(2) - 1,
      }
    );
  }

  public String formatPoint(int[] point) {
    return String.format("%d,%d,%d", point[0], point[1], point[2]);
  }

  public String formatPoint(List<Integer> point) {
    return String.format("%d,%d,%d", point.get(0), point.get(1), point.get(2));
  }

  public List<Integer> parsePoint(String point) {
    return Arrays.stream(point.split(",")).map(Integer::parseInt).toList();
  }

  public void floodFill(
    List<Integer> initialPoint,
    Set<String> points,
    Set<String> floodPoints,
    int minX,
    int minY,
    int minZ,
    int maxX,
    int maxY,
    int maxZ
  ) {
    TreeSet<String> toVisit = new TreeSet<>();
    toVisit.add(formatPoint(initialPoint));

    while (!toVisit.isEmpty()) {
      String point = toVisit.first();
      toVisit.remove(point);

      floodPoints.add(point);
      List<String> adjacent = getAdjacent(parsePoint(point))
        .stream()
        .filter(p -> p[0] >= minX - 1)
        .filter(p -> p[1] >= minY - 1)
        .filter(p -> p[2] >= minZ - 1)
        .filter(p -> p[0] <= maxX + 1)
        .filter(p -> p[1] <= maxY + 1)
        .filter(p -> p[2] <= maxZ + 1)
        .map(p -> formatPoint(p))
        .filter(p -> !toVisit.contains(p))
        .filter(p -> !points.contains(p))
        .filter(p -> !floodPoints.contains(p))
        .toList();
      for (String adj : adjacent) {
        toVisit.add(adj);
      }
    }
  }

  public Day18(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("18.txt");

    Set<String> droplets = new HashSet<>(allInput);
    List<List<Integer>> dropletCoordinates = allInput
      .stream()
      .map(this::parsePoint)
      .toList();

    log.info(
      "Part one solution: {}",
      dropletCoordinates
        .stream()
        .flatMap(coordinates -> getAdjacent(coordinates).stream())
        .map(this::formatPoint)
        .filter(adj -> !droplets.contains(adj))
        .count()
    );

    int minX = dropletCoordinates
      .stream()
      .map(d -> d.get(0))
      .min(Integer::compare)
      .orElse(0);
    int minY = dropletCoordinates
      .stream()
      .map(d -> d.get(1))
      .min(Integer::compare)
      .orElse(0);
    int minZ = dropletCoordinates
      .stream()
      .map(d -> d.get(2))
      .min(Integer::compare)
      .orElse(0);
    int maxX = dropletCoordinates
      .stream()
      .map(d -> d.get(0))
      .max(Integer::compare)
      .orElse(0);
    int maxY = dropletCoordinates
      .stream()
      .map(d -> d.get(1))
      .max(Integer::compare)
      .orElse(0);
    int maxZ = dropletCoordinates
      .stream()
      .map(d -> d.get(2))
      .max(Integer::compare)
      .orElse(0);

    Set<String> flood = new HashSet<>();
    floodFill(
      Arrays.asList(minX - 1, minY - 1, minZ - 1),
      droplets,
      flood,
      minX,
      minY,
      minZ,
      maxX,
      maxY,
      maxZ
    );
    floodFill(
      Arrays.asList(maxX + 1, maxY + 1, maxZ + 1),
      droplets,
      flood,
      minX,
      minY,
      minZ,
      maxX,
      maxY,
      maxZ
    );

    log.info(
      "Part two solution: {}",
      dropletCoordinates
        .stream()
        .flatMap(coordinates -> getAdjacent(coordinates).stream())
        .map(this::formatPoint)
        .filter(flood::contains)
        .count()
    );
  }
}
