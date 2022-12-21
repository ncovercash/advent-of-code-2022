package dev.ncovercash;

import java.io.StringWriter;
import java.util.List;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day20 extends AbstractDay {

  @Data
  static class Node {

    Node prev;
    Node next;
    final long val;

    public void mix(long len) {
      if (val > 0) {
        long mixLen = val % (len - 1);
        for (int i = 0; i < mixLen; i++) {
          Node swappingWith = this.next;
          this.next = swappingWith.next;
          this.next.prev = this;
          swappingWith.next = this;
          swappingWith.prev = this.prev;
          swappingWith.prev.next = swappingWith;
          this.prev = swappingWith;
          this.prev.next = this;
        }
      } else {
        long mixLen = Math.abs(val) % (len - 1);
        for (int i = 0; i < mixLen; i++) {
          Node swappingWith = this.prev;
          this.prev = swappingWith.prev;
          this.prev.next = this;
          swappingWith.prev = this;
          swappingWith.next = this.next;
          swappingWith.next.prev = swappingWith;
          this.next = swappingWith;
          this.next.prev = this;
        }
      }
    }

    public String toString() {
      return Long.toString(val);
    }
  }

  public void printList(Node head) {
    StringWriter sw = new StringWriter();

    Node curr = head;
    do {
      sw.append(String.format("%d-->", curr.val));
      curr = curr.next;
    } while (curr != head);

    log.info(sw.toString());
  }

  public Day20(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("20.txt");

    List<Node> nodes = allInput
      .stream()
      .map(Integer::parseInt)
      .map(Node::new)
      .toList();

    for (int i = 0; i < nodes.size(); i++) {
      nodes.get(i).next = nodes.get((i + 1) % nodes.size());
      nodes.get((i + 1) % nodes.size()).prev = nodes.get(i);
    }

    Node zeroNode = null;

    for (int i = 0; i < nodes.size(); i++) {
      if (nodes.get(i).val == 0) {
        zeroNode = nodes.get(i);
      }
      nodes.get(i).mix(nodes.size());
    }

    int sum = 0;

    for (int n = 0; n < 3; n++) {
      for (int i = 0; i < 1000; i++) {
        zeroNode = zeroNode.next;
      }
      sum += zeroNode.val;
    }

    log.info("Part one: {}", sum);

    // recreate fresh for P2
    nodes =
      allInput
        .stream()
        .map(Integer::parseInt)
        .map(a -> a * 811589153L)
        .map(Node::new)
        .toList();

    zeroNode = null;

    for (int i = 0; i < nodes.size(); i++) {
      nodes.get(i).next = nodes.get((i + 1) % nodes.size());
      nodes.get((i + 1) % nodes.size()).prev = nodes.get(i);
      if (nodes.get(i).val == 0) {
        zeroNode = nodes.get(i);
      }
    }

    for (int n = 0; n < 10; n++) {
      for (int i = 0; i < nodes.size(); i++) {
        nodes.get(i).mix(nodes.size());
      }

      printList(zeroNode);
    }

    sum = 0;

    for (int n = 0; n < 3; n++) {
      for (int i = 0; i < 1000; i++) {
        zeroNode = zeroNode.next;
      }
      sum += zeroNode.val;
    }

    log.info("Part two: {}", sum);
  }
}
