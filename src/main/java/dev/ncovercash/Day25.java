package dev.ncovercash;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Day25 extends AbstractDay {

  public BigInteger decode(char input) {
    switch (input) {
      case '=':
        return BigInteger.valueOf(-2);
      case '-':
        return BigInteger.valueOf(-1);
      case '0':
        return BigInteger.valueOf(0);
      case '1':
        return BigInteger.valueOf(1);
      case '2':
      default:
        return BigInteger.valueOf(2);
    }
  }

  public char encode(int input) {
    switch (input) {
      case -2:
        return '=';
      case -1:
        return '-';
      case 0:
        return '0';
      case 1:
        return '1';
      case 2:
      default:
        return '2';
    }
  }

  public String reverse(String input) {
    StringWriter writer = new StringWriter();
    for (int i = input.length() - 1; i >= 0; i--) {
      writer.append(input.charAt(i));
    }
    return writer.toString();
  }

  public BigInteger decode(String input) {
    BigInteger result = BigInteger.valueOf(0);

    String lsb = reverse(input);
    for (int i = 0; i < lsb.length(); i++) {
      result =
        result.add(
          BigInteger.valueOf(5).pow(i).multiply(decode(lsb.charAt(i)))
        );
    }

    return result;
  }

  public String encode(BigInteger input) {
    List<Integer> lsbBase5 = new ArrayList<>();
    String lsbStr = reverse(input.toString(5));

    for (int i = 0; i < lsbStr.length(); i++) {
      lsbBase5.add(Character.digit(lsbStr.charAt(i), 5));
    }

    for (int i = 0; i < lsbBase5.size(); i++) {
      while (lsbBase5.get(i) > 2) {
        if (i == lsbBase5.size() - 1) {
          lsbBase5.add(0);
        }
        lsbBase5.set(i, lsbBase5.get(i) - 5);
        lsbBase5.set(i + 1, lsbBase5.get(i + 1) + 1);
      }
    }

    StringWriter result = new StringWriter();
    for (int d : lsbBase5) {
      result.append(encode(d));
    }

    return reverse(result.toString());
  }

  public Day25(String[] args) {
    super(args);
  }

  protected void completeChallenge() {
    List<String> allInput = InputUtils.getLines("25.txt");

    BigInteger sum = allInput
      .stream()
      .map(this::decode)
      .reduce(BigInteger.ZERO, (a, b) -> a.add(b));
    log.info("Part one solution: {}", encode(sum));
  }
}
