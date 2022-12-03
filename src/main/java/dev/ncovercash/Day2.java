package dev.ncovercash;

import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day2 extends AbstractDay {

  public Day2(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> matches = InputUtils.getLines("2.txt");

    int score = matches
      .stream()
      .map(this::getMatchScoreOne)
      .reduce((a, b) -> a + b)
      .orElse(0);

    log.info("Total score (part one): {}", score);

    int score2 = matches
      .stream()
      .map(this::getMatchScoreTwo)
      .reduce((a, b) -> a + b)
      .orElse(0);

    log.info("Total score (part two): {}", score2);
  }

  protected int getMatchScoreOne(String match) {
    int myChoiceScore = match.charAt(2) - 'W';

    switch (match) {
      case "A X":
      case "B Y":
      case "C Z":
        return myChoiceScore + 3; // draw
      case "A Y":
      case "B Z":
      case "C X":
        return myChoiceScore + 6; // win
      case "A Z":
      case "B X":
      case "C Y":
      default:
        return myChoiceScore + 0; // loss
    }
  }

  protected int getMatchScoreTwo(String match) {
    switch (match) {
      case "A X":
        return 3 + 0;
      case "A Y":
        return 1 + 3;
      case "A Z":
        return 2 + 6;
      case "B X":
        return 1 + 0;
      case "B Y":
        return 2 + 3;
      case "B Z":
        return 3 + 6;
      case "C X":
        return 2 + 0;
      case "C Y":
        return 3 + 3;
      case "C Z":
      default:
        return 1 + 6;
    }
  }
}
