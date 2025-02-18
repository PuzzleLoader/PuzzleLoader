package com.github.puzzle.core.loader.util;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassPathFileEntry {

    boolean isFromArchive;
    String name;
    public @Nullable ZipEntry entry;
    ZipFile parentArchive;
    public @Nullable File file;

    public ClassPathFileEntry(
            boolean isFromArchive,
            String name,
            ZipEntry entry,
            ZipFile parentArchive,
            File file
    ) {
        this.isFromArchive = isFromArchive;
        this.name = name;
        this.entry = entry;
        this.parentArchive = parentArchive;
        this.file = file;
    }

    public byte[] getContents() throws IOException {
        InputStream stream = null;
        byte[] bytes = new byte[0];

        if (parentArchive != null) stream = parentArchive.getInputStream(entry);
        if (stream == null && file.canRead() && file.isFile()) stream = new FileInputStream(file);
        if (stream == null) throw new IOException("Could Not Read \"" + name + "\"");

        bytes = stream.readAllBytes();
        stream.close();
        return bytes;
    }

    public String getName() {
        return name;
    }

    public boolean isFromArchive() {
        return isFromArchive;
    }

    public boolean isFromDirectory() {
        return !isFromArchive();
    }

}
