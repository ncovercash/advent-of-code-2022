package dev.ncovercash;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day12 extends AbstractDay {

  public int[][] getDijkstraDistance(List<String> allInput, int[] start) {
    int[][] distance = new int[allInput.size()][allInput.get(0).length()];
    boolean[][] visited = new boolean[allInput.size()][allInput
      .get(0)
      .length()];

    for (int r = 0; r < distance.length; r++) {
      for (int c = 0; c < distance[r].length; c++) {
        distance[r][c] = Integer.MAX_VALUE;
        visited[r][c] = false;
      }
    }

    distance[start[0]][start[1]] = 0;

    int[] nextNode = new int[2];
    nextNode[0] = start[0];
    nextNode[1] = start[1];

    while (true) {
      visited[nextNode[0]][nextNode[1]] = true;

      List<int[]> neighbors = new ArrayList<>();

      // up
      if (nextNode[0] > 0) {
        neighbors.add(new int[] { nextNode[0] - 1, nextNode[1] });
      }
      // down
      if (nextNode[0] < distance.length - 1) {
        neighbors.add(new int[] { nextNode[0] + 1, nextNode[1] });
      }
      // left
      if (nextNode[1] > 0) {
        neighbors.add(new int[] { nextNode[0], nextNode[1] - 1 });
      }
      // right
      if (nextNode[1] < distance[0].length - 1) {
        neighbors.add(new int[] { nextNode[0], nextNode[1] + 1 });
      }

      for (int[] neighbor : neighbors) {
        if (
          allInput.get(nextNode[0]).charAt(nextNode[1]) -
          allInput.get(neighbor[0]).charAt(neighbor[1]) <=
          1 &&
          distance[nextNode[0]][nextNode[1]] +
          1 <=
          distance[neighbor[0]][neighbor[1]]
        ) {
          distance[neighbor[0]][neighbor[1]] =
            distance[nextNode[0]][nextNode[1]] + 1;
        }
      }

      nextNode[0] = -1;
      nextNode[1] = -1;

      for (int r = 0; r < distance.length; r++) {
        for (int c = 0; c < distance[r].length; c++) {
          if (
            !visited[r][c] &&
            (
              nextNode[0] == -1 ||
              distance[r][c] <= distance[nextNode[0]][nextNode[1]]
            )
          ) {
            nextNode[0] = r;
            nextNode[1] = c;
          }
        }
      }

      if (
        nextNode[0] == -1 ||
        distance[nextNode[0]][nextNode[1]] == Integer.MAX_VALUE
      ) {
        break;
      }
    }

    return distance;
  }

  public Day12(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("12.txt");

    int[] start = new int[2];
    int[] destination = new int[2];

    List<int[]> potentialStarts = new ArrayList<>();

    for (int r = 0; r < allInput.size(); r++) {
      for (int c = 0; c < allInput.get(0).length(); c++) {
        if (allInput.get(r).charAt(c) == 'S') {
          allInput.set(r, allInput.get(r).replace('S', 'a'));
          start[0] = r;
          start[1] = c;
        }
        if (allInput.get(r).charAt(c) == 'E') {
          allInput.set(r, allInput.get(r).replace('E', 'z'));
          destination[0] = r;
          destination[1] = c;
        }
        if (allInput.get(r).charAt(c) == 'a') {
          potentialStarts.add(new int[] { r, c });
        }
      }
    }

    int[][] distance = getDijkstraDistance(allInput, destination);

    log.info("Part one solution: {}", distance[start[0]][start[1]]);
    for (int r = 0; r < allInput.size(); r++) {
      for (int c = 0; c < allInput.get(0).length(); c++) {
        if (
          allInput.get(r).charAt(c) == 'a' &&
          distance[r][c] != Integer.MAX_VALUE
        ) {
          log.info(distance[r][c]);
        }
      }
    }
    log.info(
      "Part two solution: {}",
      potentialStarts
        .stream()
        .map(st -> distance[st[0]][st[1]])
        .min((a, b) -> a - b)
    );
  }
}
