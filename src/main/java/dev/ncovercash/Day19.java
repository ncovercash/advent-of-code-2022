package dev.ncovercash;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day19 extends AbstractDay {

  @Data
  class Blueprint {

    final int id;

    final int oreRobotCostOre;
    final int clayRobotCostOre;
    final int obsidianRobotCostOre;
    final int obsidianRobotCostClay;
    final int geodeRobotCostOre;
    final int geodeRobotCostObsidian;
  }

  public int getGeodesity(
    int timeLeft,
    Blueprint bp,
    int numCollectingOre,
    int numCollectingClay,
    int numCollectingObs,
    int numCollectingGeode,
    int numOre,
    int numClay,
    int numObs
  ) {
    if (timeLeft == 0) {
      return 0;
    }
    if (timeLeft == 1) {
      return numCollectingGeode;
    }

    // if we can make a geode bot, always do it
    if (
      numOre >= bp.getGeodeRobotCostOre() &&
      numObs >= bp.getGeodeRobotCostObsidian()
    ) {
      return (
        numCollectingGeode +
        getGeodesity(
          timeLeft - 1,
          bp,
          numCollectingOre,
          numCollectingClay,
          numCollectingObs,
          numCollectingGeode + 1,
          numOre + numCollectingOre - bp.getGeodeRobotCostOre(),
          numClay + numCollectingClay,
          numObs + numCollectingObs - bp.getGeodeRobotCostObsidian()
        )
      );
    }

    int maxGeodes = getGeodesity(
      timeLeft - 1,
      bp,
      numCollectingOre,
      numCollectingClay,
      numCollectingObs,
      numCollectingGeode,
      numOre + numCollectingOre,
      numClay + numCollectingClay,
      numObs + numCollectingObs
    );

    if (numOre >= bp.getOreRobotCostOre()) {
      maxGeodes =
        Math.max(
          maxGeodes,
          getGeodesity(
            timeLeft - 1,
            bp,
            numCollectingOre + 1,
            numCollectingClay,
            numCollectingObs,
            numCollectingGeode,
            numOre + numCollectingOre - bp.getOreRobotCostOre(),
            numClay + numCollectingClay,
            numObs + numCollectingObs
          )
        );
    }

    if (numOre >= bp.getClayRobotCostOre()) {
      maxGeodes =
        Math.max(
          maxGeodes,
          getGeodesity(
            timeLeft - 1,
            bp,
            numCollectingOre,
            numCollectingClay + 1,
            numCollectingObs,
            numCollectingGeode,
            numOre + numCollectingOre,
            numClay + numCollectingClay - bp.getClayRobotCostOre(),
            numObs + numCollectingObs
          )
        );
    }

    if (
      numOre >= bp.getObsidianRobotCostOre() &&
      numClay >= bp.getObsidianRobotCostClay()
    ) {
      maxGeodes =
        Math.max(
          maxGeodes,
          getGeodesity(
            timeLeft - 1,
            bp,
            numCollectingOre,
            numCollectingClay,
            numCollectingObs + 1,
            numCollectingGeode,
            numOre + numCollectingOre - bp.getObsidianRobotCostOre(),
            numClay + numCollectingClay - bp.getObsidianRobotCostClay(),
            numObs + numCollectingObs
          )
        );
    }

    return numCollectingGeode + maxGeodes;
  }

  static Pattern BLUEPRINT_PATTERN = Pattern.compile(
    "Blueprint (\\d+): " +
    "Each ore robot costs (\\d+) ore. " +
    "Each clay robot costs (\\d+) ore. " +
    "Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
    "Each geode robot costs (\\d+) ore and (\\d+) obsidian."
  );

  public Day19(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    log.error("Failure.  This does not work.");
    List<String> allInput = InputUtils.getLines("19.txt");

    List<Blueprint> blueprints = allInput
      .stream()
      .map(BLUEPRINT_PATTERN::matcher)
      .map(m -> {
        m.find();
        return m;
      })
      .map(m ->
        new Blueprint(
          Integer.parseInt(m.group(1)),
          Integer.parseInt(m.group(2)),
          Integer.parseInt(m.group(3)),
          Integer.parseInt(m.group(4)),
          Integer.parseInt(m.group(5)),
          Integer.parseInt(m.group(6)),
          Integer.parseInt(m.group(7))
        )
      )
      .toList();

    log.info(
      "Bp 1: {}",
      getGeodesity(22, blueprints.get(0), 1, 0, 0, 2, 0, 0, 0)
    );
  }
}
