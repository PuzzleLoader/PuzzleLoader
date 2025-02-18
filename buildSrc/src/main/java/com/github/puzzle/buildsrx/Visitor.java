package com.github.puzzle.buildsrx;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

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
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//        System.out.println(name);
        if (name.equals("as"))
            descriptor = "()" + replaceList.get(name);
        if (replaceList.get(name) != null && replaceList.get(name).contains("REMOVE_ME")) return null;
//        String desc = descriptor.replaceFirst(Object.class.descriptorString(), "");
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
