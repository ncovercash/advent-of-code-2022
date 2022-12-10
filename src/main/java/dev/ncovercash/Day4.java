package dev.ncovercash;

import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day4 extends AbstractDay {

  public Day4(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> assignments = InputUtils.getLines("4.txt");

    List<List<Integer>> sectionAssignments = assignments
      .stream()
      .map(s -> s.split(","))
      .map(r ->
        Arrays
          .stream(r)
          .flatMap(f -> Arrays.stream(f.split("-")))
          .map(Integer::parseInt)
          .toList()
      )
      .toList();

    long numFullOverlaps = sectionAssignments
      .stream()
      .filter(a ->
        (a.get(0) <= a.get(2) && a.get(1) >= a.get(3)) ||
        (a.get(2) <= a.get(0) && a.get(3) >= a.get(1))
      )
      .count();

    log.info("Part one solution: {}", numFullOverlaps);

    long numTotalOverlaps = sectionAssignments
      .stream()
      .filter(a -> Math.max(a.get(0), a.get(2)) <= Math.min(a.get(1), a.get(3)))
      .count();

    log.info("Part two solution: {}", numTotalOverlaps);
  }
}
