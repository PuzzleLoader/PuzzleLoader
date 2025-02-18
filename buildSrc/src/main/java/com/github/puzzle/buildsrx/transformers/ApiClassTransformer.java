package com.github.puzzle.buildsrx.transformers;

import com.github.puzzle.buildsrx.GameScanner;
import io.github.puzzle.cosmic.util.ApiDeclaration;
import org.gradle.internal.impldep.bsh.org.objectweb.asm.Constants;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Objects;

public class ApiClassTransformer extends AbstractClassTransformer {

    String api;
    String impl;

    String apiName;
    String implName;

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new AnnotationScanner(descriptor, super.visitAnnotation(descriptor, visible));
    }

    @Override
    public void visitEnd() {
        if (api == null) {
            super.visitEnd();
            return;
        }

        MethodVisitor as = super.visitMethod(
                Opcodes.ACC_PUBLIC,
                "as",
                "()L" + impl + ";",
                null, new String[0]);
        implementDefault(as, impl);
//        implementDefault(as, "L" + impl + ";");

        MethodVisitor convert0 = super.visitMethod(
                Opcodes.ACC_PUBLIC | Constants.ACC_STATIC,
                "as",
                "(L" + impl + ";)L" + api + ";",
                null, new String[0]
        );
//        implementStatic(convert0, implName, "L" + api + ";");
        implementStatic(convert0, implName, api);

        MethodVisitor convert1 = super.visitMethod(
                Opcodes.ACC_PUBLIC | Constants.ACC_STATIC,
                "as",
                "(L" + api + ";)L" + impl + ";",
                null, new String[0]);
//        implementStatic(convert1, apiName, "L" + impl + ";");
        implementStatic(convert1, apiName, impl);

        super.visitEnd();
    }

    private void implementDefault(MethodVisitor visitor, String descriptor) {
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        visitor.visitTypeInsn(Opcodes.CHECKCAST, Object.class.getName().replaceAll("\\.", "/"));
        visitor.visitTypeInsn(Opcodes.CHECKCAST, descriptor);
        visitor.visitInsn(Opcodes.LRETURN);
        visitor.visitMaxs(-1, -1);
    }

    private void implementStatic(MethodVisitor visitor, String name, String descriptor) {
        visitor.visitParameter(name, 0);
        visitor.visitVarInsn(Opcodes.ALOAD, 0);
        visitor.visitTypeInsn(Opcodes.CHECKCAST, Object.class.getName().replaceAll("\\.", "/"));
        visitor.visitTypeInsn(Opcodes.CHECKCAST, descriptor);
        visitor.visitInsn(Opcodes.LRETURN);
        visitor.visitMaxs(-1, -1);
    }

    public class AnnotationScanner extends AnnotationVisitor {

        String descriptor;

        protected AnnotationScanner(String descriptor, AnnotationVisitor visitor) {
            super(Opcodes.ASM9);

            this.descriptor = descriptor;
            av = visitor;
        }

        @Override
        public void visit(String name, Object value) {
            if (Objects.equals(descriptor, ApiDeclaration.class.descriptorString())){
                switch (name) {
                    case "api" -> {
                        ApiClassTransformer.this.api = ((Type) value).getClassName().replaceAll("\\.", "/");
                        String[] parts = ApiClassTransformer.this.api.split("/");
                        ApiClassTransformer.this.apiName = parts[parts.length - 1].toLowerCase();
                    }
                    case "impl" -> {
                        ApiClassTransformer.this.impl = GameScanner.findClassByNameNoDupes((String) value).replaceAll("\\.", "/");
                        ApiClassTransformer.this.implName = ((String) value).toLowerCase();
                    }
                }
            }
            super.visit(name, value);
        }
    }

}
