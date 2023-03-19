package org.realcpf.future;

import java.util.concurrent.TimeUnit;

public class PongWorker implements Worker{
  private int count = 0;
  private final String name;
  public PongWorker(String name) {
    this.name = name;
  }
  @Override
  public Worker worker() {
    System.out.println(name + " working... "+Thread.currentThread());
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    count++;
    return this;
  }

  @Override
  public boolean done() {
    return count == 10;
  }

  @Override
  public String toString() {
    return "PongWorker{" +
      "count=" + count +
      ", name='" + name + '\'' +
      '}';
  }
}
