package dev.ncovercash;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day13 extends AbstractDay {

  @AllArgsConstructor
  class Packet implements Comparable<Packet> {

    int val = -1;
    List<Packet> list = null;

    public Packet(int val) {
      this.val = val;
    }

    public Packet(List<Packet> list) {
      this.list = list;
    }

    public Packet(String line) {
      if (line.startsWith("[")) {
        Scanner scan = new Scanner(line.substring(1, line.length() - 1));
        scan.useDelimiter(",");

        this.list = new ArrayList<>();

        int depth = 0;
        StringWriter innerPacket = new StringWriter();
        while (scan.hasNext()) {
          String token = scan.next();

          if (token.startsWith("[")) {
            innerPacket.append(token);
            depth += token.codePoints().filter(c -> c == '[').count();
            depth -= token.codePoints().filter(c -> c == ']').count();
            if (depth == 0) {
              this.list.add(new Packet(innerPacket.toString()));
              innerPacket = new StringWriter();
            } else {
              innerPacket.append(",");
            }
          } else if (token.endsWith("]")) {
            innerPacket.append(token);
            depth -= token.codePoints().filter(c -> c == ']').count();
            if (depth == 0) {
              this.list.add(new Packet(innerPacket.toString()));
              innerPacket = new StringWriter();
            } else {
              innerPacket.append(",");
            }
          } else {
            if (depth == 0) {
              this.list.add(new Packet(Integer.parseInt(token)));
            } else {
              innerPacket.append(token);
              innerPacket.append(",");
            }
          }
        }
      } else {
        this.val = Integer.parseInt(line);
      }
    }

    @Override
    public String toString() {
      if (this.list == null) {
        return Integer.toString(val);
      } else {
        StringWriter sw = new StringWriter();
        sw.append('{');
        for (Packet p : this.list) {
          sw.append(p.toString());
          sw.append(',');
        }
        sw.append('}');
        return sw.toString();
      }
    }

    public boolean isInt() {
      return this.list == null;
    }

    public boolean isList() {
      return this.list != null;
    }

    /**
     * negative: wrong order
     * positive: correct order
     * zero: unknown
     */
    @Override
    public int compareTo(Packet right) {
      Packet left = this;

      if (left.isInt() && right.isInt()) {
        if (left.val < right.val) {
          return 1;
        } else if (left.val > right.val) {
          return -1;
        } else {
          return 0;
        }
      } else if (left.isList() && right.isList()) {
        for (int i = 0; i < left.list.size(); i++) {
          // right ran out, wrong size
          if (i >= right.list.size()) {
            return -1;
          }

          if (left.list.get(i).compareTo(right.list.get(i)) != 0) {
            return left.list.get(i).compareTo(right.list.get(i));
          }
        }
        // left ran out and no other errors, so all good
        if (left.list.size() < right.list.size()) {
          return 1;
        }
        return 0;
      } else {
        if (left.isInt()) {
          return new Packet(Arrays.asList(left)).compareTo(right);
        } else {
          return left.compareTo(new Packet(Arrays.asList(right)));
        }
      }
    }
  }

  public Day13(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("13.txt");

    int pairNum = 1;
    int total = 0;

    for (int i = 0; i < allInput.size(); i++) {
      Packet left = new Packet(allInput.get(i));
      i++;
      Packet right = new Packet(allInput.get(i));

      if (left.compareTo(right) > 0) {
        total += pairNum;
      }

      pairNum++;
      i++;
    }

    log.info("Part one solution: {}", total);

    List<Packet> packets = new ArrayList<>(
      allInput
        .stream()
        .filter(s -> !s.isBlank())
        .map(Packet::new)
        .collect(Collectors.toList())
    );
    packets.add(new Packet("[[2]]"));
    packets.add(new Packet("[[6]]"));

    packets.sort((a, b) -> {
      if (a.compareTo(b) < 0) {
        // b goes before a
        return 1;
      } else {
        return -1;
      }
    });

    List<String> stringPackets = packets
      .stream()
      .map(Packet::toString)
      .toList();

    log.info(
      "Part two solution: {}",
      (stringPackets.indexOf("{{2,},}") + 1) *
      (stringPackets.indexOf("{{6,},}") + 1)
    );
  }
}
