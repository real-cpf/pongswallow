package org.realcpf.future;

import org.realcpf.run.Run;

import java.util.Objects;

public class SliceWorker implements Worker{

  private int totalRunCount;
  private Run[] runs;
  private final String name;
  private final int batch;
  public SliceWorker(Run[] runs,String name,int batch) {
    Objects.requireNonNull(runs,"run must not be null");
    this.runs = runs;
    this.name = name;
    this.totalRunCount = runs.length;
    this.batch = batch;
  }
  @Override
  public Worker worker() {
    int _batch = this.batch;
    while (_batch > 0 && this.totalRunCount >0){
      int index = this.totalRunCount - 1;
      if (null != this.runs[index]) {
        this.runs[index].once();
        this.totalRunCount=this.totalRunCount-1;
        this.runs[index] = null;
      }
      --_batch;
    }
    return this;
  }

  @Override
  public boolean done() {
    return this.totalRunCount == 0;
  }

  @Override
  public String toString() {
    return "SliceWorker{" +
      "totalRunCount=" + totalRunCount +
      ", name='" + name + '\'' +
      '}';
  }
}
