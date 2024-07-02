package dev.crmodders.puzzle.core.launch.transformers;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ClassPublisizor implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        System.out.println("Transformed Class " + name);
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter writer = new ClassWriter(fixFlags(reader));
        reader.accept(writer, ClassReader.SKIP_DEBUG);
        return writer.toByteArray();
    }

    public static int fixFlags(ClassReader reader) {
        int currentAccess = reader.getAccess();
        if ((currentAccess & Opcodes.ACC_PRIVATE) != 0) {
            return (currentAccess ^ Opcodes.ACC_PRIVATE) | Opcodes.ACC_PUBLIC;
        } else {
            return currentAccess;
        }
    }

}
