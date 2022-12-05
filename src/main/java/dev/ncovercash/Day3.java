package dev.ncovercash;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day3 extends AbstractDay {

  public Day3(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> sacks = InputUtils.getLines("3.txt");

    int result = sacks
      .stream()
      .map((String sack) -> {
        Set<Character> leftHalf = new TreeSet<>(
          InputUtils.stringToCharacterList(sack.substring(0, sack.length() / 2))
        );
        Set<Character> rightHalf = new TreeSet<>(
          InputUtils.stringToCharacterList(sack.substring(sack.length() / 2))
        );

        char overlap = leftHalf
          .stream()
          .filter(rightHalf::contains)
          .findFirst()
          .orElse(' ');

        return letterScore(overlap);
      })
      .reduce((a, b) -> a + b)
      .orElse(0);

    log.info("Part one result: {}", result);

    int partTwo = 0;

    for (int i = 0; i < sacks.size(); i += 3) {
      Set<Character> elf1 = new TreeSet<>(
        InputUtils.stringToCharacterList(sacks.get(i))
      );
      Set<Character> elf2 = new TreeSet<>(
        InputUtils.stringToCharacterList(sacks.get(i + 1))
      );
      Set<Character> elf3 = new TreeSet<>(
        InputUtils.stringToCharacterList(sacks.get(i + 2))
      );

      char common = elf1
        .stream()
        .filter(elf2::contains)
        .filter(elf3::contains)
        .findFirst()
        .orElse(' ');

      partTwo += letterScore(common);
    }

    log.info("Part two result: {}", partTwo);
  }

  protected int letterScore(char overlap) {
    if (Character.isUpperCase(overlap)) {
      return overlap - 'A' + 27;
    } else {
      return overlap - 'a' + 1;
    }
  }
}
