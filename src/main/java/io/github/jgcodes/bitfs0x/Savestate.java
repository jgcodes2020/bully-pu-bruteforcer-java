package io.github.jgcodes.bitfs0x;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class Savestate {
  private static record Info(ByteBuffer buffer, int address) {}
  private final List<Info> buffers;

  public Savestate(Game game) {
    Arrays.stream(game.version.segments())
      .map(region -> {
        ByteBuffer buffer = ByteBuffer.allocateDirect((int) region.size());
      })
  }
}
