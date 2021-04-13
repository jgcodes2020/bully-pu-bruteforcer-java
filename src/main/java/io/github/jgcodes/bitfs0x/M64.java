package io.github.jgcodes.bitfs0x;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class M64 {
  PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.m64");

  private List<Input> inputs;

  /**
   * Reads a .m64 file, converting it into an internal input list.
   *
   * @param path Path to the .m64 file
   * @throws FileNotFoundException if the file does not exist or isn't an .m64
   * @throws IOException if some other I/O error occurs or the .m64 does not conform to the format
   */
  public M64(Path path) throws IOException {
    if (!matcher.matches(path))
      throw new FileNotFoundException("File is not an .m64 (Mupen movie)");

    inputs = new ArrayList<>();

    try (FileChannel in = FileChannel.open(path, StandardOpenOption.READ)) {
      byte[] bytes = new byte[4];
      ByteBuffer buffer = ByteBuffer.wrap(bytes);
      int nRead;

      in.position(0x400L);
      while ((nRead = in.read(buffer.clear())) > 0) {
        inputs.add(new Input(bytes));
      }
    }
    catch (IndexOutOfBoundsException e) {
      throw new IOException(".m64 file is not formatted correctly");
    }
  }
}
