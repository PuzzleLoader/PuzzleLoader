package com.github.puzzle.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

public class ZipUtil {

    public static final Map<String, FileSystem> FILE_SYSTEM_CACHE = new HashMap<>();

    public static FileSystem getFileSystemFromZip(File file) {
        if (FILE_SYSTEM_CACHE.get(file.getAbsolutePath()) != null) return FILE_SYSTEM_CACHE.get(file.getAbsolutePath());

        FileSystem fs;
        try (FileSystem fs0 = FileSystems.newFileSystem(file.getAbsoluteFile().toPath(), (ClassLoader) null)) {
            FILE_SYSTEM_CACHE.put(file.getAbsolutePath(), fs = fs0);
        } catch (IOException e) {
            throw new RuntimeException("Invalid Zipfile \"" + file.getAbsolutePath() + "\" cannot have its own file system!");
        }

        return fs;
    }

    public static byte[] getBytesFromFileInZip(ZipFile file, String entryName) {
        try {
            InputStream stream = file.getInputStream(file.getEntry(entryName));
            byte[] bytes = stream.readAllBytes();
            stream.close();
            return bytes;
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static String getStringFromFileInZip(ZipFile file, String entryName) {
        try {
            InputStream stream = file.getInputStream(file.getEntry(entryName));
            byte[] bytes = stream.readAllBytes();
            stream.close();
            return new String(bytes);
        } catch (IOException e) {
            return "";
        }
    }

}
