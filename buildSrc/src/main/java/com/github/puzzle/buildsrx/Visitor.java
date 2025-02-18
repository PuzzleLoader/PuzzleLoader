package com.github.puzzle.buildsrx;

import org.gradle.internal.impldep.bsh.org.objectweb.asm.Constants;
import org.objectweb.asm.*;

import java.util.HashMap;
import java.util.Map;

public class Visitor extends ClassVisitor {

    Map<String, String> replaceList = new HashMap<>();

    protected Visitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    public void setReplaceList(Map<String, String> replaceList) {
        this.replaceList = replaceList;
    }

    public Map<String, String> getReplaceList() {
        return replaceList;
    }

    @Override
    public void visitEnd() {
        System.out.println(apiClass + " " + implClass);
        if (apiClass != null) {
            MethodVisitor visitor = super.visitMethod(Constants.ACC_PUBLIC, "as", "()" + implClass.descriptorString(), null, new String[0]);
            implDefault(visitor);

            visitor = super.visitMethod(Constants.ACC_PUBLIC | Constants.ACC_STATIC, "as", "(" + implClass.descriptorString() + ")" + apiClass.descriptorString(), null, new String[0]);
            implStatic(visitor);

            visitor = super.visitMethod(Constants.ACC_PUBLIC | Constants.ACC_STATIC, "as", "(" + apiClass.descriptorString() + ")" + implClass.descriptorString(), null, new String[0]);
            implStatic2(visitor);
        }

        super.visitEnd();
    }

    private void implDefault(MethodVisitor visitor) {
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        visitor.visitTypeInsn(Opcodes.CHECKCAST, implClass.descriptorString());
        visitor.visitInsn(Opcodes.LRETURN);
    }

    private void implStatic(MethodVisitor visitor) {
        visitor.visitParameter(implClass.getSimpleName().toLowerCase(), 0);
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        visitor.visitTypeInsn(Opcodes.CHECKCAST, Object.class.descriptorString());
        visitor.visitTypeInsn(Opcodes.CHECKCAST, apiClass.descriptorString());
        visitor.visitInsn(Opcodes.LRETURN);
    }

    private void implStatic2(MethodVisitor visitor) {
        visitor.visitParameter(apiClass.getSimpleName().toLowerCase(), 0);
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        visitor.visitTypeInsn(Opcodes.CHECKCAST, Object.class.descriptorString());
        visitor.visitTypeInsn(Opcodes.CHECKCAST, implClass.descriptorString());
        visitor.visitInsn(Opcodes.LRETURN);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println(name + " " + signature);
//        System.out.println(name);
        if (replaceList.get(name) != null)
            descriptor = "()" + replaceList.get(name);
        if (replaceList.get(name) != null && replaceList.get(name).contains("REMOVE_ME")) return null;
//        String desc = descriptor.replaceFirst(Object.class.descriptorString(), "");
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    Class apiClass;
    Class implClass;

    public void change(Class<?> apiClass, Class<?> implClass) {
        this.apiClass = apiClass;
        this.implClass = implClass;
    }
}
