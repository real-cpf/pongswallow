package org.realcpf;

import org.realcpf.executor.ExecCenter;
import org.realcpf.executor.TimeSliceExector;
import org.realcpf.future.PongWorker;
import org.realcpf.future.SliceRunByteBufferFill;
import org.realcpf.future.SliceWorker;
import org.realcpf.future.Worker;
import org.realcpf.run.Run;
import org.realcpf.run.SliceRun;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;

public class Main {
  public static void main(String[] args) {

    test1();

  }
  public static void test2(){
    ExecutorService executor = Executors.newFixedThreadPool(2);
    Queue<Worker> workers = new ConcurrentLinkedQueue<>();
    TimeSliceExector timeSliceExector = new TimeSliceExector(workers);
    executor.execute(timeSliceExector);
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    int last = 0;
    Run[] runs = new Run[16];
    int batch = 0;
    for (int i=15;i<1024;i+=16){
      SliceRun run = new SliceRunByteBufferFill(last,i,buffer);
      last = i+1;
      runs[batch] = run;
      batch++;
      if (batch % 16 == 0){
        timeSliceExector.append(new SliceWorker(runs,String.format("test - %s ",i),4));
        runs = new Run[16];
        batch=0;
      }
    }

    try {
      TimeUnit.SECONDS.sleep(10);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    long start = System.currentTimeMillis();
    buffer.position(0);
    IntStream.range(0,1024).forEach(i->{
      byte b = buffer.get(i);
      if (!(b=='X')) {
        throw new RuntimeException(b+"error "+i);
      }
    });
    System.out.println("cc " + (System.currentTimeMillis() - start));
    System.out.println("test done");
    timeSliceExector.shutdown();
    executor.shutdown();
  }
  public static void test1(){
    ExecutorService executor = Executors.newFixedThreadPool(12);
    ExecCenter execCenter = new ExecCenter(executor);
    executor.execute(execCenter::run);
    LockSupport.parkUntil(1000);
    for (int i=0;i<100;i++){
      execCenter.append(new PongWorker("test"+i));
    }


    try {
      TimeUnit.SECONDS.sleep(100);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    execCenter.shutdown();
    executor.shutdown();

  }
}
