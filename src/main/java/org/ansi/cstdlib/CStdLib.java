package org.ansi.cstdlib;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

/**
 * A small subset of the C standard library, specifically the parts this codebase uses.
 */
public interface CStdLib extends Library {
  CStdLib INSTANCE = Native.load(Platform.isWindows()? "msvcrt":"c", CStdLib.class);
  void memcpy(Pointer dst, Pointer src, long len);
  void memmove(Pointer dst, Pointer src, long len);

  static void copyMemory(Pointer dst, Pointer src, long len) {
    INSTANCE.memcpy(dst, src, len);
  }
  static void moveMemory(Pointer dst, Pointer src, long len) {
    INSTANCE.memmove(dst, src, len);
  }
}
