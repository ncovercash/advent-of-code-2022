package dev.ncovercash;

import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day1 extends AbstractDay {

  public Day1(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<List<String>> clumps = InputUtils.getLinesByClump("1.txt");

    List<Integer> aggregates = clumps
      .stream()
      .map(clump ->
        clump.stream().map(Integer::parseInt).reduce((a, b) -> a + b).orElse(0)
      )
      .sorted()
      .toList();

    log.info("Part one result {}", aggregates.get(aggregates.size() - 1));
    log.info(
      "Part two result {}",
      aggregates.get(aggregates.size() - 1) +
      aggregates.get(aggregates.size() - 2) +
      aggregates.get(aggregates.size() - 3)
    );
  }
}
