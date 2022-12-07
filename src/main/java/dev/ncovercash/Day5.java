package dev.ncovercash;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day5 extends AbstractDay {

  public Day5(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> input = InputUtils.getLines("5.txt");

    List<String> drawing = new ArrayList<>();
    List<String> instructions = new ArrayList<>();

    boolean isInstructions = false;
    for (String line : input) {
      if (line.isBlank()) {
        isInstructions = true;
      } else if (isInstructions) {
        instructions.add(line);
      } else {
        drawing.add(line);
      }
    }

    String drawingLabels = drawing.get(drawing.size() - 1);
    List<Stack<Character>> stacks = new ArrayList<>();
    for (int i = 0; i < drawingLabels.length(); i++) {
      if (Character.isDigit(drawingLabels.charAt(i))) {
        Stack<Character> stack = new Stack<>();

        // from bottom up
        for (int j = drawing.size() - 2; j >= 0; j--) {
          if (drawing.get(j).length() < i || drawing.get(j).charAt(i) == ' ') {
            break;
          } else {
            stack.push(drawing.get(j).charAt(i));
          }
        }

        stacks.add(stack);
      }
    }

    for (String instruction : instructions) {
      try (Scanner scan = new Scanner(instruction)) {
        scan.next();
        int num = scan.nextInt();
        scan.next();
        int from = scan.nextInt() - 1;
        scan.next();
        int to = scan.nextInt() - 1;

        for (int i = 0; i < num; i++) {
          stacks.get(to).push(stacks.get(from).pop());
        }
      }
    }

    log.info(
      "Part one answer: {}",
      stacks
        .stream()
        .map(Stack<Character>::pop)
        .map(s -> s.toString())
        .collect(Collectors.joining(""))
    );

    // part 2, restart
    stacks.removeIf(c -> true);
    for (int i = 0; i < drawingLabels.length(); i++) {
      if (Character.isDigit(drawingLabels.charAt(i))) {
        Stack<Character> stack = new Stack<>();

        // from bottom up
        for (int j = drawing.size() - 2; j >= 0; j--) {
          if (drawing.get(j).length() < i || drawing.get(j).charAt(i) == ' ') {
            break;
          } else {
            stack.push(drawing.get(j).charAt(i));
          }
        }

        stacks.add(stack);
      }
    }

    for (String instruction : instructions) {
      try (Scanner scan = new Scanner(instruction)) {
        scan.next();
        int num = scan.nextInt();
        scan.next();
        int from = scan.nextInt() - 1;
        scan.next();
        int to = scan.nextInt() - 1;

        Stack<Character> removed = new Stack<>();

        for (int i = 0; i < num; i++) {
          removed.push(stacks.get(from).pop());
        }
        while (!removed.empty()) {
          stacks.get(to).push(removed.pop());
        }
      }
    }

    log.info(
      "Part two answer: {}",
      stacks
        .stream()
        .map(Stack<Character>::pop)
        .map(s -> s.toString())
        .collect(Collectors.joining(""))
    );
  }
}
