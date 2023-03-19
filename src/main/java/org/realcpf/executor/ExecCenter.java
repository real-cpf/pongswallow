package org.realcpf.executor;

import org.realcpf.future.Worker;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

public class ExecCenter {
  private AtomicBoolean ing = new AtomicBoolean(Boolean.TRUE);
  private AtomicBoolean waitting = new AtomicBoolean(Boolean.FALSE);
  private final Executor executor;
  private final Queue<Worker> workers;
  private Thread current;
  private long ts;
  private final Object lock = new Object();

  public ExecCenter(Executor executor, Queue<Worker> workers) {
    this.executor = executor;
    this.workers = workers;
  }


  public ExecCenter(Executor executor) {
    this.executor = executor;
    this.workers = new ConcurrentLinkedQueue<>();
  }

  public void append(Worker worker) {
    if (waitting.get()) {
      this.workers.add(worker);
      LockSupport.unpark(current);
      waitting.compareAndSet(Boolean.TRUE, Boolean.FALSE);
    } else {
      this.workers.add(worker);
    }
  }
  private void applyAsync(Supplier<Worker> worker, Executor executor, Queue<Worker> workers){
    CompletableFuture.supplyAsync(worker,executor).whenComplete((w, e)->{
      if (!w.done()) {
        append(w);
      }else {
        System.out.println(w + " done");
      }
    });
  }

  public void run() {
    this.current = Thread.currentThread();
    while (ing.get()) {
      long start = System.currentTimeMillis();
      while (!workers.isEmpty()) {
        Worker worker = workers.poll();
        applyAsync(worker::worker,executor,workers);
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
