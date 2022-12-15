package dev.ncovercash;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day15 extends AbstractDay {

  @Data
  class Sensor {

    final int sensorX;
    final int sensorY;
    final int beaconX;
    final int beaconY;

    public Range getKnownEmptyXCoordinates(int y) {
      return getKnownEmptyXCoordinates(y, false);
    }

    public Range getKnownEmptyXCoordinates(int y, boolean includeBeacon) {
      int maxDistance =
        Math.abs(sensorX - beaconX) +
        Math.abs(sensorY - beaconY) -
        Math.abs(y - sensorY);

      if (maxDistance < 0) {
        return new Range(-1, -1);
      }

      int lower = sensorX - maxDistance;
      int upper = sensorX + maxDistance;

      if (!includeBeacon && y == beaconY) {
        if (lower == upper) {
          // only beacon, so empty range
          return new Range(-1, -1);
        }

        // must be on one end of range
        if (lower == beaconX) {
          lower++;
        } else {
          upper--;
        }
      }

      return new Range(lower, upper);
    }
  }

  public Day15(String[] args) {
    super(args);
  }

  private static final Pattern LINE_PATTERN = Pattern.compile(
    "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)"
  );

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("15.txt");

    List<Sensor> sensors = new ArrayList<>();

    for (String line : allInput) {
      Matcher groups = LINE_PATTERN.matcher(line);
      groups.find();

      sensors.add(
        new Sensor(
          Integer.parseInt(groups.group(1)),
          Integer.parseInt(groups.group(2)),
          Integer.parseInt(groups.group(3)),
          Integer.parseInt(groups.group(4))
        )
      );
    }

    List<Range> rangesAt2000000 = Range.compact(
      sensors.stream().map(s -> s.getKnownEmptyXCoordinates(10)).toList()
    );

    log.info(
      "Part one solution: {}",
      rangesAt2000000.stream().map(Range::size).reduce(0, (a, b) -> a + b)
    );

    for (int y = 0; y <= 4000000; y++) {
      // make stream happy
      final int _y = y;
      List<Range> occupied = sensors
        .stream()
        .map(s -> s.getKnownEmptyXCoordinates(_y, true))
        .toList();
      occupied =
        Range
          .compact(occupied)
          .stream()
          .map(r -> r.truncate(0, 4000000))
          .filter(r -> !r.isEmpty())
          .toList();

      if (occupied.size() > 1 || occupied.get(0).size() != 4000001) {
        int missingX;
        if (occupied.size() == 1) {
          if (occupied.get(0).getMin() > 0) {
            missingX = 0;
          } else {
            missingX = 4000000;
          }
        } else {
          missingX = occupied.get(0).getMax() + 1;
        }

        log.info(
          "Part 2 found at ({},{}), answer: {}",
          missingX,
          y,
          // exceeds int bounds
          (long) missingX * 4000000 + y
        );
        break;
      }
    }
  }
}
