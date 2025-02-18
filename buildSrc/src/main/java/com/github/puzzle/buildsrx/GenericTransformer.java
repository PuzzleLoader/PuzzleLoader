package com.github.puzzle.buildsrx;

import com.github.puzzle.buildsrx.transformers.AbstractClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GenericTransformer {

    public static void transform(File f, AbstractClassTransformer... transformers) {
        try {
            InputStream byteStream = new FileInputStream(f);
            byte[] bytes = byteStream.readAllBytes();
            byteStream.close();

            ZipInputStream input = new ZipInputStream(new ByteArrayInputStream(bytes));
            ZipOutputStream output = new ZipOutputStream(new FileOutputStream(f));
            ZipEntry entry = input.getNextEntry();
            while (entry != null) {
                output.putNextEntry(entry);
                String entryName = entry.getName();

                if (entryName.endsWith(".class") && !entryName.contains("module-info")) {
                    byte[] classBytes = input.readAllBytes();
                    output.write(transformClass(classBytes, transformers));
                }
                else output.write(input.readAllBytes());

                entry = input.getNextEntry();
            }
            input.close();
            output.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] transformClass(byte[] bytes, AbstractClassTransformer... transformers) {
        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);

        for (AbstractClassTransformer transformer : transformers) {
            transformer.setWriter(writer);
            transformer.setClassName(reader.getClassName().replaceAll("\\.", "/"));
            reader.accept(transformer, 2);
        }

        return writer.toByteArray();
    }

}
