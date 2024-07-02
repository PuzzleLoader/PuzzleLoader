package dev.crmodders.puzzle.class_accessors.transformers;

import org.objectweb.asm.Opcodes;

public enum ClassModifiers {
    PUBLIC(Opcodes.ACC_PUBLIC, Opcodes.ACC_PRIVATE, Opcodes.ACC_PROTECTED),
    PRIVATE(Opcodes.ACC_PRIVATE, Opcodes.ACC_PUBLIC, Opcodes.ACC_PROTECTED),
    DEFAULT(0, Opcodes.ACC_PRIVATE, Opcodes.ACC_PUBLIC, Opcodes.ACC_PROTECTED),
    PROTECTED(Opcodes.ACC_PROTECTED, Opcodes.ACC_PUBLIC, Opcodes.ACC_PRIVATE);

    int newFlag;
    int[] incompatibleFlags;

    ClassModifiers(int newFlag, int... incompatibleFlags) {
        this.newFlag = newFlag;
        this.incompatibleFlags = incompatibleFlags;
    }

    int updateFlags(int oldFlags) {
        int currentFlags = oldFlags;

        for (int opcode : incompatibleFlags) {
            if ((oldFlags & opcode) != 0) {
                currentFlags = (currentFlags ^ opcode);
            }
        }

        return currentFlags | newFlag;
    }

}
