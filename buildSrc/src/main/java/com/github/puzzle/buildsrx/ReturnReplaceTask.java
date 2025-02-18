package com.github.puzzle.buildsrx;

import io.github.puzzle.cosmic.util.ChangeType;
import io.github.puzzle.cosmic.util.ApiDeclaration;
import org.codehaus.groovy.classgen.AsmClassGenerator;
import org.gradle.api.DefaultTask;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ReturnReplaceTask extends DefaultTask {

    public ReturnReplaceTask() {
        setGroup("buildSrx");
    }

    public static void transform(File f) {
        try {
            FileInputStream s = new FileInputStream(f);
            System.out.println(s.available());
            byte[] bytes = s.readAllBytes();
            s.close();

            MiniClassLoader.INSTANCE.addURL(f.toURL());
            ZipInputStream inputJar = new ZipInputStream(new ByteArrayInputStream(bytes));

            ZipOutputStream outputJar = new ZipOutputStream(new FileOutputStream(f));
            System.out.println(bytes.length);

            inputJar = new ZipInputStream(new ByteArrayInputStream(bytes));
            ZipEntry currentEntry = inputJar.getNextEntry();
            while (currentEntry != null) {
                String entryName = currentEntry.getName();
                if (entryName.endsWith(".class")) {
                    byte[] bytez = inputJar.readAllBytes();
                    ClassReader reader = new ClassReader(bytez);
                    String name = reader.getClassName();

                    try {
                        MiniClassLoader.INSTANCE.loadFromBytes(name.replaceAll("[/]", "."), bytez);
                    } catch (Exception e) {}
                }

                currentEntry = inputJar.getNextEntry();
            }

            inputJar = new ZipInputStream(new ByteArrayInputStream(bytes));
            currentEntry = inputJar.getNextEntry();
            while (currentEntry != null) {
                outputJar.putNextEntry(currentEntry);

                String entryName = currentEntry.getName();
                if (entryName.endsWith(".class")) {
                    outputJar.write(transformClass(inputJar.readAllBytes()));
                }
                else
                    outputJar.write(inputJar.readAllBytes());

                currentEntry = inputJar.getNextEntry();
            }

            inputJar.close();
            outputJar.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] transformClass(byte[] bytes) throws Exception {
        ClassReader reader = new ClassReader(bytes);
        String name = reader.getClassName();

        ClassWriter writer = new ClassWriter(reader, reader.getAccess());
        Visitor visitor = new Visitor(AsmClassGenerator.ASM9, writer);

        Class<?> clazz = MiniClassLoader.INSTANCE.loadClass(name.replaceAll("[/]", "."));
        {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String[] returnReplace = null;
                for (Annotation a : method.getAnnotations()) {
                    if (a.annotationType().toString().equals(ChangeType.class.toString())) {
                        returnReplace = (String[]) a.annotationType().getMethods()[1].invoke(a);
                        System.out.println(Arrays.toString(returnReplace));
                    }
                }
                if (returnReplace != null) {
                    String clazzToReturn = "L" + returnReplace[0] + ";";
                    visitor.getReplaceList().put("as", clazzToReturn);
                }
            }
        }

        reader.accept(visitor, 2);
        return writer.toByteArray();
    }

    private static byte[] transformClass2(byte[] bytes) throws Exception {
        ClassReader reader = new ClassReader(bytes);
        String name = reader.getClassName();

        ClassWriter writer = new ClassWriter(reader, reader.getAccess());
        Visitor visitor = new Visitor(AsmClassGenerator.ASM9, writer);

        Class<?> clazz = MiniClassLoader.INSTANCE.loadClass(name.replaceAll("[/]", "."));
        Object[] annotation = Arrays.stream(clazz.getAnnotations()).filter((p) -> p.annotationType().toString().equals(ApiDeclaration.class.toString())).toArray();
        if (annotation.length != 0) {
            Class<?> apiClass = (Class<?>) annotation[0].getClass().getDeclaredMethod("api").invoke(annotation[0]);
            String implClassName = (String) annotation[0].getClass().getDeclaredMethod("impl").invoke(annotation[0]);
            Class<?> implClass = findClassByNameNoDupes(implClassName, packages.toArray(new String[0]));
            visitor.change(apiClass, implClass);
        }
        {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String returnReplace = null;
                boolean removeAsMethod = false;
                for (Annotation a : method.getAnnotations()) {
                    if (a.annotationType().toString().equals(ChangeType.class.toString())) {
                        returnReplace = (String) a.annotationType().getMethods()[1].invoke(a);
                        removeAsMethod = (boolean) a.annotationType().getMethods()[0].invoke(a);
                    }
                }
                if (returnReplace != null) {
                    Class<?> clazz0 = findClassByNameNoDupes(returnReplace, packages.toArray(new String[0]));
                    if (clazz0 == null) {
                        System.out.println("Could not find class for " + returnReplace);
                        if (removeAsMethod) {
                            System.out.println("REMOVING \".as()\" METHOD");
                            visitor.getReplaceList().put("as", "REMOVE_ME");
                        }
                    } else {
                        String clazzToReturn = clazz0.descriptorString();
                        visitor.getReplaceList().put("as", clazzToReturn);
                    }
                }
            }
        }

        reader.accept(visitor, 2);
        return writer.toByteArray();
    }

    public static final Class<?> findClassByNameNoDupes(String classname, String[] searchPackages) {

        Class<?> foundClass = null;
        for(int i=0; i < searchPackages.length; i++){
            try{
                boolean wasNull = foundClass == null;
                if (!searchPackages[i].contains("ui")) {
                    foundClass = MiniClassLoader.INSTANCE.loadClass(searchPackages[i] + "." + classname);
                    System.out.println("Found " + foundClass);
                    if (!wasNull) throw new RuntimeException(classname + " exists in multiple packages!");
                }
            } catch (ClassNotFoundException e){
                //not in this package, try another
            }
        }
        return foundClass;
    }

    static Set<String> packages = new HashSet<>();

    public static File transform2(File f, File crJar) {
        try {
            FileInputStream s = new FileInputStream(f);
            System.out.println(s.available());
            byte[] bytes = s.readAllBytes();
            s.close();

            MiniClassLoader.INSTANCE.addURL(f.toURL());
            MiniClassLoader.INSTANCE.addURL(crJar.toURL());
            new ZipInputStream(new ByteArrayInputStream(bytes));
            ZipInputStream inputJar;

            ZipOutputStream outputJar = new ZipOutputStream(new FileOutputStream(f));
            System.out.println(bytes.length);

            inputJar = new ZipInputStream(new FileInputStream(crJar));
            ZipEntry currentEntry = inputJar.getNextEntry();
            while (currentEntry != null) {
                String entryName = currentEntry.getName();
                if (entryName.endsWith(".class") && !entryName.contains("module-info")) {
                    byte[] bytez = inputJar.readAllBytes();
                    ClassReader reader = new ClassReader(bytez);
                    String name = reader.getClassName();

                    try {
                        Class<?> clazz = MiniClassLoader.INSTANCE.loadClass(name.replaceAll("[/]", "."));
                        if (entryName.contains("finalforeach/cosmicreach"))
                            packages.add(clazz.getPackageName());
                    } catch (Exception ignore) {
                        Class<?> clazz = MiniClassLoader.INSTANCE.loadFromBytes(name.replaceAll("[/]", "."), bytez);
                        if (entryName.contains("finalforeach/cosmicreach"))
                            packages.add(clazz.getPackageName());
                    }
                }

                currentEntry = inputJar.getNextEntry();
            }

            inputJar = new ZipInputStream(new ByteArrayInputStream(bytes));
            currentEntry = inputJar.getNextEntry();
            while (currentEntry != null) {
                String entryName = currentEntry.getName();
                if (entryName.endsWith(".class")) {
                    byte[] bytez = inputJar.readAllBytes();
                    ClassReader reader = new ClassReader(bytez);
                    String name = reader.getClassName();

                    try {
                        MiniClassLoader.INSTANCE.loadClass(name.replaceAll("[/]", "."));
                    } catch (Exception e) {
                        MiniClassLoader.INSTANCE.loadFromBytes(name.replaceAll("[/]", "."), bytez);
                    }
                }

                currentEntry = inputJar.getNextEntry();
            }

            inputJar = new ZipInputStream(new ByteArrayInputStream(bytes));
            currentEntry = inputJar.getNextEntry();
            while (currentEntry != null) {
                outputJar.putNextEntry(currentEntry);

                String entryName = currentEntry.getName();
                if (entryName.endsWith(".class")) {
                    outputJar.write(transformClass2(inputJar.readAllBytes()));
                }
                else
                    outputJar.write(inputJar.readAllBytes());

                currentEntry = inputJar.getNextEntry();
            }

            inputJar.close();
            outputJar.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return f;
    }
}
