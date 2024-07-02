package dev.crmodders.puzzle.access_manipulator.transformers;

import org.objectweb.asm.Opcodes;

public enum ClassModifier {
    MUTABLE(0, new int[]{ Opcodes.ACC_FINAL }),
    PUBLIC(Opcodes.ACC_PUBLIC, new int[]{ Opcodes.ACC_PRIVATE, Opcodes.ACC_PROTECTED }),
    PRIVATE(Opcodes.ACC_PRIVATE, new int[]{ Opcodes.ACC_PUBLIC, Opcodes.ACC_PROTECTED }),
    PROTECTED(Opcodes.ACC_PROTECTED, new int[]{ Opcodes.ACC_PUBLIC, Opcodes.ACC_PRIVATE });

    final int[] newFlags;
    final int[] incompatibleFlags;

    ClassModifier(int[] newFlags, int[] incompatibleFlags) {
        this.newFlags = newFlags;
        this.incompatibleFlags = incompatibleFlags;
    }

    ClassModifier(int newFlag, int[] incompatibleFlags) {
        this.newFlags = new int[]{newFlag};
        this.incompatibleFlags = incompatibleFlags;
    }

    int updateFlags(int oldFlags) {
        int currentFlags = oldFlags;

        for (int flag : incompatibleFlags) {
            if ((oldFlags & flag) != 0) {
                currentFlags = (currentFlags ^ flag);
            }
        }

        for (int flag : newFlags) {
            currentFlags |= flag;
        }

        return currentFlags;
    }

}
