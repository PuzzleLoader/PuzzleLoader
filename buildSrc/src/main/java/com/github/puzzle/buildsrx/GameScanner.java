package com.github.puzzle.buildsrx;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GameScanner {

    public static final Set<String> packages = new HashSet<>();
    public static final Set<String> classes = new HashSet<>();

    public static void scan(File f) {
        try {
            InputStream byteStream = new FileInputStream(f);
            byte[] bytes = byteStream.readAllBytes();
            byteStream.close();

            ZipInputStream input = new ZipInputStream(new ByteArrayInputStream(bytes));
            ZipEntry entry = input.getNextEntry();
            while (entry != null) {
                String entryName = entry.getName();

                if (entryName.endsWith(".class") && !entryName.contains("module-info") && entryName.contains("finalforeach/cosmicreach")) {
                    String clazz = entryName;
                    clazz = clazz.replaceAll("\\.class", "");
                    clazz = clazz.replaceAll("/", ".");
                    classes.add(clazz);

                    String[] parts = clazz.split("\\.");

                    StringBuilder pkgBuilder = new StringBuilder();
                    for (int i = 0; i < parts.length - 1; i++)
                        pkgBuilder.append(parts[i]).append(i == parts.length - 2 ? "" : ".");

                    packages.add(pkgBuilder.toString());
                }

                entry = input.getNextEntry();
            }
            input.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String findClassByNameNoDupes(String className) {
        String foundClass = null;
        for (String pkg : packages){
            try {
                boolean wasNull = foundClass == null;
                if (!pkg.contains("ui")) {
                    if (!classes.contains(pkg + "." + className))
                        throw new ClassNotFoundException(pkg, new Exception("Could not find class within our indexes!"));

                    foundClass = pkg + "." + className;
                    if (!wasNull) throw new RuntimeException(className + " exists in multiple packages!");
                }
            } catch (ClassNotFoundException e){
                //not in this package, try another
            }
        }
        return foundClass;
    }

}
