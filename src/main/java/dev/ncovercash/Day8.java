package dev.ncovercash;

import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day8 extends AbstractDay {

  public Day8(String[] args) {
    super(args);
  }

  public boolean isVisibleFromWest(int r, int c, List<List<Integer>> heights) {
    int target = heights.get(r).get(c);
    for (int i = 0; i < c; i++) {
      if (heights.get(r).get(i) >= target) {
        return false;
      }
    }
    return true;
  }

  public boolean isVisibleFromEast(int r, int c, List<List<Integer>> heights) {
    int target = heights.get(r).get(c);
    for (int i = heights.get(r).size() - 1; i > c; i--) {
      if (heights.get(r).get(i) >= target) {
        return false;
      }
    }
    return true;
  }

  public boolean isVisibleFromNorth(int r, int c, List<List<Integer>> heights) {
    int target = heights.get(r).get(c);
    for (int i = 0; i < r; i++) {
      if (heights.get(i).get(c) >= target) {
        return false;
      }
    }
    return true;
  }

  public boolean isVisibleFromSouth(int r, int c, List<List<Integer>> heights) {
    int target = heights.get(r).get(c);
    for (int i = heights.size() - 1; i > r; i--) {
      if (heights.get(i).get(c) >= target) {
        return false;
      }
    }
    return true;
  }

  public int numVisibleToWest(int r, int c, List<List<Integer>> heights) {
    int n = 0;
    for (int i = c - 1; i >= 0; i--) {
      n++;
      if (heights.get(r).get(i) >= heights.get(r).get(c)) {
        break;
      }
    }
    return n;
  }

  public int numVisibleToEast(int r, int c, List<List<Integer>> heights) {
    int n = 0;
    for (int i = c + 1; i < heights.get(r).size(); i++) {
      n++;
      if (heights.get(r).get(i) >= heights.get(r).get(c)) {
        break;
      }
    }
    return n;
  }

  public int numVisibleToNorth(int r, int c, List<List<Integer>> heights) {
    int n = 0;
    for (int i = r - 1; i >= 0; i--) {
      n++;
      if (heights.get(i).get(c) >= heights.get(r).get(c)) {
        break;
      }
    }
    return n;
  }

  public int numVisibleToSouth(int r, int c, List<List<Integer>> heights) {
    int n = 0;
    for (int i = r + 1; i < heights.size(); i++) {
      n++;
      if (heights.get(i).get(c) >= heights.get(r).get(c)) {
        break;
      }
    }
    return n;
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("8.txt");

    List<List<Integer>> heights = allInput
      .stream()
      .map(l -> l.chars().mapToObj(i -> i - '0').toList())
      .toList();

    int numVisible = 0;
    int biggestVisiblity = 0;

    for (int r = 0; r < heights.size(); r++) {
      for (int c = 0; c < heights.get(r).size(); c++) {
        if (
          isVisibleFromWest(r, c, heights) ||
          isVisibleFromNorth(r, c, heights) ||
          isVisibleFromEast(r, c, heights) ||
          isVisibleFromSouth(r, c, heights)
        ) {
          numVisible++;
        }

        biggestVisiblity =
          Math.max(
            biggestVisiblity,
            numVisibleToWest(r, c, heights) *
            numVisibleToNorth(r, c, heights) *
            numVisibleToEast(r, c, heights) *
            numVisibleToSouth(r, c, heights)
          );
      }
    }

    log.info("Part one: {}", numVisible);
    log.info("Part two: {}", biggestVisiblity);
  }
}
