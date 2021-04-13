package io.github.jgcodes.bitfs0x;

/**
 * A region of virtual address space that can be stored and restored to essentially "rewind".
 */
public record MemoryRegion(long offset, long size) {}
