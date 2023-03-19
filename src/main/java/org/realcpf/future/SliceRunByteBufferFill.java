package org.realcpf.future;

import org.realcpf.run.SliceRun;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

public class SliceRunByteBufferFill extends SliceRun {
  private final int start;
  private final int end;
  private final ByteBuffer buffer;
  public SliceRunByteBufferFill(int start, int end, ByteBuffer buffer) {
    this.start = start;
    this.end = end;
    this.buffer = buffer;
  }

  @Override
  public void once() {
    IntStream.rangeClosed(start,end).forEach(i->{
      buffer.put(i,(byte) 'X');
    });
  }
}
