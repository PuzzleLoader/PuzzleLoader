package com.github.puzzle.buildsrx.transformers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class AbstractClassTransformer extends ClassVisitor {

    String className;

    protected AbstractClassTransformer() {
        super(Opcodes.ASM9);
    }

    public void setWriter(ClassWriter writer) {
        cv = writer;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
