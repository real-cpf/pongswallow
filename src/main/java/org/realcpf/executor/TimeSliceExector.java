package org.realcpf.executor;


import org.realcpf.future.Worker;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class TimeSliceExector implements Runnable{
  private AtomicBoolean ing = new AtomicBoolean(Boolean.TRUE);
  private AtomicBoolean waitting = new AtomicBoolean(Boolean.FALSE);
  private final Queue<Worker> workers;
  private long ts = 0L;
  public TimeSliceExector(Queue<Worker> workers) {
    this.workers = workers;
  }
  private Thread current;

  public void append(Worker worker) {
    if (waitting.get()) {
      this.workers.add(worker);
      LockSupport.unpark(current);
      waitting.compareAndSet(Boolean.TRUE, Boolean.FALSE);
    } else {
      this.workers.add(worker);
    }
  }

  public void run() {
    this.current = Thread.currentThread();
    while (ing.get()) {
      long start = System.currentTimeMillis();
      while (!workers.isEmpty()) {
        Worker worker = workers.poll();
        worker.worker();
        if (!worker.done()) {
          append(worker);
        }
      }
      ts = ts + (System.currentTimeMillis() - start);
      waitting.compareAndSet(Boolean.FALSE, Boolean.TRUE);
      LockSupport.park();
    }
  }
  public void shutdown(){
    System.out.println("cost " + ts);
    this.ing.set(Boolean.FALSE);
    LockSupport.unpark(current);
  }

}
