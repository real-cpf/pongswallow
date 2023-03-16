package org.realcpf.executor;

import org.realcpf.future.PongWorker;
import org.realcpf.future.Worker;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class ExecCenter {
  private AtomicBoolean ing = new AtomicBoolean(Boolean.TRUE);
  private final Executor executor;
  private final Queue<Worker> workers;
  private Thread current;

  public ExecCenter(Executor executor, Queue<Worker> workers) {
    this.executor = executor;
    this.workers = workers;
  }


  public ExecCenter(Executor executor) {
    this.executor = executor;
    this.workers = new ConcurrentLinkedQueue<>();
  }

  public boolean append(Worker worker) {
    LockSupport.unpark(current);
    this.workers.add(worker);
    return true;
  }

  public void run() {
    this.current = Thread.currentThread();
    while (ing.get()) {
      while (!workers.isEmpty()) {
        CompletableFuture.supplyAsync(() -> {
          Worker worker = workers.poll();
          assert worker != null;
          return worker.worker();
        },executor).whenComplete(((worker, throwable) -> {
          if (null != worker && null == throwable) {
            if (!worker.done()) {
              append(worker);
            }
          }else {
            throwable.printStackTrace();
          }
        }));
      }
      LockSupport.park();
    }
  }
}
