package dev.ncovercash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day7 extends AbstractDay {

  class File {

    public File parent;

    public String name;
    public int size;

    public boolean isDir;
    public Map<String, File> contents;

    public File(String name, int size, File parent) {
      this.parent = parent;
      this.name = name;
      this.size = size;
      this.isDir = false;
    }

    public File(String name, File parent) {
      this.parent = parent;
      this.name = name;
      this.size = -1;
      this.isDir = true;
      this.contents = new HashMap<>();
    }

    public int getSize() {
      if (!this.isDir) {
        return this.size;
      }

      if (this.size == -1) {
        this.size =
          contents
            .entrySet()
            .stream()
            .map(e -> e.getValue().getSize())
            .reduce(0, (a, b) -> a + b);
      }

      return this.size;
    }

    public void print(int indent) {
      if (this.isDir) {
        log.info(
          String.format(
            "%" + indent + "s- %s (dir, size=%d)",
            " ",
            this.name,
            this.getSize()
          )
        );
        this.contents.entrySet()
          .stream()
          .map(Entry::getValue)
          .forEach(e -> e.print(indent + 2));
      } else {
        log.info(
          String.format(
            "%" + indent + "s- %s (file, size=%d)",
            " ",
            this.name,
            this.size
          )
        );
      }
    }

    public int getNumUnder100000() {
      if (!this.isDir) {
        return 0;
      }

      return (
        (this.getSize() <= 100000 ? this.getSize() : 0) +
        contents
          .entrySet()
          .stream()
          .map(e -> e.getValue().getNumUnder100000())
          .reduce(0, (a, b) -> a + b)
      );
    }

    public int getSmallestOfAtLeast(int neededSpace) {
      Optional<Integer> minFile =
        this.contents.entrySet()
          .stream()
          .map(Entry::getValue)
          .filter(f -> f.isDir)
          .filter(f -> f.getSize() >= neededSpace)
          .map(f -> f.getSmallestOfAtLeast(neededSpace))
          .min(Integer::compare);

      return minFile.orElse(this.getSize());
    }
  }

  public Day7(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("7.txt");

    File root = new File("/", null);
    File curDir = root;

    for (String line : allInput) {
      if (line.startsWith("$ cd")) {
        String dir = line.substring(5);
        if ("/".equals(dir)) {
          curDir = root;
        } else if ("..".equals(dir)) {
          curDir = curDir.parent;
        } else {
          for (Entry<String, File> cur : curDir.contents.entrySet()) {
            if (cur.getValue().name.equals(dir)) {
              curDir = cur.getValue();
            }
          }
        }
      } else if (line.startsWith("$ ls")) {
        // we don't need to do anything, since we're listing the files in the curDir
        // and any non-$ lines are treated as such
      } else {
        if (line.startsWith("dir ")) {
          String name = line.substring(4);
          curDir.contents.put(name, new File(name, curDir));
        } else {
          // extract size and name
          try (Scanner lineScan = new Scanner(line)) {
            int size = lineScan.nextInt();
            String name = lineScan.nextLine().trim();
            curDir.contents.put(name, new File(name, size, curDir));
          }
        }
      }
    }

    int neededSpace = root.getSize() - 40000000;

    root.print(1);

    log.info("Part one solution: {}", root.getNumUnder100000());
    log.info("Part two solution: {}", root.getSmallestOfAtLeast(neededSpace));
  }
}
