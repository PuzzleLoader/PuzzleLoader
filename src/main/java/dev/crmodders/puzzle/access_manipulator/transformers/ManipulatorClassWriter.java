package dev.crmodders.puzzle.access_manipulator.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ManipulatorClassWriter {

    ClassReader reader;
    ClassWriter writer;

    String className;

    public ManipulatorClassWriter(String className, byte[] bytes) {
        this.className = className;

        reader = new ClassReader(bytes);
        if (AccessManipulatorTransformer.classesToModify.containsKey(className)) {
            int updatedAccess = AccessManipulatorTransformer.classesToModify.get(className).updateFlags(reader.getAccess());
            writer = new ClassWriter(reader, updatedAccess);
        } else writer = new ClassWriter(reader, reader.getAccess());
    }

    public byte[] applyManipulations() {
        reader.accept(new ClassTransformerVisitor(className, Opcodes.ASM9, writer), ClassReader.SKIP_DEBUG);
        return writer.toByteArray();
    }

}
