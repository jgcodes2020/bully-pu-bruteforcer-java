package io.github.jgcodes.bitfs0x;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

public class Game {
  public enum Version {
    /**
     * Represents the original Japanese version of SM64.<br>
     * This version has a few extra glitches that later versions lack.
     */
    JP(
      new MemoryRegion(1302528L, 2836576L),
      new MemoryRegion(14045184L, 4897408L)
    ),
    /**
     * Represents the US version of SM64.<br>
     * This version had some glitches patched (e.g. timestop, spawning displacement),
     * but for the most part is basically as glitchy.
     */
    US(
      new MemoryRegion(1294336L, 2406112L),
      new MemoryRegion(13594624L, 4897632L)
    );

    private final MemoryRegion[] segments;

    Version(MemoryRegion... segments) {
      this.segments = segments;
    }

    public MemoryRegion[] segments() {
      return segments;
    }
  }
  private static PathMatcher isDLL = FileSystems.getDefault().getPathMatcher("glob:*.dll");
  // Proxy interfaces
  public final SuperMario64 sm64;
  public final NativeLibrary sm64Lib;
  // Private properties
  final Version version;

  public Game(Version version, Path dllPath) throws FileNotFoundException {
    if (!isDLL.matches(dllPath)) throw new FileNotFoundException("File is not a DLL");
    this.version = version;
    // Load sm64_jp.dll
    System.setProperty("jna.library.path", dllPath.getParent().toAbsolutePath().toString());
    final String dllName = dllPath.getFileName().toString();
    sm64 = Native.load(dllName, SuperMario64.class);
    sm64.sm64_init();
    sm64Lib = NativeLibrary.getInstance(dllName);
  }

  public void advance(Input input) {
    setInput(input);
    advance();
  }

  public void advance() {
    sm64.sm64_update();
  }

  public Pointer locate(String name) {
    return sm64Lib.getGlobalVariableAddress(name);
  }

  private void setInput(Input input) {
    Pointer controlPtr = locate("gControllerPads");
    controlPtr.setShort(0, input.buttons());
    controlPtr.setByte(2, input.joyX());
    controlPtr.setByte(3, input.joyY());
  }
}
