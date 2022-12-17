package dev.ncovercash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day16 extends AbstractDay {

  @Data
  @RequiredArgsConstructor
  class Valve {

    final int flowRate;

    @Singular
    final List<Integer> tunnels;

    public Valve(Valve source) {
      this(source.flowRate, source.tunnels);
    }
  }

  public static int[] dijkstra(int valveIndex, List<Valve> valves) {
    int[] distances = new int[valves.size()];
    for (int i = 0; i < distances.length; i++) {
      distances[i] = Integer.MAX_VALUE;
    }

    distances[valveIndex] = 0;
    Set<Integer> visited = new HashSet<>();

    while (visited.size() < distances.length) {
      int nextI = -1;
      for (int i = 0; i < distances.length; i++) {
        if (
          !visited.contains(i) &&
          (nextI == -1 || distances[i] < distances[nextI])
        ) {
          nextI = i;
        }
      }

      visited.add(nextI);

      for (int sibling : valves.get(nextI).getTunnels()) {
        distances[sibling] = Math.min(distances[sibling], distances[nextI] + 1);
      }
    }

    return distances;
  }

  private int getMaxFlowRate(
    int valveIndex,
    List<Valve> valves,
    int[][] distances,
    boolean[] open,
    int timeRemaining
  ) {
    int bestFlow = 0;

    if (timeRemaining <= 1) {
      return bestFlow;
    }

    // should only be false for AA, since that's the only time we'd travel
    // to a pointless valve
    if (!open[valveIndex] && valves.get(valveIndex).getFlowRate() != 0) {
      int thisFlow = valves.get(valveIndex).getFlowRate() * (timeRemaining - 1);
      bestFlow = thisFlow;

      boolean[] openWithThis = Arrays.copyOf(open, open.length);
      openWithThis[valveIndex] = true;

      for (int destI = 0; destI < distances[valveIndex].length; destI++) {
        if (
          !openWithThis[destI] &&
          timeRemaining - 1 - distances[valveIndex][destI] >= 0
        ) {
          bestFlow =
            Math.max(
              bestFlow,
              thisFlow +
              getMaxFlowRate(
                destI,
                valves,
                distances,
                openWithThis,
                timeRemaining - 1 - distances[valveIndex][destI]
              )
            );
        }
      }
    } else {
      for (int destI = 0; destI < distances[valveIndex].length; destI++) {
        if (
          timeRemaining - 1 - distances[valveIndex][destI] >= 0 && !open[destI]
        ) {
          bestFlow =
            Math.max(
              bestFlow,
              getMaxFlowRate(
                destI,
                valves,
                distances,
                open,
                timeRemaining - distances[valveIndex][destI]
              )
            );
        }
      }
    }

    return bestFlow;
  }

  @AllArgsConstructor
  class SplitList {

    List<Integer> a;
    List<Integer> b;
  }

  public int[][] copy2DArray(int[][] src) {
    int[][] dest = new int[src.length][src[0].length];

    for (int i = 0; i < src.length; i++) {
      dest[i] = Arrays.copyOf(src[i], src[i].length);
    }

    return dest;
  }

  public static List<Set<Integer>> getAllSubsets(int[] source) {
    List<Set<Integer>> subsets = new ArrayList<>();
    subsets.add(new HashSet<>());

    for (int val : source) {
      List<Set<Integer>> newSubsets = new ArrayList<>();

      for (Set<Integer> subset : subsets) {
        newSubsets.add(subset);
        Set<Integer> addedSet = new HashSet<>(subset);
        addedSet.add(val);
        newSubsets.add(addedSet);
      }

      subsets = newSubsets;
    }

    return subsets;
  }

  public Day16(String[] args) {
    super(args);
  }

  private static final Pattern LINE_PATTERN = Pattern.compile(
    "Valve (\\S+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)"
  );

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("16.txt");

    Map<String, Integer> nameMap = new HashMap<>();
    for (int i = 0; i < allInput.size(); i++) {
      Matcher groups = LINE_PATTERN.matcher(allInput.get(i));
      groups.find();

      nameMap.put(groups.group(1), i);
    }

    List<Valve> valves = new ArrayList<>();
    for (String line : allInput) {
      Matcher groups = LINE_PATTERN.matcher(line);
      groups.find();

      valves.add(
        new Valve(
          Integer.parseInt(groups.group(2)),
          Arrays.stream(groups.group(3).split(", ")).map(nameMap::get).toList()
        )
      );
    }

    int[][] distances = new int[valves.size()][valves.size()];
    for (int i = 0; i < distances.length; i++) {
      distances[i] = dijkstra(i, valves);
      for (int j = 0; j < distances[i].length; j++) {
        if (valves.get(j).getFlowRate() == 0) {
          distances[i][j] = Integer.MAX_VALUE;
        }
      }
    }

    boolean[] initialOpen = new boolean[valves.size()];

    log.info(
      "Part one solution: {}",
      getMaxFlowRate(nameMap.get("AA"), valves, distances, initialOpen, 30)
    );

    List<Set<Integer>> subsets = getAllSubsets(
      IntStream
        .range(0, valves.size())
        .filter(i -> valves.get(i).getFlowRate() != 0)
        .toArray()
    );

    log.info(
      "Part two solution: {}",
      subsets
        .stream()
        .map(subset -> {
          int[][] humanDistances = copy2DArray(distances);
          int[][] elephantDistances = copy2DArray(distances);

          for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[i].length; j++) {
              if (subset.contains(j)) {
                elephantDistances[i][j] = Integer.MAX_VALUE;
              } else {
                humanDistances[i][j] = Integer.MAX_VALUE;
              }
            }
          }

          return (
            getMaxFlowRate(
              nameMap.get("AA"),
              valves,
              humanDistances,
              initialOpen,
              26
            ) +
            getMaxFlowRate(
              nameMap.get("AA"),
              valves,
              elephantDistances,
              initialOpen,
              26
            )
          );
        })
        .max(Integer::compare)
        .get()
    );
  }
}
