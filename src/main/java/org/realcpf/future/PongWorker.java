package org.realcpf.future;

public class PongWorker implements Worker{
  private int count = 0;
  private final String name;
  public PongWorker(String name) {
    this.name = name;
  }
  @Override
  public Worker worker() {
    System.out.println(name + " start work "+Thread.currentThread());
    count++;
    System.out.println(name + " done work "+Thread.currentThread());
    return this;
  }

  @Override
  public boolean done() {
    System.out.println("count "+count);
    return count == 10;
  }
}
