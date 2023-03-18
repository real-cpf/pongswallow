package org.realcpf;

import org.realcpf.executor.ExecCenter;
import org.realcpf.future.PongWorker;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Main {
  public static void main(String[] args) {
    Executor executor = Executors.newFixedThreadPool(12);
    ExecCenter execCenter = new ExecCenter(executor);
    String[] names = new String[]{"A","B","C","D","E"};
    executor.execute(execCenter::run);
//    LockSupport.parkUntil(1000);
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    execCenter.append(new PongWorker("test-a"));
//    for (String name:names) {
//      execCenter.append(new PongWorker(name));
//    }
//    for (int i=0;i<100;i++){
//      execCenter.append(new PongWorker("test"+i));
//    }
  }
}
