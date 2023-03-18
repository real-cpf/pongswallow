package org.realcpf.future;

import org.realcpf.run.Run;

import java.util.Objects;

public class SliceWorker implements Worker{

  private int totalRunCount;
  private Run[] runs;
  public SliceWorker(Run[] runs) {
    Objects.requireNonNull(runs,"run must not be null");
    this.runs = runs;
    this.totalRunCount = runs.length;
  }
  @Override
  public Worker worker() {
    while (this.totalRunCount > 0){
      int index = this.totalRunCount - 1;
      this.runs[index].once();
      this.totalRunCount=this.totalRunCount-1;
      this.runs[index] = null;
    }
    return this;
  }

  @Override
  public boolean done() {
    return this.totalRunCount == 0;
  }
}
