package dev.ncovercash;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public abstract class AbstractDay {

  private static final double NS_IN_MS = 1000000.0;

  protected String[] args;

  abstract void completeChallenge();

  public void run() {
    long startTime = System.nanoTime();

    completeChallenge();

    long endTime = System.nanoTime();

    log.info("That took " + (endTime - startTime) / NS_IN_MS + " milliseconds");
  }
}
